package com.smartsplit.accountservice.Service;

import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.accountservice.Request.CreateGroupRequest;
import com.smartsplit.accountservice.Request.DeleteGroupRequest;
import com.smartsplit.accountservice.Request.InviteFriendRequest;
import com.smartsplit.accountservice.Result.CreateGroupResult;
import com.smartsplit.accountservice.Result.DeleteGroupResult;
import com.smartsplit.accountservice.Result.GetMyGroupsResult;
import com.smartsplit.accountservice.Result.InviteFriendResult;

public interface GroupService {
    public CreateGroupResult createGroup(CreateGroupRequest request, Jwt jwt);

    public GetMyGroupsResult getMyGroups(Jwt jwt);

    public InviteFriendResult inviteFriend(InviteFriendRequest request, Jwt jwt);

    public DeleteGroupResult deleteGroup(DeleteGroupRequest request, Jwt jwt);
}
