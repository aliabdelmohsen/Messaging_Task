package com.thiqa.messaging.repository;

import com.thiqa.messaging.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByUserNameAndChannel(final String userName, final String channel);

}
