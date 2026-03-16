package com.example.customersupporthub.repository;

import com.example.customersupporthub.domain.Role;
import com.example.customersupporthub.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findAllByAgentIdAndRole(Long agentId, Role role);
}
