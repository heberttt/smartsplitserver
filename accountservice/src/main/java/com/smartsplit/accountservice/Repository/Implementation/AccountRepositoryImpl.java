package com.smartsplit.accountservice.Repository.Implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.smartsplit.accountservice.DO.AccountDO;
import com.smartsplit.accountservice.Repository.AccountRepository;

@Repository
public class AccountRepositoryImpl implements AccountRepository{

    private final JdbcClient jdbcClient;

    public AccountRepositoryImpl(JdbcClient client){
        this.jdbcClient = client;
    }

    @Override
    public List<AccountDO> findAll() {
        return jdbcClient.sql("SELECT * FROM accounts")
            .query(AccountDO.class)
            .list();
    }

    @Override
    public Optional<AccountDO> findById(String id) {
        return jdbcClient.sql("SELECT id, username, email, profilePictureLink FROM accounts WHERE id = :id")
            .param("id", id)
            .query(AccountDO.class)
            .optional();
    }

    @Override
    public Optional<AccountDO> findByUsername(String username) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findByUsername'");
    }

    @Override
    public Optional<AccountDO> findByEmail(String email) {
        return jdbcClient.sql("SELECT id, username, email, profilePictureLink FROM accounts WHERE email = :email")
                .param("email", email)
                .query(AccountDO.class)
                .optional();
    }

    @Override
    public void save(AccountDO accountDO) {
        var updated = jdbcClient.sql("INSERT INTO accounts(id, username, email, profilePictureLink) values(?, ?, ?, ?)")
                .params(List.of(accountDO.getId(), accountDO.getUsername(), accountDO.getEmail(), accountDO.getProfilePictureLink()))
                .update();

        Assert.state(updated == 1, "Failed to create account " + accountDO.getEmail());
    }

    @Override
    public void changeUsername(String username, String id) {
        int updated = jdbcClient.sql("UPDATE accounts SET username = ? WHERE id = ?")
        .params(List.of(username, id))
        .update();

        Assert.state(updated == 1, "Row updated: " + updated);
    }
    
}
