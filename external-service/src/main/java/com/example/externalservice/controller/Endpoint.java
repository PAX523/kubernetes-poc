package com.example.externalservice.controller;

import com.example.externalservice.configuration.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class Endpoint
{
    private final RestTemplate restTemplate = new RestTemplate();

    private final ApplicationProperties properties;

    @GetMapping(path = "/ping/{response}")
    public ResponseEntity<String> ping(@PathVariable("response") final String response)
    {
        return restTemplate.getForEntity(String.format("http://localhost:%d/ping/%s", properties.getExternalServicePort(), response), String.class);
    }

    @PostMapping(path = "/message/save")
    public void saveMessage(@RequestParam("message") final String message)
    {
        restTemplate.postForEntity(String.format("http://localhost:%d/message/save?message=%s", properties.getExternalServicePort(), message), Void.class, String.class);
    }

    @GetMapping(path = "/message/all")
    public ResponseEntity<List> getAllMessages()
    {
        return restTemplate.getForEntity(String.format("http://localhost:%d/message/all", properties.getExternalServicePort()), List.class);
    }
}
