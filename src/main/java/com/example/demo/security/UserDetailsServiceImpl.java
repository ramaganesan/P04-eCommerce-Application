package com.example.demo.security;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("Autheticating user" + userName);
        User user = userRepository.findByUsername(userName);
        if(user == null){
            throw  new UsernameNotFoundException("Unable to find valid user information");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), Collections.emptyList());
    }
}
