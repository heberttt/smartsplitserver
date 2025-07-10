package com.smartsplit.accountservice.Repository.Implementation;

import java.util.List;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import com.smartsplit.accountservice.DO.AccountDO;
import com.smartsplit.accountservice.Repository.FriendRepository;

@Repository
public class FriendRepositoryImpl implements FriendRepository {

    final JdbcClient jdbcClient;

    public FriendRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<AccountDO> findMyFriends(String id) {
        String sql = """
                    SELECT a.id, a.username, a.email, a.profilePictureLink
                    FROM friendships f
                    JOIN accounts a ON
                      (f.account1_id = :id AND a.id = f.account2_id)
                      OR
                      (f.account2_id = :id AND a.id = f.account1_id)
                """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query((rs, rowNum) -> {
                    AccountDO account = new AccountDO();
                    account.setId(rs.getString("id"));
                    account.setUsername(rs.getString("username"));
                    account.setEmail(rs.getString("email"));
                    account.setProfilePictureLink(rs.getString("profilePictureLink"));
                    return account;
                })
                .list();
    }

    @Override
    public void addFriend(String friend1id, String friend2id) {
        if (friend1id.equals(friend2id)) {
            throw new IllegalArgumentException("Cannot friend yourself");
        }

        String account1, account2;
        if (friend1id.toLowerCase().compareTo(friend2id.toLowerCase()) < 0) {
            account1 = friend1id;
            account2 = friend2id;
        } else {
            account1 = friend2id;
            account2 = friend1id;
        }
        System.out.println("account 1: " + account1);
        System.out.println("account 2: " + account2);

        int updated = jdbcClient.sql("""
                    INSERT INTO friendships (account1_id, account2_id)
                    VALUES (:account1, :account2)
                """)
                .param("account1", account1)
                .param("account2", account2)
                .update();

        if (updated != 1) {
            throw new IllegalStateException("Failed to insert friendship: " + friend1id + " <-> " + friend2id);
        }
    }

    @Override
    public void removeFriend(String friend1id, String friend2id) {
        if (friend1id.equals(friend2id)) {
            throw new IllegalArgumentException("Cannot remove friendship with yourself");
        }

        String account1, account2;
        if (friend1id.toLowerCase().compareTo(friend2id.toLowerCase()) < 0) {
            account1 = friend1id;
            account2 = friend2id;
        } else {
            account1 = friend2id;
            account2 = friend1id;
        }

        int deleted = jdbcClient.sql("""
                    DELETE FROM friendships
                    WHERE account1_id = :account1 AND account2_id = :account2
                """)
                .param("account1", account1)
                .param("account2", account2)
                .update();

        if (deleted != 1) {
            throw new IllegalStateException("No friendship found between: " + friend1id + " and " + friend2id);
        }
    }

    @Override
    public boolean checkIfFriend(String friend1id, String friend2id) {
        if (friend1id.equals(friend2id)) {
            return false;
        }

        String account1, account2;
        if (friend1id.toLowerCase().compareTo(friend2id.toLowerCase()) < 0) {
            account1 = friend1id;
            account2 = friend2id;
        } else {
            account1 = friend2id;
            account2 = friend1id;
        }

        List<Integer> results = jdbcClient.sql("""
                    SELECT COUNT(*) AS count
                    FROM friendships
                    WHERE account1_id = :account1 AND account2_id = :account2
                """)
                .param("account1", account1)
                .param("account2", account2)
                .query((rs, rowNum) -> rs.getInt("count"))
                .list();

        return !results.isEmpty() && results.get(0) > 0;
    }

}
