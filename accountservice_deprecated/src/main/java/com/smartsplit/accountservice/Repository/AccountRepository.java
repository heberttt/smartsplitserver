package com.smartsplit.accountservice.Repository;

import java.util.List;
import java.util.Optional;

import com.smartsplit.accountservice.Model.AccountDO;

public interface AccountRepository {

    public List<AccountDO> findAll();

    public Optional<AccountDO> findById(Integer id);

    public Optional<AccountDO> findByUsername(String username);

    public Optional<AccountDO> findByEmail(String username);

    public void save(AccountDO accountDO);
}
