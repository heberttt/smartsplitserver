package com.smartsplit.accountservice.Repository.Implementation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.smartsplit.accountservice.DO.AccountDO;
import com.smartsplit.accountservice.DO.FriendRequestDO;
import com.smartsplit.accountservice.Enum.FriendRequestStatusEnum;
import com.smartsplit.accountservice.Repository.FriendRequestRepository;

@Repository
public class FriendRequestRepositoryImpl implements FriendRequestRepository {

    private final JdbcClient jdbcClient;

    public FriendRequestRepositoryImpl(JdbcClient client) {
        this.jdbcClient = client;
    }

    @Override
    public void createFriendRequest(String initiatorId, String targetId) {

        System.out.println("initiatorId = " + initiatorId);
        System.out.println("targetId = " + targetId);
        System.out.println("status = " + FriendRequestStatusEnum.PENDING);

        var updated = jdbcClient.sql("INSERT INTO friendship_request(initiator_id, target_id, status) values(?, ?, ?)")
                .params(List.of(initiatorId, targetId, FriendRequestStatusEnum.PENDING.toString()))
                .update();

        Assert.state(updated == 1, "Failed to create friend request: " + initiatorId + "->" + targetId);
    }

    @Override
    public void acceptFriendRequest(int id) {
        int updated = jdbcClient.sql("UPDATE friendship_request SET status = ? WHERE id = ?")
                .params(List.of(FriendRequestStatusEnum.ACCEPTED.toString(), id))
                .update();

        Assert.state(updated == 1, "Row updated: " + updated);
    }

    @Override
    public void rejectFriendRequest(int id) {
        int updated = jdbcClient.sql("UPDATE friendship_request SET status = ? WHERE id = ?")
                .params(List.of(FriendRequestStatusEnum.REJECTED.toString(), id))
                .update();

        String updateErrorMessage = "";

        if (updated == 0){
            updateErrorMessage = "Friend request does not exist";
        }
        else if (updated == 1){
            
        }else{
            updateErrorMessage = "More than one row updated: " + updated;
        }

        Assert.state(updated == 1, updateErrorMessage);
    }

    @Override
    public Optional<FriendRequestDO> findFriendRequestById(String id) {
        return jdbcClient.sql("SELECT id, initiator_id, target_id, status FROM accounts WHERE id = :id")
                .param("id", id)
                .query(FriendRequestDO.class)
                .optional();
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
