package com.smartsplit.accountservice.Results;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RegisterResult {
   
    private Boolean success = false;

    private String errorMessage;

    @NotNull
    private int statusCode;

}
