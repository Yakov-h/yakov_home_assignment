package com.example.customersupporthub.api;

import com.example.customersupporthub.api.dto.UpdateProfileRequest;
import com.example.customersupporthub.api.dto.UserResponse;
import com.example.customersupporthub.api.mapper.ApiMapper;
import com.example.customersupporthub.security.SecurityUtils;
import com.example.customersupporthub.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserService userService;
    private final SecurityUtils securityUtils;
    private final ApiMapper apiMapper;

    public ProfileController(UserService userService, SecurityUtils securityUtils, ApiMapper apiMapper) {
        this.userService = userService;
        this.securityUtils = securityUtils;
        this.apiMapper = apiMapper;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyProfile(Authentication authentication) {
        return ResponseEntity.ok(apiMapper.toUserResponse(userService.getOwnProfile(securityUtils.currentUser(authentication))));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyProfile(Authentication authentication, @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(apiMapper.toUserResponse(userService.updateOwnProfile(securityUtils.currentUser(authentication), request)));
    }
}
