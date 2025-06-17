package bg.ludogoriesoft.brokerbot.clientElevenLabs;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

//@Configuration
public class CustomRequestInterceptorElevenLabs{

    @Value("${eleven.labs.encrypted.key}")
    private String token;

//    @Override
//    public void apply(RequestTemplate requestTemplate) {
//        requestTemplate.header("Xi-Api-Key", token);
//        requestTemplate.header("Content-Type", "application/json");
//        requestTemplate.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
//    }

    @Bean
    public RequestInterceptor requestInterceptorElevenLab() {

        return requestTemplate ->  {
            requestTemplate.header("Xi-Api-Key", token);
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

        };
    }
}
