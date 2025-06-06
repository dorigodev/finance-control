package dev.dorigo.financecontrol.service;

import dev.dorigo.financecontrol.domain.user.User;
import dev.dorigo.financecontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public User getAuhenticatedUser(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return (User) userRepository.findByEmail(email).orElseThrow();

    }
}
