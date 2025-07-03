package com.smartsplit.accountservice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.accountservice.Request.AddFriendRequest;
import com.smartsplit.accountservice.Request.RemoveFriendRequest;
import com.smartsplit.accountservice.Request.RequestFriendRequest;
import com.smartsplit.accountservice.Result.AddFriendResult;
import com.smartsplit.accountservice.Result.GetFriendResult;
import com.smartsplit.accountservice.Result.RemoveFriendResult;
import com.smartsplit.accountservice.Result.RequestFriendResult;

public interface FriendController{
    public ResponseEntity<GetFriendResult> getFriends(Jwt jwt);

    public ResponseEntity<RequestFriendResult> createFriendRequest(RequestFriendRequest request, Jwt jwt);

    public ResponseEntity<AddFriendResult> acceptFriendRequest(AddFriendRequest request, Jwt jwt);

    public ResponseEntity<RemoveFriendResult> removeFriend(RemoveFriendRequest request, Jwt jwt);
}