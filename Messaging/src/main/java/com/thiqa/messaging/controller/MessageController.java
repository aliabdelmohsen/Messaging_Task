package com.thiqa.messaging.controller;

import com.thiqa.messaging.enums.ChannelEnum;
import com.thiqa.messaging.request.MessageRequest;
import com.thiqa.messaging.response.MessageResponse;
import com.thiqa.messaging.service.MessageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("api/v1/messages")
@AllArgsConstructor
public class MessageController {
    private final MessageService messagingService;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody @Valid final MessageRequest request) {
        messagingService.sendMessage(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageSource.getMessage("MESSAGE_SENT_SUCCESSFULLY", null, Locale.US));
    }

    @GetMapping
    public ResponseEntity<MessageResponse> getMessages(@RequestParam @NonNull final ChannelEnum channel) {
        return ResponseEntity.ok(messagingService.getAllMessagesByUser(channel.name()));
    }
}
