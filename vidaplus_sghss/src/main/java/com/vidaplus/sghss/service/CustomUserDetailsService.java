package com.vidaplus.sghss.service;

import com.vidaplus.sghss.model.entities.User;
import com.vidaplus.sghss.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    //Dependency injection
    @Autowired
    UserRepository userRepository;

    //Method to fetch user
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()){
            throw  new UsernameNotFoundException("Usuário não encontrado pelo e-mail!");
        }

        User user = userOptional.get();

        List<SimpleGrantedAuthority> authorities = Collections.emptyList();

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
               authorities);

    }
}
