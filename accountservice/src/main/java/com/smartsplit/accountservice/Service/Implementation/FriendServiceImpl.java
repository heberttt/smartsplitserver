package com.smartsplit.accountservice.Service.Implementation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.smartsplit.accountservice.DO.AccountDO;
import com.smartsplit.accountservice.Repository.FriendRepository;
import com.smartsplit.accountservice.Repository.FriendRequestRepository;
import com.smartsplit.accountservice.Request.AddFriendRequest;
import com.smartsplit.accountservice.Request.RemoveFriendRequest;
import com.smartsplit.accountservice.Result.AddFriendResult;
import com.smartsplit.accountservice.Result.GetFriendResult;
import com.smartsplit.accountservice.Result.RemoveFriendResult;
import com.smartsplit.accountservice.Service.FriendService;

@Service
public class FriendServiceImpl implements FriendService {

    private final FriendRepository friendRepository;

    private final FriendRequestRepository friendRequestRepository;

    public FriendServiceImpl(FriendRepository friendRepository, FriendRequestRepository friendRequestRepository) {
        this.friendRepository = friendRepository;
        this.friendRequestRepository = friendRequestRepository;
    }

    @Override
    public GetFriendResult getFriends(Jwt jwt) {
        String id = jwt.getClaimAsString("user_id");

        GetFriendResult result = new GetFriendResult();

        try {
            List<AccountDO> friends = friendRepository.findMyFriends(id);

            result.setSuccess(true);
            result.setStatusCode(200);
            result.setData(friends);

            return result;

        } catch (Exception e) {
            result.setSuccess(false);
            result.setStatusCode(500);
            result.setErrorMessage(e.toString());

            return result;
        }
    }

    @Override
    public AddFriendResult addFriend(AddFriendRequest request, Jwt jwt) {
        AddFriendResult result = new AddFriendResult();

        String id = jwt.getClaimAsString("user_id");


        try {
            Map<Integer, AccountDO> pendingRequests = friendRequestRepository
                    .findPendingFriendRequestToByUserId(id);

            Optional<Map.Entry<Integer, AccountDO>> matchedEntry = pendingRequests.entrySet().stream()
                    .filter(entry -> entry.getKey() == request.getTargetId())
                    .findFirst();

            if (matchedEntry.isEmpty()) {
                throw new Exception("No pending request from user: " + request.getTargetId());
            }

            Integer friendRequestId = matchedEntry.get().getKey();
            AccountDO targetAccount = matchedEntry.get().getValue();

            if (targetAccount == null || friendRequestId == null) {
                throw new Exception("There is no pending friend request by this user");
            }

            if (friendRepository.findMyFriends(targetAccount.getId()).stream()
                    .anyMatch(account -> account.getId().equals(targetAccount.getId()))) {
                throw new Exception("Friend is already added");
            }

            friendRepository.addFriend(id, targetAccount.getId());

            friendRequestRepository.acceptFriendRequest(friendRequestId);

            result.setSuccess(true);
            result.setStatusCode(200);

            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.toString());
            result.setStatusCode(500);

            return result;
        }
    }

    @Override
    public RemoveFriendResult removeFriend(RemoveFriendRequest request, Jwt jwt) {
        RemoveFriendResult result = new RemoveFriendResult();

        String id = jwt.getClaimAsString("user_id");

        try{
            friendRepository.removeFriend(id , request.getFriendId());

            result.setSuccess(true);
            result.setStatusCode(200);
            return result;
        }catch(Exception e){
            result.setSuccess(false);
            result.setStatusCode(500);
            result.setErrorMessage(e.toString());
            return result;
        }
    }

}
