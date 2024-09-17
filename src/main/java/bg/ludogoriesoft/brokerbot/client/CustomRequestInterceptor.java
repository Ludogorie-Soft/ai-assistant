package bg.ludogoriesoft.brokerbot.client;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomRequestInterceptor implements RequestInterceptor {

    @Value("${bot.authorization.token}")
    private String token;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        System.out.println("Token ---->>>> " + token);  // Temporary log to check if token is being injected
        requestTemplate.header("Authorization", token);
        requestTemplate.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
    }
}