package com.smartsplit.accountservice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestBody;

import com.smartsplit.accountservice.Request.CreateFriendRequestRequest;
import com.smartsplit.accountservice.Request.RejectFriendRequestRequest;
import com.smartsplit.accountservice.Result.CreateFriendRequestResult;
import com.smartsplit.accountservice.Result.GetMyPendingFriendRequestResult;
import com.smartsplit.accountservice.Result.RejectFriendRequestResult;

public interface FriendRequestController {
    public ResponseEntity<GetMyPendingFriendRequestResult> getMyPendingFriendRequestResult(Jwt jwt);

    public ResponseEntity<CreateFriendRequestResult> createFriendRequest(CreateFriendRequestRequest request,Jwt jwt);

    public ResponseEntity<RejectFriendRequestResult> rejectFriendRequest(@RequestBody RejectFriendRequestRequest request, @AuthenticationPrincipal Jwt jwt);
}
