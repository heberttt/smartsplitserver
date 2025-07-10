package com.smartsplit.accountservice.Service.Implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.smartsplit.accountservice.Request.CreateGroupRequest;
import com.smartsplit.accountservice.Request.DeleteGroupRequest;
import com.smartsplit.accountservice.Request.InviteFriendRequest;
import com.smartsplit.accountservice.Result.CreateGroupResult;
import com.smartsplit.accountservice.Result.DeleteGroupResult;
import com.smartsplit.accountservice.Result.GetMyGroupsResult;
import com.smartsplit.accountservice.Result.InviteFriendResult;
import com.smartsplit.accountservice.Service.GroupService;
import com.smartsplit.accountservice.DO.AccountDO;
import com.smartsplit.accountservice.DO.GroupDO;
import com.smartsplit.accountservice.Repository.AccountRepository;
import com.smartsplit.accountservice.Repository.FriendRepository;
import com.smartsplit.accountservice.Repository.GroupRepository;

@Service
public class GroupServiceImpl implements GroupService{

    private final GroupRepository groupRepository;
    private final FriendRepository friendRepository;
    private final AccountRepository accountRepository;

    public GroupServiceImpl(GroupRepository groupRepository, FriendRepository friendRepository,
            AccountRepository accountRepository) {
        this.groupRepository = groupRepository;
        this.friendRepository = friendRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public CreateGroupResult createGroup(CreateGroupRequest request, Jwt jwt) {
        String initiatorId = jwt.getClaimAsString("user_id");
        
        CreateGroupResult result = new CreateGroupResult();

        try{
            for (String memberId : request.getOtherMembersId()){
                if (!friendRepository.checkIfFriend(initiatorId, memberId)){
                    throw new Exception("Unable to add members that are not your friend");
                }
            }

            GroupDO groupDO = new GroupDO();
            groupDO.setName(request.getName());
            
            List<AccountDO> memberAccounts = new ArrayList<>();
            
            for (String memberId : request.getOtherMembersId()){
                AccountDO account = accountRepository.findById(memberId).orElseThrow(() -> new Exception("Account ID does not exist: " + memberId));
                memberAccounts.add(account);
            }

            memberAccounts.add(accountRepository.findById(initiatorId).orElseThrow(() -> new Exception("Your account does not exist")));

            groupDO.setMembers(memberAccounts);

            groupRepository.createGroup(groupDO);

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

    @Override
    public GetMyGroupsResult getMyGroups(Jwt jwt) {
        String initiatorId = jwt.getClaimAsString("user_id");

        GetMyGroupsResult result = new GetMyGroupsResult();

        try {
            List<GroupDO> myGroups = groupRepository.findGroupsByUserId(initiatorId);

            result.setSuccess(true);
            result.setStatusCode(200);
            result.setData(myGroups);

            return result;
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setStatusCode(500);
            result.setErrorMessage(e.toString());
            return result;
        }
    }

    @Override
    public InviteFriendResult inviteFriend(InviteFriendRequest request, Jwt jwt) {
        String initiatorId = jwt.getClaimAsString("user_id");

        InviteFriendResult result = new InviteFriendResult();

        try {
            List<AccountDO> memberAccounts = groupRepository.findGroupById(request.getGroupId()).orElseThrow(() -> new Exception("Group not found")).getMembers();

            if (accountRepository.findById(request.getFriendId()).isEmpty()){
                throw new Exception("Friend account does not exist");
            }

            if (groupRepository.findGroupById(request.getGroupId()).isEmpty()){
                throw new Exception("Group does not exist");
            }

            if (!memberAccounts.stream().anyMatch(a -> a.getId().equals(initiatorId))){
                throw new Exception("Your account is not in the specified group");
            }

            if (memberAccounts.stream().anyMatch(a -> a.getId().equals(request.getFriendId()))){
                throw new Exception("Friend account is already in the specified group");
            }
            
            groupRepository.joinGroup(request.getGroupId(), request.getFriendId());

            
            result.setSuccess(true);
            result.setStatusCode(200);
            result.setData(groupRepository.findGroupById(request.getGroupId()).orElseThrow(() -> new Exception("Group cannot be found")));
            return result;
        } catch (Exception e) {
            result.setSuccess(false);
            result.setStatusCode(500);
            result.setErrorMessage(e.toString());
            return result;
        }
    }

    @Override
    public DeleteGroupResult deleteGroup(DeleteGroupRequest request, Jwt jwt) {
        String initiatorId = jwt.getClaimAsString("user_id");

        DeleteGroupResult result = new DeleteGroupResult();

        try{
            List<AccountDO> memberAccounts = groupRepository.findGroupById(request.getGroupId()).orElseThrow(() -> new Exception("Group not found")).getMembers();

            if (groupRepository.findGroupById(request.getGroupId()).isEmpty()){
                throw new Exception("Group does not exist");
            }

            if (!memberAccounts.stream().anyMatch(a -> a.getId().equals(initiatorId))){
                throw new Exception("Your account is not in the specified group");
            }

            groupRepository.deleteGroup(request.getGroupId());

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
