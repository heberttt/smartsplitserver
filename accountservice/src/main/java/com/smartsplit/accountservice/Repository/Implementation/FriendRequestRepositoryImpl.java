package com.smartsplit.accountservice.Repository.Implementation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.smartsplit.accountservice.DO.AccountDO;
import com.smartsplit.accountservice.Enum.FriendRequestStatusEnum;
import com.smartsplit.accountservice.Repository.FriendRequestRepository;

@Repository
public class FriendRequestRepositoryImpl implements FriendRequestRepository {

    private final JdbcClient jdbcClient;

    public FriendRequestRepositoryImpl(JdbcClient client) {
        this.jdbcClient = client;
    }

    @Override
    public void addFriendRequest(String initiatorId, String targetId) {
        var updated = jdbcClient.sql("INSERT INTO friend_request(initiator_id, target_id, status) values(?, ?, ?, ?)")
                .params(List.of(initiatorId, targetId, FriendRequestStatusEnum.PENDING.toString()))
                .update();

        Assert.state(updated == 1, "Failed to create friend request: " + initiatorId + "->" + targetId);
    }

    @Override
    public void acceptFriendRequest(String id) {
        int updated = jdbcClient.sql("UPDATE friend_request SET status = ? WHERE id = ?")
                .params(List.of(FriendRequestStatusEnum.ACCEPTED.toString(), id))
                .update();

        Assert.state(updated == 1, "Row updated: " + updated);
    }

    @Override
    public void rejectFriendRequest(String id) {
        int updated = jdbcClient.sql("UPDATE friend_request SET status = ? WHERE id = ?")
                .params(List.of(FriendRequestStatusEnum.REJECTED.toString(), id))
                .update();

        Assert.state(updated == 1, "Row updated: " + updated);
    }

    @Override
    public Map<Integer, AccountDO> findPendingFriendRequestToByUserId(String userId) {
        String sql = """
                SELECT
                  fr.id AS request_id,
                  a.id AS initiator_id,
                  a.username,
                  a.email,
                  a.profilePictureLink
                FROM friendship_request fr
                JOIN accounts a ON fr.initiator_id = a.id
                WHERE fr.status = 'PENDING'
                  AND fr.target_id = :targetId
                                            """;

        return jdbcClient.sql(sql)
            .param("targetId", userId)
            .query((rs, rowNum) -> {
                int requestId = rs.getInt("request_id");

                AccountDO account = new AccountDO();
                account.setId(rs.getString("initiator_id"));
                account.setUsername(rs.getString("username"));
                account.setEmail(rs.getString("email"));
                account.setProfilePictureLink(rs.getString("profilePictureLink"));

                return Map.entry(requestId, account);
            })
            .list()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

}
