package com.smartsplit.authservice.Security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.smartsplit.authservice.Clients.AccountClient;
import com.smartsplit.authservice.Models.AccountDO;

@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final AccountClient accountClient;

    public CustomUserDetailsService(AccountClient accountClient){
        this.accountClient = accountClient;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AccountDO user = accountClient.getAccountByEmail(email).getAccountDetails();

        return new User(user.getEmail(), user.getPasswordHash(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
    
}
