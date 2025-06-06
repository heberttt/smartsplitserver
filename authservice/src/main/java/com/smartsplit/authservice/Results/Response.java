package com.smartsplit.authservice.Results;

import lombok.Data;

@Data
public class Response {
    private Boolean success = false;

    private int statusCode;

    private String errorMessage;
}
