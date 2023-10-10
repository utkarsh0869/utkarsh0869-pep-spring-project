package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository, MessageRepository messageRepository) {
        this.accountRepository = accountRepository;
        this.messageRepository = messageRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public boolean isUserNameTaken(String username) {
        return accountRepository.existsByUsername(username);
    }

    public Account authenticate(String username, String password) {
        Account account = accountRepository.findByUsernameAndPassword(username, password);
        return account;
    }

    public boolean doesUserExist(int user_id) {
        Optional<Account> user = accountRepository.findById(user_id);
        return user.isPresent();
    }

}
