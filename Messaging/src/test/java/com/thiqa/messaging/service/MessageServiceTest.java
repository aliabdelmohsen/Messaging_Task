package com.thiqa.messaging.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.thiqa.messaging.enums.ChannelEnum;
import com.thiqa.messaging.model.Message;
import com.thiqa.messaging.model.User;
import com.thiqa.messaging.repository.MessageRepository;
import com.thiqa.messaging.request.MessageRequest;
import com.thiqa.messaging.response.MessageData;
import com.thiqa.messaging.response.MessageResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {MessageService.class})
@ExtendWith(SpringExtension.class)
class MessageServiceTest {
    @MockBean
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @MockBean
    private User user;


    @Test
    @DisplayName("Test sendMessage(MessageRequest)")
    void testSendMessage() {
        Message message = new Message();
        message.setChannel("Channel");
        message.setContent("Not all who wander are lost");
        message.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setId(1L);
        message.setReceiver("Receiver");
        message.setStatus("Status");
        message.setUserName("janedoe");
        when(messageRepository.save(Mockito.<Message>any())).thenReturn(message);
        when(user.getUsername()).thenReturn("janedoe");

        MessageRequest request = new MessageRequest();
        request.setChannel(ChannelEnum.EMAIL);
        request.setContent("Not all who wander are lost");
        request.setReceiver("Receiver");

        messageService.sendMessage(request);

        verify(user).getUsername();
        verify(messageRepository).save(isA(Message.class));
    }


