package com.ars.api.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;


@Configuration
public class RabbitConfig {

    public static final String EXCHANGE = "tickets.exchange";
    public static final String QUEUE = "tickets.created.queue";
    public static final String ROUTING_KEY = "ticket.created";

    @Bean
    public DirectExchange ticketsExchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue ticketsCreatedQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Binding ticketsCreatedBinding(Queue ticketsCreatedQueue, DirectExchange ticketsExchange) {
        return BindingBuilder.bind(ticketsCreatedQueue).to(ticketsExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
