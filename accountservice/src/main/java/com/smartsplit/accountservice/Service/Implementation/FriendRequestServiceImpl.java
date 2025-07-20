package com.smartsplit.accountservice.Service.Implementation;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.smartsplit.accountservice.DO.AccountDO;
import com.smartsplit.accountservice.Repository.AccountRepository;
import com.smartsplit.accountservice.Repository.FriendRepository;
import com.smartsplit.accountservice.Repository.FriendRequestRepository;
import com.smartsplit.accountservice.Request.CreateFriendRequestByEmailRequest;
import com.smartsplit.accountservice.Request.CreateFriendRequestRequest;
import com.smartsplit.accountservice.Request.RejectFriendRequestRequest;
import com.smartsplit.accountservice.Result.CreateFriendRequestResult;
import com.smartsplit.accountservice.Result.GetMyPendingFriendRequestResult;
import com.smartsplit.accountservice.Result.RejectFriendRequestResult;
import com.smartsplit.accountservice.Service.FriendRequestService;

@Service
public class FriendRequestServiceImpl implements FriendRequestService{

    final private FriendRequestRepository friendRequestRepository;

    final private FriendRepository friendRepository;

    final private AccountRepository accountRepository;

    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository, FriendRepository friendRepository, AccountRepository accountRepository){
        this.friendRequestRepository = friendRequestRepository;
        this.friendRepository = friendRepository;
        this.accountRepository = accountRepository;
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
            result.setErrorMessage(e.getMessage());

            return result;
        }
    }

    @Override
    public CreateFriendRequestResult createFriendRequestByEmail(CreateFriendRequestByEmailRequest request, Jwt jwt) {
        CreateFriendRequestResult result = new CreateFriendRequestResult();

        String initiator_id = jwt.getClaimAsString("user_id");

        try{

            Optional<AccountDO> searchedAccount = accountRepository.findByEmail(request.getTargetEmail());

            if (searchedAccount == null){
                throw new Exception("Account with that email does not exist");
            }

            String targetId = searchedAccount.get().getId();

            if (initiator_id.equals(targetId)){
                throw new Exception("Unable to send a friend request to yourself");
            }

            if (friendRepository.findMyFriends(initiator_id).stream().anyMatch(friend -> friend.getId().equals(targetId))){
                throw new Exception("Unable to create friend request with existing friends");
            }

            Map<Integer, AccountDO> pendingRequests = friendRequestRepository.findPendingFriendRequestToByUserId(targetId);

            Optional<Map.Entry<Integer, AccountDO>> matchedEntry = pendingRequests.entrySet().stream()
                    .filter(entry -> entry.getValue().getId().equals(initiator_id))
                    .findFirst();
            
            if (matchedEntry.isPresent()){
                throw new Exception("Pending friend request already exists");
            }

            Map<Integer, AccountDO> oppositeRequest = friendRequestRepository.findPendingFriendRequestToByUserId(initiator_id);

            Optional<Map.Entry<Integer, AccountDO>> foundEntry = oppositeRequest.entrySet().stream()
                    .filter(entry -> entry.getValue().getId().equals(targetId))
                    .findFirst();
            
            if (foundEntry.isPresent()){
                throw new Exception("Target already sent a pending friend request");
            }



            friendRequestRepository.createFriendRequest(initiator_id, targetId);

            result.setSuccess(true);
            result.setStatusCode(200);
            
            return result;

        }catch(Exception e){
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setStatusCode(500);

            System.out.println(e.toString());
            
            return result;
        }
    }

    @Override
    public CreateFriendRequestResult createFriendRequest(CreateFriendRequestRequest request, Jwt jwt) {
        CreateFriendRequestResult result = new CreateFriendRequestResult();

        String initiator_id = jwt.getClaimAsString("user_id");

        try{

            if (initiator_id.equals(request.getTargetId())){
                throw new Exception("Unable to send a friend request to yourself");
            }

            if (friendRepository.findMyFriends(initiator_id).stream().anyMatch(friend -> friend.getId().equals(request.getTargetId()))){
                throw new Exception("Unable to create friend request with existing friends");
            }

            Map<Integer, AccountDO> pendingRequests = friendRequestRepository.findPendingFriendRequestToByUserId(request.getTargetId());

            Optional<Map.Entry<Integer, AccountDO>> matchedEntry = pendingRequests.entrySet().stream()
                    .filter(entry -> entry.getValue().getId().equals(initiator_id))
                    .findFirst();
            
            if (matchedEntry.isPresent()){
                throw new Exception("Pending friend request already exists");
            }

            Map<Integer, AccountDO> oppositeRequest = friendRequestRepository.findPendingFriendRequestToByUserId(initiator_id);

            Optional<Map.Entry<Integer, AccountDO>> foundEntry = oppositeRequest.entrySet().stream()
                    .filter(entry -> entry.getValue().getId().equals(request.getTargetId()))
                    .findFirst();
            
            if (foundEntry.isPresent()){
                throw new Exception("Target already sent a pending friend request");
            }



            friendRequestRepository.createFriendRequest(initiator_id, request.getTargetId());

            result.setSuccess(true);
            result.setStatusCode(200);
            
            return result;

        }catch(Exception e){
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setStatusCode(500);

            System.out.println(e.toString());
            
            return result;
        }
    }

    @Override
    public RejectFriendRequestResult rejectFriendRequest(RejectFriendRequestRequest request ,Jwt jwt){
        RejectFriendRequestResult result = new RejectFriendRequestResult();
        String initiator_id = jwt.getClaimAsString("user_id");

        try{

            Map<Integer, AccountDO> pendingRequests = friendRequestRepository.findPendingFriendRequestToByUserId(initiator_id);


            Optional<Map.Entry<Integer, AccountDO>> matchedEntry = pendingRequests.entrySet().stream()
                    .filter(entry -> entry.getKey() == request.getFriendRequestID())
                    .findFirst();

            if (matchedEntry.isEmpty()){
                throw new Exception("There is no pending friend request with given ID");
            }

            friendRequestRepository.rejectFriendRequest(request.getFriendRequestID());

            result.setSuccess(true);
            result.setStatusCode(200);
            return result;
        }catch(Exception e){
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setStatusCode(500);
            return result;
        }
    }
    
}
