package org.eventbook.eventbooking.service.impl;

import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.domain.event.Event;
import org.eventbook.eventbooking.domain.exception.ResourceNotFoundException;
import org.eventbook.eventbooking.domain.user.User;
import org.eventbook.eventbooking.repository.UserRepository;
import org.eventbook.eventbooking.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found.")
        );
    }

    @Override
    @Transactional
    public void create(final User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getAllByUserId(final BigInteger id) {
        return userRepository.findAllByUserId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isEventOwner(final BigInteger userId,
                                final BigInteger eventId) {
        return userRepository.isEventOwner(userId, eventId);
    }


}
