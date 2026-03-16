package com.example.customersupporthub.repository;

import com.example.customersupporthub.domain.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findAllByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Ticket> findAllByCustomerAgentIdOrderByCreatedAtDesc(Long agentId);
    List<Ticket> findAllByCustomerAgentIdAndSubjectContainingIgnoreCaseOrderByCreatedAtDesc(Long agentId, String subject);
}
