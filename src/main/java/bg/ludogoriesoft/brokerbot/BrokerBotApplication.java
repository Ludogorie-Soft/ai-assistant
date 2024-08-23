package bg.ludogoriesoft.brokerbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BrokerBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrokerBotApplication.class, args);
    }

}
