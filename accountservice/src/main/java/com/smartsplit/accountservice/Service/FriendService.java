package com.smartsplit.accountservice.Service;

import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.accountservice.Request.AddFriendRequest;
import com.smartsplit.accountservice.Request.RemoveFriendRequest;
import com.smartsplit.accountservice.Result.AddFriendResult;
import com.smartsplit.accountservice.Result.GetFriendResult;
import com.smartsplit.accountservice.Result.RemoveFriendResult;

public interface FriendService {
    public GetFriendResult getFriends(Jwt jwt);
    
    public AddFriendResult addFriend(AddFriendRequest request, Jwt jwt);

    public RemoveFriendResult removeFriend(RemoveFriendRequest request, Jwt jwt);
}
