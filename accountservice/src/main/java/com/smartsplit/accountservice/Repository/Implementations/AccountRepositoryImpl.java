package com.smartsplit.accountservice.Repository.Implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.smartsplit.accountservice.Model.AccountDO;
import com.smartsplit.accountservice.Repository.AccountRepository;

@Repository
public class AccountRepositoryImpl implements AccountRepository{

    private final JdbcClient jdbcClient;

    public AccountRepositoryImpl(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<AccountDO> findAll() {
        return jdbcClient.sql("SELECT * FROM accounts")
                .query(AccountDO.class)
                .list();
    }

    @Override
    public void save(AccountDO accountDO) {
        var updated = jdbcClient.sql("INSERT INTO accounts(username, email, password_hash, signed_in_with_google) values(?, ?, ?, ?)")
                .params(List.of(accountDO.getUsername(), accountDO.getEmail(), accountDO.getPasswordHash(), accountDO.getSignedInWithGoogle()))
                .update();

        Assert.state(updated == 1, "Failed to create account " + accountDO.getEmail());
    }

    @Override
    public Optional<AccountDO> findById(Integer id) {
        return jdbcClient.sql("SELECT id, username, email, password_hash, signed_in_with_google FROM accounts WHERE id = :id")
                .param("id", id)
                .query(AccountDO.class)
                .optional();
    }

    @Override
    public Optional<AccountDO> findByUsername(String username) {
        return jdbcClient.sql("SELECT id, username, email, password_hash, signed_in_with_google FROM accounts WHERE username = :username")
                .param("username", username)
                .query(AccountDO.class)
                .optional();
    }

    @Override
    public Optional<AccountDO> findByEmail(String email) {
        return jdbcClient.sql("SELECT id, username, email, password_hash, signed_in_with_google FROM accounts WHERE email = :email")
                .param("email", email)
                .query(AccountDO.class)
                .optional();
    }
    
}
