package com.example.externalservice.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ApplicationProperties
{
    @Value("${internal-service.host}")
    private String internalServiceHost;

    @Value("${internal-service.port}")
    private int internalServicePort;
}
