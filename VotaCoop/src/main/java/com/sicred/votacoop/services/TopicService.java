package com.sicred.votacoop.services;

import com.sicred.votacoop.exceptions.ResourceNotFoundException;
import com.sicred.votacoop.models.Session;
import com.sicred.votacoop.models.Topic;
import com.sicred.votacoop.repositories.SessionRepository;
import com.sicred.votacoop.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private SessionRepository sessionRepository;

    // Create a new topic
    public Topic createTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    // Start a voting session for a topic
    public Session startSession(Long topicId, Optional<Integer> durationInSeconds) {
        // Ensure the topic exists
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        Session session = new Session();
        session.setTopic(topic);

        // Set the session's start time to now
        session.setStartTime(LocalDateTime.now());

        // If the duration is provided, set the end time accordingly. Else, default it to 60 seconds.
        int actualDuration = durationInSeconds.orElse(60); // Default to 60 seconds
        session.setDuration(actualDuration);
        session.setEndTime(LocalDateTime.now().plusSeconds(actualDuration)); // Convert to milliseconds

        return sessionRepository.save(session);
    }

    // Get all topics
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }
}
