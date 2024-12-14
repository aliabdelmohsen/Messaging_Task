package com.thiqa.messaging.controller;

import com.thiqa.messaging.enums.ChannelEnum;
import com.thiqa.messaging.request.MessageRequest;
import com.thiqa.messaging.response.MessageData;
import com.thiqa.messaging.response.MessageResponse;
import com.thiqa.messaging.service.MessageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {MessageController.class})
@ExtendWith(SpringExtension.class)
class MessageControllerTest {
    @Autowired
    private MessageController messageController;

    @MockBean
    private MessageService messageService;

    @MockBean
    private MessageSource messageSource;

    @Test
    @DisplayName("Test getMessages(ChannelEnum); then content string a string")
    void testGetMessages_thenContentStringAString() throws Exception {
        ArrayList<MessageData> messages = new ArrayList<>();
        MessageData.MessageDataBuilder contentResult = MessageData.builder()
                .channel("Channel")
                .content("Not all who wander are lost");
        MessageData buildResult = contentResult.dateTime(LocalDate.of(1970, 1, 1).atStartOfDay())
                .receiver("Receiver")
                .status("Status")
                .userName("janedoe")
                .build();
        messages.add(buildResult);
        MessageResponse buildResult2 = MessageResponse.builder().messages(messages).build();
        when(messageService.getAllMessagesByUser(Mockito.<String>any())).thenReturn(buildResult2);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/messages");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("channel", String.valueOf(ChannelEnum.EMAIL));

        MockMvcBuilders.standaloneSetup(messageController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"messages\":[{\"userName\":\"janedoe\",\"channel\":\"Channel\",\"receiver\":\"Receiver\",\"content\":\"Not all who"
                                        + " wander are lost\",\"status\":\"Status\",\"dateTime\":[1970,1,1,0,0]}]}"));
    }

    @Test
    @DisplayName("Test sendMessage(MessageRequest); then StatusCode return HttpStatus")
    void testSendMessage_thenStatusCodeReturnHttpStatus() throws NoSuchMessageException {
        MessageService messagingService = mock(MessageService.class);
        doNothing().when(messagingService).sendMessage(Mockito.<MessageRequest>any());
        AnnotationConfigApplicationContext messageSource = mock(AnnotationConfigApplicationContext.class);
        when(messageSource.getMessage(Mockito.<String>any(), Mockito.<Object[]>any(), Mockito.<Locale>any()))
                .thenReturn("Not all who wander are lost");
        MessageController messageController = new MessageController(messagingService, messageSource);

        MessageRequest request = new MessageRequest();
        request.setChannel(ChannelEnum.EMAIL);
        request.setContent("Not all who wander are lost");
        request.setReceiver("Receiver");

        ResponseEntity<String> actualSendMessageResult = messageController.sendMessage(request);

        verify(messagingService).sendMessage(isA(MessageRequest.class));
        verify(messageSource).getMessage(eq("MESSAGE_SENT_SUCCESSFULLY"), isNull(), isA(Locale.class));
        HttpStatusCode statusCode = actualSendMessageResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals("Not all who wander are lost", actualSendMessageResult.getBody());
        assertEquals(201, actualSendMessageResult.getStatusCodeValue());
        assertEquals(HttpStatus.CREATED, statusCode);
        assertTrue(actualSendMessageResult.hasBody());
        assertTrue(actualSendMessageResult.getHeaders().isEmpty());
    }

    @Test
    @DisplayName("Test getMessages(ChannelEnum); then content string '{\"messages\":[]}'")
    void testGetMessages_thenContentStringMessages() throws Exception {
        MessageResponse.MessageResponseBuilder builderResult = MessageResponse.builder();
        MessageResponse buildResult = builderResult.messages(new ArrayList<>()).build();
        when(messageService.getAllMessagesByUser(Mockito.<String>any())).thenReturn(buildResult);
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/v1/messages");
        MockHttpServletRequestBuilder requestBuilder = getResult.param("channel", String.valueOf(ChannelEnum.EMAIL));

        MockMvcBuilders.standaloneSetup(messageController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"messages\":[]}"));
    }
}
