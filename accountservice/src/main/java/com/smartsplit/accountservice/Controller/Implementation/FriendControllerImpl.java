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

import com.smartsplit.accountservice.Controller.FriendController;
import com.smartsplit.accountservice.Request.AddFriendRequest;
import com.smartsplit.accountservice.Request.RemoveFriendRequest;
import com.smartsplit.accountservice.Result.AddFriendResult;
import com.smartsplit.accountservice.Result.GetFriendResult;
import com.smartsplit.accountservice.Result.RemoveFriendResult;
import com.smartsplit.accountservice.Service.FriendService;

@Controller
@RequestMapping("/api/friends")
public class FriendControllerImpl implements FriendController{

    private final FriendService friendService;

    public FriendControllerImpl(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping()
    public ResponseEntity<GetFriendResult> getFriends(@AuthenticationPrincipal Jwt jwt) {
        final GetFriendResult result = friendService.getFriends(jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping()
    public ResponseEntity<RemoveFriendResult> removeFriend(@RequestBody RemoveFriendRequest request, @AuthenticationPrincipal Jwt jwt) {
        final RemoveFriendResult result = friendService.removeFriend(request, jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/addFriend")
    public ResponseEntity<AddFriendResult> acceptFriendRequest(@RequestBody AddFriendRequest request, @AuthenticationPrincipal Jwt jwt) {
        final AddFriendResult result = friendService.addFriend(request, jwt);

        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
    
}
