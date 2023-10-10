package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{

    @Query("SELECT m FROM Message m JOIN Account a ON m.posted_by = a.account_id WHERE a.account_id = :account_id")
    List<Message> findMessagesByAccountId(@Param("account_id") int account_id);
}
