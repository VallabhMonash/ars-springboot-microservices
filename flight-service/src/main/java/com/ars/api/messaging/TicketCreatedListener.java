package com.ars.api.messaging;

import com.ars.event.TicketCreatedEvent;
import com.ars.api.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TicketCreatedListener {

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void handleTicketCreated(TicketCreatedEvent event) {
        System.out.println("Ticket created event received: ticketId=" + event.getTicketId()
                + ", flightId=" + event.getFlightId()
                + ", passengerEmail=" + event.getPassengerEmail());
    }
}

