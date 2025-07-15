package com.smartsplit.splitservice.Repository.Implementation;

import com.smartsplit.splitservice.Model.Friend;
import com.smartsplit.splitservice.Model.FriendPayment;
import com.smartsplit.splitservice.Model.FriendSplit;
import com.smartsplit.splitservice.Model.Receipt;
import com.smartsplit.splitservice.Model.ReceiptItemSplit;
import com.smartsplit.splitservice.Model.ReceiptWithId;
import com.smartsplit.splitservice.Repository.SplitRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class SplitRepositoryImpl implements SplitRepository {

    private final JdbcClient jdbcClient;

    public SplitRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void createSplitBill(String payerId, Receipt receipt) {
        String accessToken;
        Integer billId = null;

        accessToken = UUID.randomUUID().toString();

        billId = jdbcClient
                .sql("""
                            INSERT INTO bills (name, creator_id, extra_charges, rounding, created_at, public_access_token)
                            VALUES (:name, :creatorId, :extraCharges, :rounding, :createdAt, :token)
                            RETURNING id
                        """)
                .param("name", receipt.getName())
                .param("creatorId", payerId)
                .param("extraCharges", receipt.getAdditionalChargesPercent())
                .param("rounding", receipt.getRoundingAdjustment())
                .param("createdAt", receipt.getNow())
                .param("token", accessToken)
                .query(Integer.class)
                .single();

        Map<Friend, Integer> participantIdMap = new HashMap<>();

        for (ReceiptItemSplit itemSplit : receipt.getSplits()) {
            Integer billItemId = jdbcClient.sql("""
                        INSERT INTO bill_items (bill_id, item_name, quantity, price)
                        VALUES (:billId, :itemName, :quantity, :price)
                        RETURNING id
                    """)
                    .param("billId", billId)
                    .param("itemName", itemSplit.getItemName())
                    .param("quantity", itemSplit.getQuantity())
                    .param("price", itemSplit.getTotalPrice())
                    .query(Integer.class)
                    .single();

            for (FriendSplit friendSplit : itemSplit.getFriendSplits()) {
                Friend friend = friendSplit.getFriend();

                Integer participantId = participantIdMap.get(friend);
                if (participantId == null) {
                    String accountId = friend.getId();
                    String guestName = friend.getUsername();

                    boolean isPaid = payerId.equals(accountId);
                    LocalDateTime paidAt = isPaid ? receipt.getNow() : null;

                    participantId = jdbcClient.sql("""
                                INSERT INTO split_participants (bill_id, account_id, guest_name, is_paid, paid_at)
                                VALUES (:billId, :accountId, :guestName, :isPaid, :paidAt)
                                RETURNING id
                            """)
                            .param("billId", billId)
                            .param("accountId", accountId)
                            .param("guestName", guestName)
                            .param("isPaid", isPaid)
                            .param("paidAt", paidAt)
                            .query(Integer.class)
                            .single();

                    participantIdMap.put(friend, participantId);
                }

                jdbcClient.sql("""
                            INSERT INTO item_shares (bill_item_id, participant_id, quantity_share)
                            VALUES (:billItemId, :participantId, :quantityShare)
                        """)
                        .param("billItemId", billItemId)
                        .param("participantId", participantId)
                        .param("quantityShare", friendSplit.getQuantity())
                        .update();
            }
        }
    }

    @Override
    public Optional<ReceiptWithId> findReceiptById(int billId) {
        List<Map<String, Object>> billRows = jdbcClient.sql("""
                    SELECT id, name, extra_charges, rounding, created_at, creator_id, public_access_token
                    FROM bills
                    WHERE id = :billId
                """)
                .param("billId", billId)
                .query()
                .listOfRows();

        if (billRows.isEmpty()) {
            return Optional.empty();
        }

        Map<String, Object> billRow = billRows.get(0);

        int id = (int) billRow.get("id");

        Receipt receipt = new Receipt();
        receipt.setName((String) billRow.get("name"));
        receipt.setAdditionalChargesPercent(((Number) billRow.get("extra_charges")).intValue());
        receipt.setRoundingAdjustment(((Number) billRow.get("rounding")).intValue());
        receipt.setNow(((Timestamp) billRow.get("created_at")).toLocalDateTime());

        List<Map<String, Object>> itemRows = jdbcClient.sql("""
                    SELECT id, item_name, quantity, price
                    FROM bill_items
                    WHERE bill_id = :billId
                """)
                .param("billId", billId)
                .query()
                .listOfRows();

        List<ReceiptItemSplit> itemSplits = new ArrayList<>();

        for (Map<String, Object> itemRow : itemRows) {
            int billItemId = (int) itemRow.get("id");

            ReceiptItemSplit itemSplit = new ReceiptItemSplit();
            itemSplit.setItemName((String) itemRow.get("item_name"));
            itemSplit.setQuantity(((Number) itemRow.get("quantity")).intValue());
            itemSplit.setTotalPrice(((Number) itemRow.get("price")).intValue());

            List<Map<String, Object>> shareRows = jdbcClient.sql("""
                        SELECT sp.account_id, sp.guest_name, ish.quantity_share
                        FROM item_shares ish
                        JOIN split_participants sp ON ish.participant_id = sp.id
                        WHERE ish.bill_item_id = :billItemId
                    """)
                    .param("billItemId", billItemId)
                    .query()
                    .listOfRows();

            List<FriendSplit> friendSplits = new ArrayList<>();

            for (Map<String, Object> shareRow : shareRows) {
                Friend friend = new Friend();
                friend.setId((String) shareRow.get("account_id"));
                friend.setUsername((String) shareRow.get("guest_name"));

                FriendSplit split = new FriendSplit();
                split.setFriend(friend);
                split.setQuantity(((Number) shareRow.get("quantity_share")).intValue());

                friendSplits.add(split);
            }

            itemSplit.setFriendSplits(friendSplits);
            itemSplits.add(itemSplit);
        }

        receipt.setSplits(itemSplits);

        List<Map<String, Object>> memberRows = jdbcClient.sql("""
                    SELECT sp.id AS participant_id, sp.account_id, sp.guest_name, sp.is_paid, sp.payment_proof_link,
                           COALESCE(SUM(ish.quantity_share * bi.price / bi.quantity), 0) AS total_debt
                    FROM split_participants sp
                    LEFT JOIN item_shares ish ON ish.participant_id = sp.id
                    LEFT JOIN bill_items bi ON ish.bill_item_id = bi.id
                    WHERE sp.bill_id = :billId
                    GROUP BY sp.id, sp.account_id, sp.guest_name, sp.is_paid, sp.payment_proof_link
                """)
                .param("billId", billId)
                .query()
                .listOfRows();

        List<FriendPayment> members = new ArrayList<>();

        for (Map<String, Object> row : memberRows) {
            Friend friend = new Friend();
            friend.setId((String) row.get("account_id"));
            friend.setUsername((String) row.get("guest_name"));

            FriendPayment payment = new FriendPayment();
            payment.setFriend(friend);

            int extraCharges = ((Number) billRow.get("extra_charges")).intValue();

            double baseDebt = ((Number) row.get("total_debt")).doubleValue();
            double finalDebt = baseDebt * (1 + (extraCharges / 100.0));

            payment.setTotalDebt((int) Math.round(finalDebt));
            payment.setHasPaid((Boolean) row.get("is_paid"));
            payment.setPaymentImageLink((String) row.get("payment_proof_link"));

            members.add(payment);
        }

        ReceiptWithId receiptWithId = new ReceiptWithId();
        receiptWithId.setId(id);
        receiptWithId.setReceipt(receipt);
        receiptWithId.setCreatorId((String) billRow.get("creator_id"));
        receiptWithId.setMembers(members);
        receiptWithId.setPublicAccessToken((String) billRow.get("public_access_token"));

        return Optional.of(receiptWithId);
    }

    @Override
    public List<ReceiptWithId> findReceiptsByPayerId(String payerId) {
        List<Map<String, Object>> billRows = jdbcClient.sql("""
                    SELECT id, name, extra_charges, rounding, created_at, creator_id, public_access_token
                    FROM bills
                    WHERE creator_id = :creatorId
                """)
                .param("creatorId", payerId)
                .query()
                .listOfRows();

        List<ReceiptWithId> receipts = new ArrayList<>();

        for (Map<String, Object> billRow : billRows) {
            int billId = (int) billRow.get("id");

            Receipt receipt = new Receipt();
            receipt.setName((String) billRow.get("name"));
            receipt.setAdditionalChargesPercent(((Number) billRow.get("extra_charges")).intValue());
            receipt.setRoundingAdjustment(((Number) billRow.get("rounding")).intValue());
            receipt.setNow(((Timestamp) billRow.get("created_at")).toLocalDateTime());

            List<Map<String, Object>> itemRows = jdbcClient.sql("""
                        SELECT id, item_name, quantity, price
                        FROM bill_items
                        WHERE bill_id = :billId
                    """)
                    .param("billId", billId)
                    .query()
                    .listOfRows();

            List<ReceiptItemSplit> itemSplits = new ArrayList<>();

            for (Map<String, Object> itemRow : itemRows) {
                int billItemId = (int) itemRow.get("id");

                ReceiptItemSplit itemSplit = new ReceiptItemSplit();
                itemSplit.setItemName((String) itemRow.get("item_name"));
                itemSplit.setQuantity(((Number) itemRow.get("quantity")).intValue());
                itemSplit.setTotalPrice(((Number) itemRow.get("price")).intValue());

                List<Map<String, Object>> shareRows = jdbcClient.sql("""
                            SELECT sp.account_id, sp.guest_name, ish.quantity_share
                            FROM item_shares ish
                            JOIN split_participants sp ON ish.participant_id = sp.id
                            WHERE ish.bill_item_id = :billItemId
                        """)
                        .param("billItemId", billItemId)
                        .query()
                        .listOfRows();

                List<FriendSplit> friendSplits = new ArrayList<>();

                for (Map<String, Object> shareRow : shareRows) {
                    Friend friend = new Friend();
                    friend.setId((String) shareRow.get("account_id"));
                    friend.setUsername((String) shareRow.get("guest_name"));

                    FriendSplit split = new FriendSplit();
                    split.setFriend(friend);
                    split.setQuantity(((Number) shareRow.get("quantity_share")).intValue());

                    friendSplits.add(split);
                }

                itemSplit.setFriendSplits(friendSplits);
                itemSplits.add(itemSplit);
            }

            receipt.setSplits(itemSplits);

            List<Map<String, Object>> memberRows = jdbcClient.sql("""
                    SELECT sp.id AS participant_id,
                           sp.account_id,
                           sp.guest_name,
                           sp.is_paid,
                           sp.payment_proof_link,
                           sp.paid_at,
                           COALESCE(SUM(ish.quantity_share * bi.price / bi.quantity), 0) AS total_debt
                    FROM split_participants sp
                    LEFT JOIN item_shares ish ON ish.participant_id = sp.id
                    LEFT JOIN bill_items bi ON ish.bill_item_id = bi.id
                    WHERE sp.bill_id = :billId
                    GROUP BY sp.id, sp.account_id, sp.guest_name, sp.is_paid, sp.payment_proof_link, sp.paid_at
                    """)
                    .param("billId", billId)
                    .query()
                    .listOfRows();

            List<FriendPayment> members = new ArrayList<>();

            for (Map<String, Object> memberRow : memberRows) {
                Friend friend = new Friend();
                friend.setId((String) memberRow.get("account_id"));
                friend.setUsername((String) memberRow.get("guest_name"));

                FriendPayment payment = new FriendPayment();
                payment.setFriend(friend);
                payment.setHasPaid((Boolean) memberRow.get("is_paid"));
                payment.setPaymentImageLink((String) memberRow.get("payment_proof_link"));
                payment.setPaidAt(((Timestamp) memberRow.get("paid_at")) != null
                        ? ((Timestamp) memberRow.get("paid_at")).toLocalDateTime()
                        : null);

                int extraCharges = ((Number) billRow.get("extra_charges")).intValue();
                int baseDebt = ((Number) memberRow.get("total_debt")).intValue();

                int taxedDebt = (baseDebt * (100 + extraCharges)) / 100;

                payment.setTotalDebt(taxedDebt);

                members.add(payment);
            }

            ReceiptWithId receiptWithId = new ReceiptWithId();
            receiptWithId.setId(billId);
            receiptWithId.setCreatorId((String) billRow.get("creator_id"));
            receiptWithId.setReceipt(receipt);
            receiptWithId.setMembers(members);
            receiptWithId.setPublicAccessToken((String) billRow.get("public_access_token"));

            receipts.add(receiptWithId);
        }

        return receipts;
    }

    @Override
    public void deleteBill(int billId) {
        int deleted = jdbcClient.sql("""
                    DELETE FROM bills
                    WHERE id = :billId
                """)
                .param("billId", billId)
                .update();

        if (deleted != 1) {
            throw new IllegalStateException("Bill is not deleted: " + billId);
        }
    }

    @Override
    public List<ReceiptWithId> findReceiptsWhereUserIsParticipant(String accountId) {
        List<Map<String, Object>> billRows = jdbcClient
                .sql("""
                            SELECT DISTINCT b.id, b.name, b.extra_charges, b.rounding, b.created_at, b.creator_id, b.public_access_token
                            FROM bills b
                            JOIN split_participants sp ON b.id = sp.bill_id
                            WHERE sp.account_id = :accountId AND b.creator_id != :accountId
                        """)
                .param("accountId", accountId)
                .query()
                .listOfRows();

        List<ReceiptWithId> receipts = new ArrayList<>();

        for (Map<String, Object> billRow : billRows) {
            int billId = (int) billRow.get("id");

            Receipt receipt = new Receipt();
            receipt.setName((String) billRow.get("name"));
            receipt.setAdditionalChargesPercent(((Number) billRow.get("extra_charges")).intValue());
            receipt.setRoundingAdjustment(((Number) billRow.get("rounding")).intValue());
            receipt.setNow(((Timestamp) billRow.get("created_at")).toLocalDateTime());

            List<Map<String, Object>> itemRows = jdbcClient.sql("""
                        SELECT id, item_name, quantity, price
                        FROM bill_items
                        WHERE bill_id = :billId
                    """)
                    .param("billId", billId)
                    .query()
                    .listOfRows();

            List<ReceiptItemSplit> itemSplits = new ArrayList<>();

            for (Map<String, Object> itemRow : itemRows) {
                int billItemId = (int) itemRow.get("id");

                ReceiptItemSplit itemSplit = new ReceiptItemSplit();
                itemSplit.setItemName((String) itemRow.get("item_name"));
                itemSplit.setQuantity(((Number) itemRow.get("quantity")).intValue());
                itemSplit.setTotalPrice(((Number) itemRow.get("price")).intValue());

                List<Map<String, Object>> shareRows = jdbcClient.sql("""
                            SELECT sp.account_id, sp.guest_name, ish.quantity_share
                            FROM item_shares ish
                            JOIN split_participants sp ON ish.participant_id = sp.id
                            WHERE ish.bill_item_id = :billItemId
                        """)
                        .param("billItemId", billItemId)
                        .query()
                        .listOfRows();

                List<FriendSplit> friendSplits = new ArrayList<>();
                for (Map<String, Object> shareRow : shareRows) {
                    Friend friend = new Friend();
                    friend.setId((String) shareRow.get("account_id"));
                    friend.setUsername((String) shareRow.get("guest_name"));

                    FriendSplit split = new FriendSplit();
                    split.setFriend(friend);
                    split.setQuantity(((Number) shareRow.get("quantity_share")).intValue());

                    friendSplits.add(split);
                }

                itemSplit.setFriendSplits(friendSplits);
                itemSplits.add(itemSplit);
            }

            receipt.setSplits(itemSplits);

            List<Map<String, Object>> memberRows = jdbcClient.sql("""
                        SELECT sp.id AS participant_id,
                               sp.account_id,
                               sp.guest_name,
                               sp.is_paid,
                               sp.payment_proof_link,
                               sp.paid_at,
                               COALESCE(SUM(ish.quantity_share * bi.price / bi.quantity), 0) AS total_debt
                        FROM split_participants sp
                        LEFT JOIN item_shares ish ON ish.participant_id = sp.id
                        LEFT JOIN bill_items bi ON ish.bill_item_id = bi.id
                        WHERE sp.bill_id = :billId
                        GROUP BY sp.id, sp.account_id, sp.guest_name, sp.is_paid, sp.payment_proof_link, sp.paid_at
                    """)
                    .param("billId", billId)
                    .query()
                    .listOfRows();

            List<FriendPayment> members = new ArrayList<>();
            for (Map<String, Object> memberRow : memberRows) {
                Friend friend = new Friend();
                friend.setId((String) memberRow.get("account_id"));
                friend.setUsername((String) memberRow.get("guest_name"));

                FriendPayment payment = new FriendPayment();
                payment.setFriend(friend);
                payment.setHasPaid((Boolean) memberRow.get("is_paid"));
                payment.setPaymentImageLink((String) memberRow.get("payment_proof_link"));
                payment.setPaidAt(memberRow.get("paid_at") != null
                        ? ((Timestamp) memberRow.get("paid_at")).toLocalDateTime()
                        : null);

                int extraCharges = ((Number) billRow.get("extra_charges")).intValue();
                int baseDebt = ((Number) memberRow.get("total_debt")).intValue();
                int taxedDebt = (baseDebt * (100 + extraCharges)) / 100;

                payment.setTotalDebt(taxedDebt);

                members.add(payment);
            }

            ReceiptWithId receiptWithId = new ReceiptWithId();
            receiptWithId.setId(billId);
            receiptWithId.setCreatorId((String) billRow.get("creator_id"));
            receiptWithId.setReceipt(receipt);
            receiptWithId.setMembers(members);
            receiptWithId.setPublicAccessToken((String) billRow.get("public_access_token"));

            receipts.add(receiptWithId);
        }

        return receipts;

    }

    @Override
    public void attachPayment(int billId, String payerId, String paymentImageLink) {
        StringBuilder sql = new StringBuilder("""
                    UPDATE split_participants
                    SET is_paid = true,
                        paid_at = NOW()
                """);

        Map<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        params.put("payerId", payerId);

        if (paymentImageLink != null) {
            sql.append(", payment_proof_link = :paymentImageLink");
            params.put("paymentImageLink", paymentImageLink);
        }

        sql.append("""
                    WHERE bill_id = :billId
                    AND account_id = :payerId
                """);

        int rowsAffected = jdbcClient
                .sql(sql.toString())
                .params(params)
                .update();

        if (rowsAffected == 0) {
            throw new IllegalArgumentException(
                    "No matching participant found for bill ID " + billId + " and account ID " + payerId);
        }
    }


    @Override
    public void attachPaymentGuest(int billId, String guestName, String paymentImageLink) {
        StringBuilder sql = new StringBuilder("""
                    UPDATE split_participants
                    SET is_paid = true,
                        paid_at = NOW()
                """);

        Map<String, Object> params = new HashMap<>();
        params.put("billId", billId);
        params.put("guestName", guestName);

        if (paymentImageLink != null) {
            sql.append(", payment_proof_link = :paymentImageLink");
            params.put("paymentImageLink", paymentImageLink);
        }

        sql.append("""
                WHERE bill_id = :billId
                AND guest_name = :guestName
            """);

        int rowsAffected = jdbcClient
                .sql(sql.toString())
                .params(params)
                .update();

        if (rowsAffected == 0) {
            throw new IllegalArgumentException(
                    "No matching guest participant found for bill ID " + billId + " and guest name " + guestName);
        }
    }

}
