package com.smartsplit.accountservice.Service;

import com.smartsplit.accountservice.DTO.RegisterDTO;
import com.smartsplit.accountservice.Results.GetAccountResult;
import com.smartsplit.accountservice.Results.RegisterResult;

public interface AccountService {
    public RegisterResult register(RegisterDTO dto);

    public GetAccountResult getAccountByEmail(String email);
}
