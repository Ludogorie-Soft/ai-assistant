package bg.ludogoriesoft.brokerbot.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class CustomRequestInterceptor{

    @Bean(name = "blandAiRequestInterceptor")
    public RequestInterceptor requestInterceptor(Environment environment) {
        String token = environment.getProperty("bot.authorization.token");
        String twilioKey = environment.getProperty("twilio.encrypted.key");

        return requestTemplate ->  {
            // CRITICAL: Skip entirely if this is an ElevenLabs request
            String url = requestTemplate.url();
            if (url == null || url.contains("elevenlabs.io")) {
                return; // Don't add any headers for ElevenLabs requests
            }
            
            // Only apply this interceptor to Bland AI API requests
            if (url.contains("bland.ai")) {
                // Double-check: don't add if Xi-Api-Key is already present
                if (!requestTemplate.headers().containsKey("Xi-Api-Key")) {
                    requestTemplate.header("Authorization", token);
                    requestTemplate.header("encrypted_key", twilioKey);
                    requestTemplate.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
                }
            }
        };
    }
}