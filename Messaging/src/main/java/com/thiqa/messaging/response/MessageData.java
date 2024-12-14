package com.thiqa.messaging.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageData {

    private String userName;
    private String channel;
    private String receiver;
    private String content;
    private String status;
    private LocalDateTime dateTime;
}
