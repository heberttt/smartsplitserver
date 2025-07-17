package com.smartsplit.accountservice.Controller.Implementation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.smartsplit.accountservice.Controller.GroupController;
import com.smartsplit.accountservice.Request.CreateGroupRequest;
import com.smartsplit.accountservice.Request.DeleteGroupRequest;
import com.smartsplit.accountservice.Request.InviteFriendRequest;
import com.smartsplit.accountservice.Request.LeaveGroupRequest;
import com.smartsplit.accountservice.Result.CreateGroupResult;
import com.smartsplit.accountservice.Result.DeleteGroupResult;
import com.smartsplit.accountservice.Result.GetMyGroupsResult;
import com.smartsplit.accountservice.Result.InviteFriendResult;
import com.smartsplit.accountservice.Result.LeaveGroupResult;
import com.smartsplit.accountservice.Service.GroupService;

@Controller
@RequestMapping("/api/groups")
public class GroupControllerImpl implements GroupController {

    private final GroupService groupService;

    public GroupControllerImpl(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("")
    public ResponseEntity<CreateGroupResult> createGroup(@RequestBody CreateGroupRequest request, @AuthenticationPrincipal Jwt jwt) {
        final CreateGroupResult result = groupService.createGroup(request, jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("")
    public ResponseEntity<GetMyGroupsResult> getMyGroups(@AuthenticationPrincipal Jwt jwt) {
        final GetMyGroupsResult result = groupService.getMyGroups(jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/members")
    public ResponseEntity<InviteFriendResult> inviteFriend(@RequestBody InviteFriendRequest request, @AuthenticationPrincipal Jwt jwt) {
        final InviteFriendResult result = groupService.inviteFriend(request, jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping("")
    public ResponseEntity<DeleteGroupResult> deleteGroup(@RequestBody DeleteGroupRequest request, @AuthenticationPrincipal Jwt jwt) {
        final DeleteGroupResult result = groupService.deleteGroup(request, jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping("/members")
    public ResponseEntity<LeaveGroupResult> leaveGroup(@RequestBody LeaveGroupRequest request, @AuthenticationPrincipal Jwt jwt){
        final LeaveGroupResult result = groupService.leaveGroup(request, jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
    
}
