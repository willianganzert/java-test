package com.sicred.votacoop.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "session")
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    @NotNull(message = "Topic is required.")
    private Topic topic;

    @Column(nullable = false)
    @NotNull(message = "Start time is required.")
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    @NotNull(message = "End time is required.")
    private LocalDateTime endTime;

    @Column(name = "duration", nullable = false)
    @NotNull(message = "Duration is required.")
    @Min(value = 60, message = "Duration should be at least 60 seconds.")
    private Integer duration;

    @Column(name = "message_sent", nullable = false, columnDefinition = "boolean default false")
    private Boolean messageSent = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getMessageSent() {
        return messageSent;
    }

    public void setMessageSent(Boolean messageSent) {
        this.messageSent = messageSent;
    }
}