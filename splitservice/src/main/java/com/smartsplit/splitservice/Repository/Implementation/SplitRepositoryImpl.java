package com.smartsplit.splitservice.Repository.Implementation;

import com.smartsplit.splitservice.Model.Friend;
import com.smartsplit.splitservice.Model.FriendSplit;
import com.smartsplit.splitservice.Model.Receipt;
import com.smartsplit.splitservice.Model.ReceiptItemSplit;
import com.smartsplit.splitservice.Repository.SplitRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Integer billId = jdbcClient.sql("""
                    INSERT INTO bills (name, creator_id, extra_charges, rounding, created_at)
                    VALUES (:name, :creatorId, :extraCharges, :rounding, :createdAt)
                    RETURNING id
                """)
                .param("name", receipt.getName())
                .param("creatorId", payerId)
                .param("extraCharges", receipt.getAdditionalChargesPercent())
                .param("rounding", receipt.getRoundingAdjustment())
                .param("createdAt", receipt.getNow())
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

                    participantId = jdbcClient.sql("""
                                INSERT INTO split_participants (bill_id, account_id, guest_name)
                                VALUES (:billId, :accountId, :guestName)
                                RETURNING id
                            """)
                            .param("billId", billId)
                            .param("accountId", accountId)
                            .param("guestName", guestName)
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
    public List<Receipt> findReceiptsById(String payerId) {
        List<Map<String, Object>> billRows = jdbcClient.sql("""
                    SELECT id, name, extra_charges, rounding, created_at
                    FROM bills
                    WHERE creator_id = :creatorId
                """)
                .param("creatorId", payerId)
                .query()
                .listOfRows();

        List<Receipt> receipts = new ArrayList<>();

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
            receipts.add(receipt);
        }

        return receipts;
    }

}
