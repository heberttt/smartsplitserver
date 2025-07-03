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

import com.smartsplit.accountservice.Controller.FriendRequestController;
import com.smartsplit.accountservice.Request.CreateFriendRequestRequest;
import com.smartsplit.accountservice.Request.RejectFriendRequestRequest;
import com.smartsplit.accountservice.Result.CreateFriendRequestResult;
import com.smartsplit.accountservice.Result.GetMyPendingFriendRequestResult;
import com.smartsplit.accountservice.Result.RejectFriendRequestResult;
import com.smartsplit.accountservice.Service.FriendRequestService;

@Controller
@RequestMapping("/api/friendRequest")
public class FriendRequestControllerImpl implements FriendRequestController{

    final FriendRequestService friendRequestService;

    public FriendRequestControllerImpl(FriendRequestService friendRequestService){
        this.friendRequestService = friendRequestService;
    }

    @GetMapping()
    public ResponseEntity<GetMyPendingFriendRequestResult> getMyPendingFriendRequestResult(@AuthenticationPrincipal Jwt jwt) {
        final GetMyPendingFriendRequestResult result = friendRequestService.getMyPendingFriendRequest(jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping()
    public ResponseEntity<CreateFriendRequestResult> createFriendRequest(@RequestBody CreateFriendRequestRequest request, @AuthenticationPrincipal Jwt jwt) {
        final CreateFriendRequestResult result = friendRequestService.createFriendRequest(request, jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping()
    public ResponseEntity<RejectFriendRequestResult> rejectFriendRequest(@RequestBody RejectFriendRequestRequest request, @AuthenticationPrincipal Jwt jwt){
        final RejectFriendRequestResult result = friendRequestService.rejectFriendRequest(request, jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
    
}
