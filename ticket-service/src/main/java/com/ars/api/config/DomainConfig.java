package com.ars.api.config;

import com.ars.unit.TicketSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {
    // Singleton via Spring DI: one shared TicketSystem instance for the app
    @Bean
    public TicketSystem ticketSystem() {
        return new TicketSystem();
    }
}
