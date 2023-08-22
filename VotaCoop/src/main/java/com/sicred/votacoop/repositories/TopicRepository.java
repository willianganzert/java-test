package com.sicred.votacoop.repositories;

import com.sicred.votacoop.dtos.TopicView;
import com.sicred.votacoop.models.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<TopicView> findBy();
}
