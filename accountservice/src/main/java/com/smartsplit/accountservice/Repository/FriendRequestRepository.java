package com.smartsplit.accountservice.Repository;

import java.util.Map;
import java.util.Optional;

import com.smartsplit.accountservice.DO.AccountDO;
import com.smartsplit.accountservice.DO.FriendRequestDO;

public interface FriendRequestRepository {
    public void createFriendRequest(String initiatorId, String targetId);

    public void acceptFriendRequest(int id);

    public void rejectFriendRequest(int id);

    public Map<Integer, AccountDO> findPendingFriendRequestToByUserId(String userId);
    
    public Optional<FriendRequestDO> findFriendRequestById(String id);
}
