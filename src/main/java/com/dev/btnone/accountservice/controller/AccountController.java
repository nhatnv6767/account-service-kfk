package com.dev.btnone.accountservice.controller;


import com.dev.btnone.accountservice.model.MessageDTO;
import com.dev.btnone.accountservice.model.StatisticDTO;
import com.dev.btnone.accountservice.model.AccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * The type Account controller.
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    /**
     * The Kafka template.
     */
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Create account dto.
     *
     * @param account the account
     * @return the account dto
     */
    @PostMapping("/new")
    public AccountDTO create(@RequestBody AccountDTO account) {
        StatisticDTO stat = new StatisticDTO("Account " + account.getEmail() + " is created", new Date());

        // send notification
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTo(account.getEmail());
        messageDTO.setToName(account.getName());
        messageDTO.setSubject("Welcome to My Page");
        messageDTO.setContent("How do like that??");

        kafkaTemplate.send("notification", messageDTO);
        kafkaTemplate.send("statistic", stat);

        return account;
    }

    /**
     * Test string.
     *
     * @return the string
     */
    @GetMapping("/test")
    public String test() {
        return "Test successful!";
    }
}
