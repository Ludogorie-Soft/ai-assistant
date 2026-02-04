package bg.ludogoriesoft.brokerbot.clientElevenLabs;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Collection;
import java.util.Map;

@Configuration
public class CustomRequestInterceptorElevenLabs {

    @Bean(name = "elevenLabsRequestInterceptor")
    public RequestInterceptor requestInterceptor(Environment environment) {
        String token = environment.getProperty("eleven.labs.encrypted.key", "");
        
        return requestTemplate -> {
            // Get the headers map and directly remove Authorization
            Map<String, Collection<String>> headers = requestTemplate.headers();
            headers.remove("Authorization");
            
            // Add ElevenLabs-specific headers
            if (token != null && !token.isEmpty()) {
                requestTemplate.header("Xi-Api-Key", token);
            }
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        };
    }
}
