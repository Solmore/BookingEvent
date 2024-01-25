package org.eventbook.eventbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class EventBookingApplication {

    public static void main(final String[] args) {
        SpringApplication.run(EventBookingApplication.class, args);
    }

}
