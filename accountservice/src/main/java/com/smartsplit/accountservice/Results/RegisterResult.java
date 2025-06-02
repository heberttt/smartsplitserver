package com.smartsplit.accountservice.Results;

import lombok.Data;

@Data
public class RegisterResult {
   
    private Boolean success = false;

    private String message;

}
