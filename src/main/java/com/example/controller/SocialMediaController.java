package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private final MessageService messageService;
    private final AccountService accountService;

    @Autowired
    public SocialMediaController(MessageService messageService, AccountService accountService) {
        this.messageService = messageService;
        this.accountService = accountService;
    }

    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @GetMapping("/messages/{message_id}")
    public Message getMessagesById(@PathVariable int message_id) {
        return messageService.getMessagesById(message_id);
    }

    @DeleteMapping("/messages/{message_id}")
    public int deleteMessageById(@PathVariable int message_id) {
        return messageService.deleteMessageById(message_id);
    }

    @GetMapping("/accounts/{account_id}/messages")
    public List<Message> getMessagesByAccountId(@PathVariable int account_id) {
        return messageService.getMessagesByAccountId(account_id);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        if (!account.getUsername().isEmpty() &&
            account.getPassword().length() >= 4) {
            if (accountService.isUserNameTaken(account.getUsername())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
            } else {
                Account createdAccount = accountService.createAccount(account);
                return ResponseEntity.ok(createdAccount); 
            }
        } else {
            return ResponseEntity.badRequest().build(); 
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginAccount(@RequestBody Account account) {
        try {
            if (account.getUsername().isBlank() || account.getPassword().isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
            }

            Account accountReturned = accountService.authenticate(account.getUsername(), account.getPassword());

            if (accountReturned != null) {
                return ResponseEntity.ok(accountReturned); 
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); 
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        try {
            // Validate the input
            if (message.getMessage_text().isBlank() ||
                message.getMessage_text().length() > 255 ||
                !accountService.doesUserExist(message.getPosted_by())) {
                return ResponseEntity.badRequest().build(); // 400 Bad Request
            }

            // Create a new message
            Message messageReturned = messageService.createMessage(message);

            return ResponseEntity.ok(messageReturned); // 200 OK with the created message
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request for other errors
        }
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<?> updateMessage(
        @PathVariable int message_id,
        @RequestBody Message message
    ) {
        try {
            // Validate the input
            if (message.getMessage_text().isBlank() || 
                message.getMessage_text().length() > 255) {
                return ResponseEntity.badRequest().build(); // 400 Bad Request
            }

            // Update the message
            int rowsUpdated = messageService.updateMessage(message_id, message.getMessage_text());

            if (rowsUpdated == 1) {
                return ResponseEntity.ok(rowsUpdated); // 200 OK with the number of rows updated
            } else {
                return ResponseEntity.badRequest().build(); // 400 Bad Request if update fails
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request for other errors
        }
    }

    
}
