package com.smartsplit.accountservice.Service.Implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.smartsplit.accountservice.DO.AccountDO;
import com.smartsplit.accountservice.Repository.AccountRepository;
import com.smartsplit.accountservice.Request.ChangeProfilePictureRequest;
import com.smartsplit.accountservice.Request.ChangeUsernameRequest;
import com.smartsplit.accountservice.Result.ChangeProfilePictureResult;
import com.smartsplit.accountservice.Result.ChangeUsernameResult;
import com.smartsplit.accountservice.Result.GetAccountsResult;
import com.smartsplit.accountservice.Result.LoginResult;
import com.smartsplit.accountservice.Service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

    final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository repository) {
        this.accountRepository = repository;
    }

    @Override
    public GetAccountsResult getAccountById(String param){
        GetAccountsResult result = new GetAccountsResult();

        try{
            
            final AccountDO account = accountRepository.findById(param).orElseThrow(() -> new Exception("Account not found"));

            result.setData(account);
            result.setSuccess(true);
            result.setStatusCode(200);

            return result;
        }catch(Exception e){
            result.setSuccess(false);
            result.setErrorMessage(e.toString());
            result.setStatusCode(500);
            return result;
        }
    }

    @Override
    public LoginResult login(Jwt jwt) {
        String id = jwt.getClaimAsString("user_id");
        Optional<AccountDO> account = accountRepository.findById(id);

        LoginResult result = new LoginResult();

        if (account.isEmpty()) {
            try {
                AccountDO newAccount = new AccountDO();
                newAccount.setId(id);
                newAccount.setEmail(jwt.getClaimAsString("email"));
                newAccount.setUsername(jwt.getClaimAsString("name") == null ? jwt.getClaimAsString("email")
                        : jwt.getClaimAsString("name"));
                newAccount.setProfilePictureLink(jwt.getClaimAsString("picture") == null
                        ? "https://firebasestorage.googleapis.com/v0/b/smartsplit-87a0b.firebasestorage.app/o/profiles%2Fuser-profile.png?alt=media"
                        : jwt.getClaimAsString("picture"));
                accountRepository.save(newAccount);

                result.setData(newAccount);
                result.setSuccess(true);
                result.setStatusCode(201);

            } catch (Exception e) {
                result.setSuccess(false);
                result.setErrorMessage("Account creation failed");
                result.setStatusCode(500);

                System.out.println(e);
            }
        } else {
            result.setData(account.get());
            result.setSuccess(true);
            result.setStatusCode(200);
        }

        return result;
    }

    @Override
    public ChangeUsernameResult changeUsername(ChangeUsernameRequest request, Jwt jwt) {
        ChangeUsernameResult result = new ChangeUsernameResult();

        if (!isValidGoogleUsername(request.getUsername())) {
            result.setStatusCode(400);
            result.setSuccess(false);
            result.setErrorMessage("Invalid username format");
            return result;
        }

        String id = jwt.getClaimAsString("user_id");

        try {
            accountRepository.changeUsername(request.getUsername(), id);

            Optional<AccountDO> updatedAccount = accountRepository.findById(id);

            if (updatedAccount.isEmpty()) {
                throw new Exception("Updated account not found");
            }

            if (!updatedAccount.get().getUsername().equals(request.getUsername())) {
                throw new Exception("Update name error");
            }

            result.setSuccess(true);
            result.setStatusCode(200);
            result.setData(updatedAccount.get());
            return result;

        } catch (Exception e) {
            result.setSuccess(false);
            result.setStatusCode(500);
            result.setErrorMessage(e.toString());
            return result;
        }
    }

    @Override
    public ChangeProfilePictureResult changeProfilePicture(ChangeProfilePictureRequest request, Jwt jwt) {
        ChangeProfilePictureResult result = new ChangeProfilePictureResult();

        String id = jwt.getClaimAsString("user_id");

        try {
            accountRepository.changeProfilePicture(request.getProfilePictureLink(), id);

            Optional<AccountDO> updatedAccount = accountRepository.findById(id);

            if (updatedAccount.isEmpty()) {
                throw new Exception("Updated account not found");
            }

            result.setSuccess(true);
            result.setStatusCode(200);
            result.setData(updatedAccount.get());
            return result;

        } catch (Exception e) {
            result.setSuccess(false);
            result.setStatusCode(500);
            result.setErrorMessage(e.toString());
            return result;
        }
    }

    public boolean isValidGoogleUsername(String username) {
        if (username == null)
            return false;

        String normalized = username.trim().toLowerCase();

        List<String> forbiddenUsernames = List.of(
                "admin", "root", "support", "system", "null", "undefined");

        return normalized.matches("^[a-z][a-z0-9.\\- ]{2,29}$") &&
                !normalized.endsWith(".") &&
                !normalized.contains("..") &&
                !forbiddenUsernames.contains(normalized);
    }

}