    @Test
    @DisplayName("Test getAllMessagesByUser(String); given Message getChannel() return 'Channel'; then calls getChannel()")
    void testGetAllMessagesByUser_givenMessageGetChannelReturnChannel_thenCallsGetChannel() {
        Message message = mock(Message.class);
        when(message.getChannel()).thenReturn("Channel");
        when(message.getContent()).thenReturn("Not all who wander are lost");
        when(message.getReceiver()).thenReturn("Receiver");
        when(message.getStatus()).thenReturn("Status");
        when(message.getUserName()).thenReturn("janedoe");
        when(message.getDateTime()).thenReturn(LocalDate.of(1970, 1, 1).atStartOfDay());
        doNothing().when(message).setChannel(Mockito.<String>any());
        doNothing().when(message).setContent(Mockito.<String>any());
        doNothing().when(message).setDateTime(Mockito.<LocalDateTime>any());
        doNothing().when(message).setId(Mockito.<Long>any());
        doNothing().when(message).setReceiver(Mockito.<String>any());
        doNothing().when(message).setStatus(Mockito.<String>any());
        doNothing().when(message).setUserName(Mockito.<String>any());
        message.setChannel("Channel");
        message.setContent("Not all who wander are lost");
        message.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setId(1L);
        message.setReceiver("Receiver");
        message.setStatus("Status");
        message.setUserName("janedoe");

        ArrayList<Message> messageList = new ArrayList<>();
        messageList.add(message);
        when(messageRepository.findAllByUserNameAndChannel(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(messageList);
        when(user.getUsername()).thenReturn("janedoe");

        MessageResponse actualAllMessagesByUser = messageService.getAllMessagesByUser("Channel");

        verify(message).getChannel();
        verify(message).getContent();
        verify(message).getDateTime();
        verify(message).getReceiver();
        verify(message).getStatus();
        verify(message).getUserName();
        verify(message).setChannel(eq("Channel"));
        verify(message).setContent(eq("Not all who wander are lost"));
        verify(message).setDateTime(isA(LocalDateTime.class));
        verify(message).setId(eq(1L));
        verify(message).setReceiver(eq("Receiver"));
        verify(message).setStatus(eq("Status"));
        verify(message).setUserName(eq("janedoe"));
        verify(user).getUsername();
        verify(messageRepository).findAllByUserNameAndChannel(eq("janedoe"), eq("Channel"));
        List<MessageData> messages = actualAllMessagesByUser.getMessages();
        assertEquals(1, messages.size());
        MessageData getResult = messages.get(0);
        assertEquals("Channel", getResult.getChannel());
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("Receiver", getResult.getReceiver());
        assertEquals("Status", getResult.getStatus());
        assertEquals("janedoe", getResult.getUserName());
    }


    @Test
    @DisplayName("Test getAllMessagesByUser(String); then return Messages Empty")
    void testGetAllMessagesByUser_thenReturnMessagesEmpty() {
        when(messageRepository.findAllByUserNameAndChannel(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(new ArrayList<>());
        when(user.getUsername()).thenReturn("janedoe");

        MessageResponse actualAllMessagesByUser = messageService.getAllMessagesByUser("Channel");

        verify(user).getUsername();
        verify(messageRepository).findAllByUserNameAndChannel(eq("janedoe"), eq("Channel"));
        assertTrue(actualAllMessagesByUser.getMessages().isEmpty());
    }

    @Test
    @DisplayName("Test getAllMessagesByUser(String); then return Messages size is one")
    void testGetAllMessagesByUser_thenReturnMessagesSizeIsOne() {

        Message message = new Message();
        message.setChannel("Channel");
        message.setContent("Not all who wander are lost");
        message.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setId(1L);
        message.setReceiver("Receiver");
        message.setStatus("Status");
        message.setUserName("janedoe");

        ArrayList<Message> messageList = new ArrayList<>();
        messageList.add(message);
        when(messageRepository.findAllByUserNameAndChannel(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(messageList);
        when(user.getUsername()).thenReturn("janedoe");

        MessageResponse actualAllMessagesByUser = messageService.getAllMessagesByUser("Channel");

        verify(user).getUsername();
        verify(messageRepository).findAllByUserNameAndChannel(eq("janedoe"), eq("Channel"));
        List<MessageData> messages = actualAllMessagesByUser.getMessages();
        assertEquals(1, messages.size());
        MessageData getResult = messages.get(0);
        assertEquals("Channel", getResult.getChannel());
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("Receiver", getResult.getReceiver());
        assertEquals("Status", getResult.getStatus());
        assertEquals("janedoe", getResult.getUserName());
    }

    @Test
    @DisplayName("Test getAllMessagesByUser(String); then return Messages size is two")
    void testGetAllMessagesByUser_thenReturnMessagesSizeIsTwo() {
        Message message = new Message();
        message.setChannel("Channel");
        message.setContent("Not all who wander are lost");
        message.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        message.setId(1L);
        message.setReceiver("Receiver");
        message.setStatus("Status");
        message.setUserName("janedoe");

        Message message2 = new Message();
        message2.setChannel("com.thiqa.messaging.model.Message");
        message2.setContent("Content");
        message2.setDateTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        message2.setId(2L);
        message2.setReceiver("com.thiqa.messaging.model.Message");
        message2.setStatus("com.thiqa.messaging.model.Message");
        message2.setUserName("User Name");

        ArrayList<Message> messageList = new ArrayList<>();
        messageList.add(message2);
        messageList.add(message);
        when(messageRepository.findAllByUserNameAndChannel(Mockito.<String>any(), Mockito.<String>any()))
                .thenReturn(messageList);
        when(user.getUsername()).thenReturn("janedoe");

        MessageResponse actualAllMessagesByUser = messageService.getAllMessagesByUser("Channel");

        verify(user).getUsername();
        verify(messageRepository).findAllByUserNameAndChannel(eq("janedoe"), eq("Channel"));
        List<MessageData> messages = actualAllMessagesByUser.getMessages();
        assertEquals(2, messages.size());
        MessageData getResult = messages.get(1);
        assertEquals("Channel", getResult.getChannel());
        MessageData getResult2 = messages.get(0);
        assertEquals("Content", getResult2.getContent());
        assertEquals("Not all who wander are lost", getResult.getContent());
        assertEquals("Receiver", getResult.getReceiver());
        assertEquals("Status", getResult.getStatus());
        assertEquals("User Name", getResult2.getUserName());
        assertEquals("com.thiqa.messaging.model.Message", getResult2.getChannel());
        assertEquals("com.thiqa.messaging.model.Message", getResult2.getReceiver());
        assertEquals("com.thiqa.messaging.model.Message", getResult2.getStatus());
        assertEquals("janedoe", getResult.getUserName());
    }
}
