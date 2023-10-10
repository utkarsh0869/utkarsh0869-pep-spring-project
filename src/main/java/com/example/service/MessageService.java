package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Message;

import com.example.repository.MessageRepository;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessagesById(int message_id) {
        return messageRepository.findById(message_id).orElse(null);
    }

    public int deleteMessageById(int message_id) {
        Optional <Message> rand = messageRepository.findById(message_id);
        if(rand.isPresent()) {
            messageRepository.deleteById(message_id);
            return 1;
        }
        return 0;
    }
    

    public List<Message> getMessagesByAccountId(int account_id) {
        return messageRepository.findMessagesByAccountId(account_id);
    }

    public Message createMessage(Message message) {
        Message messageNew = new Message();
        messageNew.setMessage_text(message.getMessage_text());
        messageNew.setPosted_by(message.getPosted_by());
        messageNew.setTime_posted_epoch(message.getTime_posted_epoch());

        return messageRepository.save(messageNew);
    }

    public int updateMessage(int message_id, String message_text) {
        // Check if the message with the specified message_id exists
        Optional<Message> existingMessage = messageRepository.findById(message_id);
        if (!existingMessage.isPresent()) {
            return 0; // Message not found, return 0 rows updated
        }

        // Update the message_text and save it
        Message message = existingMessage.get();
        message.setMessage_text(message.getMessage_text());
        messageRepository.save(message);

        return 1; 
    }

}
