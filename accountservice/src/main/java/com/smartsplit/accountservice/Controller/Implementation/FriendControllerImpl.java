package com.smartsplit.accountservice.Controller.Implementation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smartsplit.accountservice.Controller.FriendController;
import com.smartsplit.accountservice.Request.AddFriendRequest;
import com.smartsplit.accountservice.Request.RemoveFriendRequest;
import com.smartsplit.accountservice.Request.RequestFriendRequest;
import com.smartsplit.accountservice.Result.AddFriendResult;
import com.smartsplit.accountservice.Result.GetFriendResult;
import com.smartsplit.accountservice.Result.RemoveFriendResult;
import com.smartsplit.accountservice.Result.RequestFriendResult;

@Controller
@RequestMapping("/api/friends")
public class FriendControllerImpl implements FriendController{

    @Override
    public ResponseEntity<GetFriendResult> getFriends(Jwt jwt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFriends'");
    }

    @Override
    public ResponseEntity<RemoveFriendResult> removeFriend(RemoveFriendRequest request, Jwt jwt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeFriend'");
    }

    @Override
    public ResponseEntity<RequestFriendResult> createFriendRequest(RequestFriendRequest request, Jwt jwt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createFriendRequest'");
    }

    @Override
    public ResponseEntity<AddFriendResult> acceptFriendRequest(AddFriendRequest request, Jwt jwt) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'acceptFriendRequest'");
    }
    
}
