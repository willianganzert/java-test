package com.sicred.votacoop.services;

import com.sicred.votacoop.dtos.TopicView;
import com.sicred.votacoop.exceptions.ResourceNotFoundException;
import com.sicred.votacoop.models.Session;
import com.sicred.votacoop.models.Topic;
import com.sicred.votacoop.repositories.SessionRepository;
import com.sicred.votacoop.repositories.TopicRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class TopicServiceTest {

    @Autowired
    private Validator validator;

    @InjectMocks
    private TopicService topicService;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private SessionRepository sessionRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateTopic() {
        Topic topic = new Topic();
        Mockito.when(topicRepository.save(any())).thenReturn(topic);

        Topic result = topicService.createTopic(topic);
        assertEquals(topic, result);
    }

    @Test
    public void testStartSessionDefaultDuration() {
        Topic topic = new Topic();
        Session session = new Session();
        Mockito.when(topicRepository.findById(any())).thenReturn(Optional.of(topic));
        Mockito.when(sessionRepository.save(any())).thenAnswer(new Answer<Session>() {
            @Override
            public Session answer(InvocationOnMock invocation) throws Throwable {
                return (Session) invocation.getArguments()[0];
            }
        });

        Session result = topicService.startSession(1L, Optional.empty());
        assertEquals(60, result.getDuration());

    }

    @Test
    public void testStartSessionWithDuration() {
        Topic topic = new Topic();
        Session session = new Session();
        Mockito.when(topicRepository.findById(any())).thenReturn(Optional.of(topic));
        Mockito.when(sessionRepository.save(any())).thenReturn(session);

        Session result = topicService.startSession(1L, Optional.of(120));
        assertEquals(session, result);
    }

    @Test
    public void testGetTopicById() {
        Topic topic = new Topic();
        Mockito.when(topicRepository.findById(any())).thenReturn(Optional.of(topic));

        Topic result = topicService.getTopicById(1L);
        assertEquals(topic, result);
    }

    @Test
    public void testGetTopicByIdNotFound() {
        Mockito.when(topicRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> topicService.getTopicById(1L));
    }

    @Test
    public void testGetAllTopics() {
        List<Topic> topics = List.of(new Topic(), new Topic());
        Mockito.when(topicRepository.findAll()).thenReturn(topics);

        List<Topic> result = topicService.getAllTopics();
        assertEquals(topics.size(), result.size());
    }

    @Test
    public void testGetAllTopicsOnly() {
        ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

        List<TopicView> topicViews = List.of(factory.createProjection(TopicView.class), factory.createProjection(TopicView.class));
        Mockito.when(topicRepository.findBy()).thenReturn(topicViews);

        List<TopicView> result = topicService.getAllTopicsOnly();
        assertEquals(topicViews.size(), result.size());
    }
}