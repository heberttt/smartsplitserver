package com.smartsplit.accountservice.Service.Implementation;

import java.util.Map;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.smartsplit.accountservice.DO.AccountDO;
import com.smartsplit.accountservice.Repository.FriendRequestRepository;
import com.smartsplit.accountservice.Result.GetMyPendingFriendRequestResult;
import com.smartsplit.accountservice.Service.FriendRequestService;

@Service
public class FriendRequestServiceImpl implements FriendRequestService{

    final FriendRequestRepository friendRequestRepository;

    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository){
        this.friendRequestRepository = friendRequestRepository;
    }

    @Override
    public GetMyPendingFriendRequestResult getMyPendingFriendRequest(Jwt jwt) {
        GetMyPendingFriendRequestResult result = new GetMyPendingFriendRequestResult();

        String id = jwt.getClaimAsString("user_id");

        try{
            final Map<Integer, AccountDO> friendRequests = friendRequestRepository.findPendingFriendRequestToByUserId(id);

            result.setData(friendRequests);
            result.setStatusCode(200);
            result.setSuccess(true);

            return result;
        }catch(Exception e){
            result.setSuccess(false);
            result.setStatusCode(500);
            result.setErrorMessage(e.toString());

            return result;
        }
    }
    
}
