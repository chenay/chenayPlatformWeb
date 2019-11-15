package com.chenay.web.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JwtUserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        PasswordEncoder passwordEncoder = new PasswordEncoder() {
            @Override
            public String encode(CharSequence charSequence) {
                return null;
            }

            @Override
            public boolean matches(CharSequence charSequence, String s) {
                return false;
            }
        };
        return User.builder().username("chenay")
                .password(passwordEncoder.encode("chenaypw"))
                .roles("USER")
                .build();
    }
}
