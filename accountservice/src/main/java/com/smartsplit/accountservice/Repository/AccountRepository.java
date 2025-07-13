package com.smartsplit.accountservice.Repository;

import java.util.List;
import java.util.Optional;

import com.smartsplit.accountservice.DO.AccountDO;

public interface AccountRepository {

    public List<AccountDO> findAll();

    public Optional<AccountDO> findById(String id);

    public Optional<AccountDO> findByUsername(String username);

    public Optional<AccountDO> findByEmail(String email);

    public void save(AccountDO accountDO);

    public void changeUsername(String username, String id);

    public void changeProfilePicture(String profilePicture, String id);

    public List<AccountDO> findByIds(List<String> ids);
}
