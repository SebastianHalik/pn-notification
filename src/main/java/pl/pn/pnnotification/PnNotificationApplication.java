package pl.pn.pnnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"pl.pn.pnnotification", "email"})
public class PnNotificationApplication {


    public static void main(String[] args) {
        SpringApplication.run(PnNotificationApplication.class, args);
    }

}
