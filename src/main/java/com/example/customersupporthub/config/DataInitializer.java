package com.example.customersupporthub.config;

import com.example.customersupporthub.domain.Role;
import com.example.customersupporthub.domain.User;
import com.example.customersupporthub.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin123!"));
            admin.setRole(Role.ADMIN);
            admin.setFullName("System Admin");
            admin.setEmail("admin@example.com");
            userRepository.save(admin);

            User agent = new User();
            agent.setUsername("agent1");
            agent.setPassword(passwordEncoder.encode("Agent123!"));
            agent.setRole(Role.AGENT);
            agent.setFullName("Primary Agent");
            agent.setEmail("agent1@example.com");
            userRepository.save(agent);

            User customer = new User();
            customer.setUsername("customer1");
            customer.setPassword(passwordEncoder.encode("Customer123!"));
            customer.setRole(Role.CUSTOMER);
            customer.setFullName("First Customer");
            customer.setEmail("customer1@example.com");
            customer.setAgent(agent);
            userRepository.save(customer);
        };
    }
}
