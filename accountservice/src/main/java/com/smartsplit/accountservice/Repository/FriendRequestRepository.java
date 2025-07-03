package com.smartsplit.accountservice.Repository;

import java.util.Map;

import com.smartsplit.accountservice.DO.AccountDO;

public interface FriendRequestRepository {
    public void addFriendRequest(String initiatorId, String targetId);

    public void acceptFriendRequest(String id);

    public void rejectFriendRequest(String id);

    public Map<Integer, AccountDO> findPendingFriendRequestToByUserId(String userId);
}
