package com.sicred.votacoop.models;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255) // Providing column details can be helpful.
    @NotNull(message = "Topic name cannot be null.")
    @Size(min = 1, max = 255, message = "Topic name must be between 1 and 255 characters.")
    private String title;

    @Column(length = 1000)
    @Size(max = 1000, message = "Description can be up to 1000 characters long.")
    private String description;

    public Topic() {
    }

    public Topic(String title) {
        this.title = title;
    }

    public Topic(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
