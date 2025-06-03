package com.smartsplit.accountservice.Service.Implementations;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartsplit.accountservice.DTO.RegisterDTO;
import com.smartsplit.accountservice.Exceptions.DuplicateEmailException;
import com.smartsplit.accountservice.Exceptions.EmailNotFoundException;
import com.smartsplit.accountservice.Model.AccountDO;
import com.smartsplit.accountservice.Repository.AccountRepository;
import com.smartsplit.accountservice.Results.RegisterResult;
import com.smartsplit.accountservice.Service.AccountService;
import com.smartsplit.accountservice.Results.GetAccountResult;

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

        RegisterResult result = new RegisterResult();


        try {
            AccountDO newAccount = new AccountDO();
            newAccount.setUsername(dto.getUsername());
            newAccount.setEmail(dto.getEmail());
            newAccount.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            newAccount.setSignedInWithGoogle(dto.getSignedInWithGoogle());


            if (accountRepository.findByEmail(dto.getEmail()).isPresent()){
                throw new DuplicateEmailException("Email already registered");
            }

            accountRepository.save(newAccount);
            result.setSuccess(true);
            result.setErrorMessage("Account created successfully");
            result.setStatusCode(201);
        } catch(DuplicateEmailException e){
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setStatusCode(409);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setStatusCode(500);
        }

        return result;
    }

    @Override
    public GetAccountResult getAccountByEmail(String email) {
        GetAccountResult result = new GetAccountResult();

        try{
            AccountDO found = accountRepository.findByEmail(email).orElseThrow(() -> new EmailNotFoundException("Email not found"));
            result.setAccountDetails(found);
            result.setSuccess(true);
            result.setStatusCode(200);
        }
        catch (EmailNotFoundException e){
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setStatusCode(404);
        }
        catch(Exception e){
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setStatusCode(500);
        }

        return result;
    }

    // @Override
    // public LoginResult login(LoginDTO dto) {
    //     LoginResult result = new LoginResult();

        
        
    //     try{
    //         Optional<AccountDO> searchedAccount = accountRepository.findByEmail(dto.getEmail());

    //         if (searchedAccount.isEmpty()){
    //             throw new EmailNotFoundException("Email does not exist");
    //         }

    //         AccountDO foundAccount = searchedAccount.get();


            
    //     }catch(EmailNotFoundException e){
    //         result.setSuccess(false);
    //         result.setErrorMessage(e.getMessage());
    //         result.setStatusCode(404);
    //     }

    // }


    
}
