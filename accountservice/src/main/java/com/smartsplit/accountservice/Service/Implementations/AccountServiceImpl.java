package com.smartsplit.accountservice.Service.Implementations;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartsplit.accountservice.DTO.RegisterDTO;
import com.smartsplit.accountservice.Exceptions.DuplicateEmailException;
import com.smartsplit.accountservice.Model.AccountDO;
import com.smartsplit.accountservice.Repository.AccountRepository;
import com.smartsplit.accountservice.Results.RegisterResult;
import com.smartsplit.accountservice.Service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountServiceImpl(AccountRepository accountRepository, PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResult register(RegisterDTO dto) {
        AccountDO newAccount = new AccountDO();
        newAccount.setUsername(dto.getUsername());
        newAccount.setEmail(dto.getEmail());
        newAccount.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        newAccount.setSignedInWithGoogle(dto.getSignedInWithGoogle());
        
        RegisterResult result = new RegisterResult();

        try {
            if (accountRepository.findByEmail(dto.getEmail()).isPresent()){
                throw new DuplicateEmailException("Email already registered");
            }
            accountRepository.save(newAccount);
            result.setSuccess(true);
            result.setMessage("Account created successfully");
        } catch(DuplicateEmailException e){
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
        }

        return result;
    }


    
}
