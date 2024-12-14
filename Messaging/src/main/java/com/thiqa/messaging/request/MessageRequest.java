package com.thiqa.messaging.request;

import com.thiqa.messaging.enums.ChannelEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {
    @NotNull(message = "Channel is required")
    @Enumerated(EnumType.STRING)
    private ChannelEnum channel;
    @NotNull(message = "Receiver is required")
    private String receiver;
    @NotNull(message = "Content is required")
    private String content;

}
