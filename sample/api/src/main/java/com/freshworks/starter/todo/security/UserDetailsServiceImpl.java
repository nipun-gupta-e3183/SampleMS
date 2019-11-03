package com.freshworks.starter.todo.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private SecurityConfig securityConfig;

    public UserDetailsServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder, SecurityConfig securityConfig) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.securityConfig = securityConfig;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (securityConfig.getJwtSecrets(username) == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(username, bCryptPasswordEncoder.encode("some_secret"), emptyList());
    }
}
