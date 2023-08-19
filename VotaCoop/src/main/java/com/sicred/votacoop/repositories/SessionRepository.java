package com.sicred.votacoop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sicred.votacoop.models.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
}
