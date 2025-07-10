package com.smartsplit.accountservice.Request;

import lombok.Data;

@Data
public class InviteFriendRequest {
    public String friendId;

    public int groupId;
}
