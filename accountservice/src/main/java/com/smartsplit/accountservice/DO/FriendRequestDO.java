package com.smartsplit.accountservice.DO;

import com.smartsplit.accountservice.Enum.FriendRequestStatusEnum;

import lombok.Data;

@Data
public class FriendRequestDO {
    int id;
    String target_id;
    String initiator_id;
    FriendRequestStatusEnum status;
}