package bg.ludogoriesoft.brokerbot.client;

import bg.ludogoriesoft.brokerbot.model.AnalyzeRequest;
import bg.ludogoriesoft.brokerbot.model.AnalyzeResponse;
import bg.ludogoriesoft.brokerbot.model.CallBody;
import bg.ludogoriesoft.brokerbot.model.CallResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Component
@FeignClient(name = "bot-client", url = "https://api.bland.ai",
        configuration = CustomRequestInterceptor.class)
public interface BotClient {

    @PostMapping("/v1/calls")
    ResponseEntity<CallResponse> makeCall(@RequestBody CallBody callBody);

    @GetMapping("/v1/calls/{id}")
    ResponseEntity<CallResponse> getCallInfo(@PathVariable("id") String id);

    @PostMapping("/v1/calls/{id}/analyze")
    ResponseEntity<AnalyzeResponse> analyzeCall(@PathVariable String id,
                                                @RequestBody AnalyzeRequest questions);
}
