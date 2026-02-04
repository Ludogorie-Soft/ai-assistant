package bg.ludogoriesoft.brokerbot.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class EmptyFeignConfig {
    // Empty configuration to prevent global RequestInterceptor beans from being applied
}
