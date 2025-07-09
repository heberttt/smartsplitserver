package com.smartsplit.accountservice.Request;

import lombok.Data;

@Data
public class CreateFriendRequestByEmailRequest {
    String targetEmail;
}
