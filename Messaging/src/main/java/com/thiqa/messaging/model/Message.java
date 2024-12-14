package com.thiqa.messaging.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "Messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private static final long serialVersionUID = 2L;

    @Id
    @SequenceGenerator(name = "message-sequence",
            sequenceName = "message-sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "message-sequence")
    private Long id;
    private String userName;
    private String channel;
    private String receiver;
    private String content;
    private String status;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dateTime;
}
