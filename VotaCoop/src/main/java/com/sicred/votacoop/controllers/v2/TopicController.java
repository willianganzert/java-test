package com.sicred.votacoop.controllers.v2;

import com.sicred.votacoop.dtos.TopicView;
import com.sicred.votacoop.models.Session;
import com.sicred.votacoop.models.Topic;
import com.sicred.votacoop.services.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController(value = "topicControllerV2")
@RequestMapping("/api/v2/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @PostMapping()
    public ResponseEntity<Topic> createAgenda(@RequestBody Topic topic) {
        return ResponseEntity.ok(topicService.createTopic(topic));
    }

    @PostMapping("{topicId}/startSession")
    public ResponseEntity<Session> startSession(@PathVariable Long topicId,
                                                @RequestParam(required = false) Integer durationInSeconds) {
        Session session = topicService.startSession(topicId, Optional.ofNullable(durationInSeconds));
        return new ResponseEntity<>(session, HttpStatus.CREATED);

    }

    @GetMapping("/{topicId}/sessions")
    public ResponseEntity<Topic> getTopicWithSessions(@PathVariable Long topicId) {
        return ResponseEntity.ok(topicService.getTopicById(topicId));
    }


    @GetMapping
    public ResponseEntity<List<TopicView>> getAllTopics() {
        return ResponseEntity.ok(topicService.getAllTopicsOnly());
    }
}
