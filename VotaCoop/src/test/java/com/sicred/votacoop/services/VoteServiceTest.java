package com.sicred.votacoop.services;

import com.sicred.votacoop.dtos.VotingResultDTO;
import com.sicred.votacoop.exceptions.BusinessException;
import com.sicred.votacoop.exceptions.ResourceNotFoundException;
import com.sicred.votacoop.models.Session;
import com.sicred.votacoop.models.Vote;
import com.sicred.votacoop.repositories.SessionRepository;
import com.sicred.votacoop.repositories.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VoteServiceTest {

    @InjectMocks
    private VoteService voteService;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserIntegrationService userIntegrationService;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testVote_sessionDoesNotExist() {
        Mockito.when(sessionRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> voteService.vote(1L, new Vote()));
    }

    @Test
    public void testVote_sessionNotActive() {
        Session session = new Session();
        session.setStartTime(LocalDateTime.now().plusHours(1));  // 1 hour in the future
        session.setEndTime(LocalDateTime.now().plusHours(2));
        Mockito.when(sessionRepository.findById(any())).thenReturn(Optional.of(session));

        assertThrows(BusinessException.class, () -> voteService.vote(1L, new Vote()));
    }

    @Test
    public void testVote_invalidCpf() {
        Session session = new Session();
        session.setStartTime(LocalDateTime.now().minusHours(1));
        session.setEndTime(LocalDateTime.now().plusHours(1));

        Mockito.when(sessionRepository.findById(any())).thenReturn(Optional.of(session));

        // Test with a null CPF
        Vote vote = new Vote();
        assertThrows(BusinessException.class, () -> voteService.vote(1L, vote));

        // Test with an invalid length CPF
        vote.setMemberCpf("12345");
        assertThrows(BusinessException.class, () -> voteService.vote(1L, vote));
    }

    @Test
    public void testVote_memberCannotVote() {
        Session session = new Session();
        session.setStartTime(LocalDateTime.now().minusHours(1));
        session.setEndTime(LocalDateTime.now().plusHours(1));

        Mockito.when(sessionRepository.findById(any())).thenReturn(Optional.of(session));
        Mockito.when(userIntegrationService.getUserVotingStatus(anyString())).thenReturn("UNABLE_TO_VOTE");

        Vote vote = new Vote();
        vote.setMemberCpf("12345678901");
        assertThrows(BusinessException.class, () -> voteService.vote(1L, vote));
    }

    @Test
    public void testVote_memberAlreadyVoted() {
        Session session = new Session();
        session.setStartTime(LocalDateTime.now().minusHours(1));
        session.setEndTime(LocalDateTime.now().plusHours(1));

        Mockito.when(sessionRepository.findById(any())).thenReturn(Optional.of(session));
        Mockito.when(voteRepository.findBySessionIdAndMemberCpf(anyLong(), anyString())).thenReturn(Optional.of(new Vote()));

        Vote vote = new Vote();
        vote.setMemberCpf("12345678901");
        assertThrows(BusinessException.class, () -> voteService.vote(1L, vote));
    }

    @Test
    public void testVote_success() {
        Session session = new Session();
        session.setStartTime(LocalDateTime.now().minusHours(1));
        session.setEndTime(LocalDateTime.now().plusHours(1));

        Mockito.when(sessionRepository.findById(any())).thenReturn(Optional.of(session));
        Mockito.when(userIntegrationService.getUserVotingStatus(anyString())).thenReturn("ABLE_TO_VOTE");
        Mockito.when(voteRepository.findBySessionIdAndMemberCpf(anyLong(), anyString())).thenReturn(Optional.empty());
        Mockito.when(voteRepository.save(any())).thenReturn(new Vote());

        Vote vote = new Vote();
        vote.setMemberCpf("12345678901");
        assertDoesNotThrow(() -> voteService.vote(1L, vote));
    }

    @Test
    public void testGetSessionResults() {
        Session session = new Session();
        Mockito.when(voteRepository.findBySessionId(anyLong())).thenReturn(List.of(new Vote(session, "", true), new Vote(session, "", true), new Vote(session, "", false)));

        VotingResultDTO result = voteService.getSessionResults(1L);
        assertEquals(3, result.getTotalVotes());
        assertEquals(2, result.getAffirmativeVotes());
        assertEquals(1, result.getNegativeVotes());
        assertEquals("Passed", result.getResult());
    }

    @Test
    public void testCheckSessions_noEndedSessions() {
        Mockito.when(sessionRepository.findByEndTimeBeforeAndMessageSentFalse(any())).thenReturn(new ArrayList<>());

        voteService.checkSessions();
        verify(kafkaTemplate, never()).send(anyString(), anyString(), any());
    }

    @Test
    public void testCheckSessions_withEndedSessions() {
        Session session = new Session();
        session.setId(1L);
        session.setMessageSent(false);

        Mockito.when(sessionRepository.findByEndTimeBeforeAndMessageSentFalse(any())).thenReturn(List.of(session));
        Mockito.when(voteRepository.findBySessionId(anyLong())).thenReturn(List.of(new Vote(session, "", true), new Vote(session, "", true), new Vote(session, "", false)));
        Mockito.when(sessionRepository.save(any())).thenReturn(session);

        voteService.checkSessions();

        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), any());
        assertTrue(session.getMessageSent());
    }
}

