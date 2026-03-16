package com.example.customersupporthub.api;

import com.example.customersupporthub.api.dto.CreateCustomerRequest;
import com.example.customersupporthub.api.dto.UserResponse;
import com.example.customersupporthub.api.mapper.ApiMapper;
import com.example.customersupporthub.security.SecurityUtils;
import com.example.customersupporthub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final UserService userService;
    private final SecurityUtils securityUtils;
    private final ApiMapper apiMapper;

    public CustomerController(UserService userService, SecurityUtils securityUtils, ApiMapper apiMapper) {
        this.userService = userService;
        this.securityUtils = securityUtils;
        this.apiMapper = apiMapper;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createCustomer(Authentication authentication, @Valid @RequestBody CreateCustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiMapper.toUserResponse(userService.createCustomer(securityUtils.currentUser(authentication), request)));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getCustomers(Authentication authentication) {
        List<UserResponse> customers = userService.getCustomersForAgent(securityUtils.currentUser(authentication))
                .stream()
                .map(apiMapper::toUserResponse)
                .toList();
        return ResponseEntity.ok(customers);
    }
}
