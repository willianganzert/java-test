package com.sicred.votacoop.services;

import com.sicred.votacoop.exceptions.BusinessException;
import com.sicred.votacoop.models.Topic;
import com.sicred.votacoop.models.Session;
import com.sicred.votacoop.models.Vote;
import com.sicred.votacoop.repositories.TopicRepository;
import com.sicred.votacoop.repositories.SessionRepository;
import com.sicred.votacoop.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private VoteRepository voteRepository;



    // Vote in a session
    public void vote(Long sessionId, Vote vote) {
        // Ensure the session exists
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // Check if the voting session is currently active
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(session.getStartTime()) || now.isAfter(session.getEndTime())) {
            throw new BusinessException("Voting session is not currently active");
        }

        // Ensure the CPF hasn't already voted in the current session
        String cpf = vote.getMemberCpf();
        if (cpf == null || cpf.length() != 11) {
            throw new BusinessException("Invalid CPF details");
        }

        boolean hasAlreadyVoted = voteRepository.findBySessionIdAndMemberCpf(sessionId, cpf).isPresent();
        if (hasAlreadyVoted) {
            throw new BusinessException(String.format("CPF %s has already voted in this session", cpf));
        }

        // Save the vote
        vote.setSession(session);
        voteRepository.save(vote);
    }

    // Get voting results for a session
    public List<Vote> getSessionResults(Long sessionId) {
        return voteRepository.findBySessionId(sessionId);
    }

}