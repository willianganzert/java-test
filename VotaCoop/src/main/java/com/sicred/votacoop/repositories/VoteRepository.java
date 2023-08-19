package com.sicred.votacoop.repositories;

import com.sicred.votacoop.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findBySessionId(Long sessionId);

    Optional<Vote> findBySessionIdAndMemberCpf(Long sessionId, String cpf);
}