package com.smartsplit.accountservice.DO;

import lombok.Data;

@Data
public class AccountDO {
    private String id;
    private String email;
    private String username;
    private String profilePictureLink;
}
