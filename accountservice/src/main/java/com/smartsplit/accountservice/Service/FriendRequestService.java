package com.smartsplit.accountservice.Service;

import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.accountservice.Result.GetMyPendingFriendRequestResult;

public interface FriendRequestService {
    public GetMyPendingFriendRequestResult getMyPendingFriendRequest(Jwt jwt);
}
