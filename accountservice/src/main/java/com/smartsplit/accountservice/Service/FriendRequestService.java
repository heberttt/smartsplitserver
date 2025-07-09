package com.smartsplit.accountservice.Service;

import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.accountservice.Request.CreateFriendRequestByEmailRequest;
import com.smartsplit.accountservice.Request.CreateFriendRequestRequest;
import com.smartsplit.accountservice.Request.RejectFriendRequestRequest;
import com.smartsplit.accountservice.Result.CreateFriendRequestResult;
import com.smartsplit.accountservice.Result.GetMyPendingFriendRequestResult;
import com.smartsplit.accountservice.Result.RejectFriendRequestResult;

public interface FriendRequestService {
    public GetMyPendingFriendRequestResult getMyPendingFriendRequest(Jwt jwt);

    public CreateFriendRequestResult createFriendRequest(CreateFriendRequestRequest request, Jwt jwt);

    public RejectFriendRequestResult rejectFriendRequest(RejectFriendRequestRequest request ,Jwt jwt);

    public CreateFriendRequestResult createFriendRequestByEmail(CreateFriendRequestByEmailRequest request, Jwt jwt);
}
