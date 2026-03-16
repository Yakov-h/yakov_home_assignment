package com.example.customersupporthub.service;

import com.example.customersupporthub.api.dto.CreateCustomerRequest;
import com.example.customersupporthub.domain.Role;
import com.example.customersupporthub.domain.User;
import com.example.customersupporthub.exception.ApiException;
import com.example.customersupporthub.repository.UserRepository;
import com.example.customersupporthub.security.AppUserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;

    private User agent;

    @BeforeEach
    void setUp() {
        agent = new User();
        agent.setId(10L);
        agent.setRole(Role.AGENT);
    }

    @Test
    void createCustomerShouldAttachAgentAndEncodePassword() {
        AppUserPrincipal principal = new AppUserPrincipal(10L, "agent1", Role.AGENT);
        CreateCustomerRequest request = new CreateCustomerRequest("newcustomer", "Password123!", "New Customer", "new@example.com");

        when(userRepository.existsByUsername(request.username())).thenReturn(false);
        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(userRepository.findById(10L)).thenReturn(Optional.of(agent));
        when(passwordEncoder.encode(request.password())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.createCustomer(principal, request);

        assertThat(result.getRole()).isEqualTo(Role.CUSTOMER);
        assertThat(result.getAgent()).isEqualTo(agent);
        assertThat(result.getPassword()).isEqualTo("encoded-password");
    }

    @Test
    void createCustomerShouldFailForDuplicateUsername() {
        AppUserPrincipal principal = new AppUserPrincipal(10L, "agent1", Role.AGENT);
        CreateCustomerRequest request = new CreateCustomerRequest("existing", "Password123!", "New Customer", "new@example.com");
        when(userRepository.existsByUsername("existing")).thenReturn(true);

        assertThatThrownBy(() -> userService.createCustomer(principal, request))
                .isInstanceOf(ApiException.class)
                .hasMessageContaining("Username already exists");
    }
}
