package bg.ludogoriesoft.brokerbot.clientElevenLabs;

import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallBodyElevenLabs;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallResponseElevenLabs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "botClientElevenLabs", url = "https://api.elevenlabs.io",
        configuration = CustomRequestInterceptorElevenLabs.class)
public interface BotClientElevenLabs {

    @PostMapping("/v1/convai/twilio/outbound-call")
    ResponseEntity<CallResponseElevenLabs> makeCall(@RequestBody CallBodyElevenLabs callBodyElevenLabs);
}
