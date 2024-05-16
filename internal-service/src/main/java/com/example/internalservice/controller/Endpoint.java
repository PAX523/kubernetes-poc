package com.example.internalservice.controller;

import com.example.internalservice.entity.Message;
import com.example.internalservice.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Endpoint {
    private final MessageRepository repository;

    @GetMapping(path = "/ping/{response}")
    public String ping(@PathVariable("response") final String response) {
        return response;
    }

    @PostMapping(path = "/message/save")
    public void saveMessage(@RequestParam("message") final String message) {
        final var result = new Message();
        result.setMessage(message);
        repository.save(result);
    }

    @GetMapping(path = "/message/all")
    public List<Message> getAllMessages() {
        return repository.findAll();
    }
}
