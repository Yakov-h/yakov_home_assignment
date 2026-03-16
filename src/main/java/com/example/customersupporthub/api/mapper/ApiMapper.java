package com.example.customersupporthub.api.mapper;

import com.example.customersupporthub.api.dto.TicketResponse;
import com.example.customersupporthub.api.dto.UserResponse;
import com.example.customersupporthub.domain.Ticket;
import com.example.customersupporthub.domain.User;
import org.springframework.stereotype.Component;

@Component
public class ApiMapper {

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getAgent() != null ? user.getAgent().getId() : null
        );
    }

    public TicketResponse toTicketResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getSubject(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getCustomer().getId(),
                ticket.getCustomer().getUsername(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }
}
