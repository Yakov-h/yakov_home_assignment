package com.example.customersupporthub.service;

import com.example.customersupporthub.api.dto.CreateTicketRequest;
import com.example.customersupporthub.domain.Role;
import com.example.customersupporthub.domain.Ticket;
import com.example.customersupporthub.domain.User;
import com.example.customersupporthub.exception.ApiException;
import com.example.customersupporthub.repository.TicketRepository;
import com.example.customersupporthub.repository.UserRepository;
import com.example.customersupporthub.security.AppUserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Ticket createTicket(AppUserPrincipal principal, CreateTicketRequest request) {
        if (!(principal.isAdmin() || principal.role() == Role.CUSTOMER)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only customers can create tickets.");
        }
        User customer = userRepository.findById(principal.id())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Customer not found."));

        Ticket ticket = new Ticket();
        ticket.setSubject(request.subject());
        ticket.setDescription(request.description());
        ticket.setCustomer(customer);
        return ticketRepository.save(ticket);
    }

    @Transactional(readOnly = true)
    public List<Ticket> getOwnTickets(AppUserPrincipal principal) {
        if (!(principal.isAdmin() || principal.role() == Role.CUSTOMER)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only customers can view their tickets.");
        }
        if (principal.isAdmin()) {
            return ticketRepository.findAll();
        }
        return ticketRepository.findAllByCustomerIdOrderByCreatedAtDesc(principal.id());
    }

    @Transactional(readOnly = true)
    public List<Ticket> searchAgentTickets(AppUserPrincipal principal, String subject) {
        if (!(principal.isAdmin() || principal.role() == Role.AGENT)) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Only agents can view customer tickets.");
        }
        if (principal.isAdmin()) {
            return ticketRepository.findAll();
        }
        if (subject == null || subject.isBlank()) {
            return ticketRepository.findAllByCustomerAgentIdOrderByCreatedAtDesc(principal.id());
        }
        return ticketRepository.findAllByCustomerAgentIdAndSubjectContainingIgnoreCaseOrderByCreatedAtDesc(principal.id(), subject.trim());
    }
}
