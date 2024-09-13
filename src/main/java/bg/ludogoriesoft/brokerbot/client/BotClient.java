package bg.ludogoriesoft.brokerbot.client;

import bg.ludogoriesoft.brokerbot.model.CallBody;
import bg.ludogoriesoft.brokerbot.model.CallResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Component
@FeignClient(name = "bot-client", url = "https://api.bland.ai",
        configuration = {CustomRequestInterceptor.class, FeignProxyConfig.class})
public interface BotClient {

    @RequestMapping(method = RequestMethod.POST, value = "/v1/calls")
    ResponseEntity<CallResponse> makeCall(@RequestBody CallBody callBody);


}
