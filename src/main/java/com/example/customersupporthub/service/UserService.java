package com.example.customersupporthub.service;

import com.example.customersupporthub.api.dto.CreateCustomerRequest;
import com.example.customersupporthub.api.dto.UpdateProfileRequest;
import com.example.customersupporthub.domain.Role;
import com.example.customersupporthub.domain.User;
import com.example.customersupporthub.exception.ApiException;
import com.example.customersupporthub.repository.UserRepository;
import com.example.customersupporthub.security.AppUserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createCustomer(AppUserPrincipal principal, CreateCustomerRequest request) {
        if (!(principal.isAdmin() || principal.role() == Role.AGENT)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only agents or admins can create customers.");
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new ApiException(HttpStatus.CONFLICT, "Username already exists.");
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new ApiException(HttpStatus.CONFLICT, "Email already exists.");
        }

        User customer = new User();
        customer.setUsername(request.username());
        customer.setPassword(passwordEncoder.encode(request.password()));
        customer.setFullName(request.fullName());
        customer.setEmail(request.email());
        customer.setRole(Role.CUSTOMER);

        if (!principal.isAdmin()) {
            User agent = getRequiredUser(principal.id());
            customer.setAgent(agent);
        }

        return userRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public List<User> getCustomersForAgent(AppUserPrincipal principal) {
        if (principal.isAdmin()) {
            return userRepository.findAll().stream().filter(u -> u.getRole() == Role.CUSTOMER).toList();
        }
        if (principal.role() != Role.AGENT) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only agents can view their customers.");
        }
        return userRepository.findAllByAgentIdAndRole(principal.id(), Role.CUSTOMER);
    }

    @Transactional(readOnly = true)
    public User getOwnProfile(AppUserPrincipal principal) {
        return getRequiredUser(principal.id());
    }

    @Transactional
    public User updateOwnProfile(AppUserPrincipal principal, UpdateProfileRequest request) {
        User user = getRequiredUser(principal.id());
        userRepository.findByEmail(request.email())
                .filter(existing -> !existing.getId().equals(user.getId()))
                .ifPresent(existing -> {
                    throw new ApiException(HttpStatus.CONFLICT, "Email already exists.");
                });
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        return userRepository.save(user);
    }

    private User getRequiredUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found."));
    }
}
