package bg.ludogoriesoft.brokerbot.clientElevenLabs;

import bg.ludogoriesoft.brokerbot.modelElevenLabs.Conversations;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallBodyElevenLabs;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallDataResponseElevenLabs;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallResponseElevenLabs;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "botClientElevenLabs", url = "https://api.elevenlabs.io",
        configuration = CustomRequestInterceptorElevenLabs.class)
public interface BotClientElevenLabs {

    @PostMapping("/v1/convai/twilio/outbound-call")
    ResponseEntity<CallResponseElevenLabs> makeCall(@RequestBody CallBodyElevenLabs callBodyElevenLabs);

    @GetMapping("/v1/convai/conversations/{conv_id}")
    ResponseEntity<CallDataResponseElevenLabs> getCallInfo(@PathVariable("conv_id") String id);

    @GetMapping("/v1/convai/conversations")
    ResponseEntity<Conversations> getAllCallsInfo();
}
