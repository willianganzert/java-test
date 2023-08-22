package com.sicred.votacoop.services;

import com.sicred.votacoop.dtos.VotingResultDTO;
import com.sicred.votacoop.exceptions.BusinessException;
import com.sicred.votacoop.exceptions.ResourceNotFoundException;
import com.sicred.votacoop.models.Session;
import com.sicred.votacoop.models.Vote;
import com.sicred.votacoop.repositories.SessionRepository;
import com.sicred.votacoop.repositories.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VoteService {

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private UserIntegrationService userIntegrationService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;



    // Vote in a session
    public void vote(Long sessionId, Vote vote) {
        // Ensure the session exists
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        // Check if the voting session is currently active
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(session.getStartTime()) || now.isAfter(session.getEndTime())) {
            throw new BusinessException("Voting session is not currently active", HttpStatus.CONFLICT);
        }

        // Ensure the CPF hasn't already voted in the current session
        String cpf = vote.getMemberCpf();
        if (cpf == null || cpf.length() != 11) {
            throw new BusinessException("Invalid CPF details", HttpStatus.BAD_REQUEST);
        }

        String votingStatus = userIntegrationService.getUserVotingStatus(vote.getMemberCpf());

        if("UNABLE_TO_VOTE".equals(votingStatus)) {
            throw new BusinessException("This member is unable to vote.", HttpStatus.FORBIDDEN);
        }

        boolean hasAlreadyVoted = voteRepository.findBySessionIdAndMemberCpf(sessionId, cpf).isPresent();
        if (hasAlreadyVoted) {
            throw new BusinessException(String.format("CPF %s has already voted in this session", cpf), HttpStatus.CONFLICT);
        }

        // Save the vote
        vote.setSession(session);
        voteRepository.save(vote);
    }

    // Get voting results for a session
    public VotingResultDTO getSessionResults(Long sessionId) {
        List<Vote> votes = voteRepository.findBySessionId(sessionId);

        Long totalVotes = (long) votes.size();
        Long affirmativeVotes = votes.stream().filter(Vote::getVoteValue).count();
        Long negativeVotes = totalVotes - affirmativeVotes;

        String result = (affirmativeVotes > negativeVotes) ? "Passed" : "Failed";

        return new VotingResultDTO(totalVotes, affirmativeVotes, negativeVotes, result);
    }

    @Scheduled(fixedRate = 60000)  // Every minute
    public void checkSessions() {
        List<Session> endedSessions = sessionRepository.findByEndTimeBeforeAndMessageSentFalse(LocalDateTime.now());
        for (Session session : endedSessions) {

            kafkaTemplate.send("voting-session-result", session.getId().toString(), getSessionResults(session.getId()));

            session.setMessageSent(true);
            sessionRepository.save(session);
        }
    }


}
