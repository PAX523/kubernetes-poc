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

    private String getBaseUrl()
    {
        return String.format("http://%s:%d", properties.getInternalServiceHost(), properties.getInternalServicePort());
    }

    @GetMapping(path = "/ping/{response}")
    public ResponseEntity<String> ping(@PathVariable("response") final String response)
    {
        return restTemplate.getForEntity(String.format("%s/ping/%s", getBaseUrl(), response), String.class);
    }

    @PostMapping(path = "/message/save")
    public void saveMessage(@RequestParam("message") final String message)
    {
        restTemplate.postForEntity(String.format("%s/message/save?message=%s", getBaseUrl(), message), Void.class, String.class);
    }

    @GetMapping(path = "/message/all")
    public ResponseEntity<List> getAllMessages()
    {
        return restTemplate.getForEntity(String.format("%s/message/all", getBaseUrl()), List.class);
    }
}
