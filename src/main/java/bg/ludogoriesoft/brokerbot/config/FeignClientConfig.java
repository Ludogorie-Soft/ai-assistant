package bg.ludogoriesoft.brokerbot.config;

import feign.Client;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    @Bean
    public Client feignClient() {
        return new ApacheHttpClient(
                HttpClients.custom()
                        .setRedirectStrategy(new LaxRedirectStrategy()) // follows redirects like 307
                        .build()
        );
    }
}
