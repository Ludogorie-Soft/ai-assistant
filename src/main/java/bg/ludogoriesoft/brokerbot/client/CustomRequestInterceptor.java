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
        requestTemplate.header("Authorization", token);
    }
}