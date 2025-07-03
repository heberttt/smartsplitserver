package com.smartsplit.accountservice.Service.Implementation;

import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.accountservice.Request.AddFriendRequest;
import com.smartsplit.accountservice.Request.RemoveFriendRequest;
import com.smartsplit.accountservice.Result.GetFriendResult;
import com.smartsplit.accountservice.Result.RemoveFriendResult;
import com.smartsplit.accountservice.Service.FriendService;

public class FriendServiceImpl implements FriendService {

    @Override
    public GetFriendResult getFriends(Jwt jwt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFriends'");
    }

    @Override
    public AddFriendRequest addFriend(AddFriendRequest request, Jwt jwt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addFriend'");
    }

    @Override
    public RemoveFriendResult removeFriend(RemoveFriendRequest request, Jwt jwt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFriend'");
    }
    
}
