package com.thiqa.messaging.service;

import com.thiqa.messaging.enums.StatusEnum;
import com.thiqa.messaging.model.Message;
import com.thiqa.messaging.model.User;
import com.thiqa.messaging.repository.MessageRepository;
import com.thiqa.messaging.request.MessageRequest;
import com.thiqa.messaging.response.MessageData;
import com.thiqa.messaging.response.MessageResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final User authenticatedUser;

    public void sendMessage(@Valid final MessageRequest request) {
        final Message message = Message.builder()
                .userName(authenticatedUser.getUsername())
                .channel(request.getChannel().name())
                .receiver(request.getReceiver())
                .content(request.getContent())
                .status(StatusEnum.SENT.name())
                .dateTime(LocalDateTime.now())
                .build();
        messageRepository.save(message);
    }

    public MessageResponse getAllMessagesByUser(final String channel) {
        List<Message> messages = messageRepository.findAllByUserNameAndChannel(authenticatedUser.getUsername(), channel);
        List<MessageData> messageData = messages.stream()
                .map(message -> MessageData.builder().userName(message.getUserName())
                        .channel(message.getChannel())
                        .receiver(message.getReceiver())
                        .content(message.getContent())
                        .status(message.getStatus())
                        .dateTime(message.getDateTime()).build()).toList();

        return MessageResponse.builder().messages(messageData).build();

    }
}
