package com.smartsplit.accountservice.Repository;

import java.util.List;

import com.smartsplit.accountservice.DO.AccountDO;

public interface FriendRepository {

    public List<AccountDO> findMyFriends(String id);

    public void addFriend(String friend1ID, String friend2ID);

    public void removeFriend(String friend1ID, String friend2ID);
}
