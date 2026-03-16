package com.example.customersupporthub.api;

import com.example.customersupporthub.api.dto.CreateTicketRequest;
import com.example.customersupporthub.api.dto.TicketResponse;
import com.example.customersupporthub.api.mapper.ApiMapper;
import com.example.customersupporthub.security.SecurityUtils;
import com.example.customersupporthub.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final SecurityUtils securityUtils;
    private final ApiMapper apiMapper;

    public TicketController(TicketService ticketService, SecurityUtils securityUtils, ApiMapper apiMapper) {
        this.ticketService = ticketService;
        this.securityUtils = securityUtils;
        this.apiMapper = apiMapper;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(Authentication authentication, @Valid @RequestBody CreateTicketRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(apiMapper.toTicketResponse(ticketService.createTicket(securityUtils.currentUser(authentication), request)));
    }

    @GetMapping("/mine")
    public ResponseEntity<List<TicketResponse>> getMyTickets(Authentication authentication) {
        List<TicketResponse> tickets = ticketService.getOwnTickets(securityUtils.currentUser(authentication))
                .stream()
                .map(apiMapper::toTicketResponse)
                .toList();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/agent-view")
    public ResponseEntity<List<TicketResponse>> searchAgentTickets(Authentication authentication,
                                                                   @RequestParam(required = false) String subject) {
        List<TicketResponse> tickets = ticketService.searchAgentTickets(securityUtils.currentUser(authentication), subject)
                .stream()
                .map(apiMapper::toTicketResponse)
                .toList();
        return ResponseEntity.ok(tickets);
    }
}
