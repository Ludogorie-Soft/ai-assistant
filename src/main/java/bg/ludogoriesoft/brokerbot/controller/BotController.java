package bg.ludogoriesoft.brokerbot.controller;

import bg.ludogoriesoft.brokerbot.model.CallResponse;
import bg.ludogoriesoft.brokerbot.model.Request;
import bg.ludogoriesoft.brokerbot.service.BotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;

    @GetMapping
    public String getForm(Model model){
        model.addAttribute("request", new Request());
        return "form";
    }

    @PostMapping("/call")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> makeCall(@ModelAttribute Request requestDto){
        ResponseEntity<CallResponse> response = botService.makeCall(requestDto);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("call_id", Objects.requireNonNull(response.getBody()).getCall_id());

        return ResponseEntity.ok(responseBody);
    }
}
