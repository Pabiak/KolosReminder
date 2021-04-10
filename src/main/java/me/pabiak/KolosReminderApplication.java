package me.pabiak;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KolosReminderApplication {

    public static void main(String[] args) {
        SpringApplication.run(KolosReminderApplication.class, args);
    }

}
