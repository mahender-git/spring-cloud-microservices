package com.security.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.List;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    UserDetailsService userDetailService;

    @Override
    public Authentication authenticate(Authentication a) throws AuthenticationException {
        try {
            UserDetails user = userDetailService.loadUserByUsername(a.getName());
            List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
            if (a.getName().equals(user.getUsername()) && user.getPassword().equals(a.getCredentials().toString())) {
                grantedAuthorities.addAll(user.getAuthorities());
                return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), grantedAuthorities);
            }else{
                throw new UsernameNotFoundException("Username/Password not found.....");
            }
        } catch (UsernameNotFoundException e) {
            throw e;
        }
    }

    public void setUserDetailService(UserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }

}
