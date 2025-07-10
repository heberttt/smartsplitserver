package com.smartsplit.accountservice.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;

import com.smartsplit.accountservice.Request.CreateGroupRequest;
import com.smartsplit.accountservice.Request.DeleteGroupRequest;
import com.smartsplit.accountservice.Request.InviteFriendRequest;
import com.smartsplit.accountservice.Result.CreateGroupResult;
import com.smartsplit.accountservice.Result.DeleteGroupResult;
import com.smartsplit.accountservice.Result.GetMyGroupsResult;
import com.smartsplit.accountservice.Result.InviteFriendResult;


public interface GroupController{
    public ResponseEntity<CreateGroupResult> createGroup(CreateGroupRequest request, Jwt jwt);

    public ResponseEntity<GetMyGroupsResult> getMyGroups(Jwt jwt);

    public ResponseEntity<InviteFriendResult> inviteFriend(InviteFriendRequest request, Jwt jwt);

    public ResponseEntity<DeleteGroupResult> deleteGroup(DeleteGroupRequest request, Jwt jwt);
}