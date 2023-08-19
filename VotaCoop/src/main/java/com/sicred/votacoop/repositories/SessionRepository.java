package com.sicred.votacoop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sicred.votacoop.models.Session;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByEndTimeBeforeAndMessageSentFalse(LocalDateTime endTime);
}
