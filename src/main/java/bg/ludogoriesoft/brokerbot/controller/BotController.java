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
    public String makeCall(Model model, @ModelAttribute Request requestDto){
        ResponseEntity<CallResponse> response = botService.makeCall(requestDto);

        model.addAttribute("call_id", Objects.requireNonNull(response.getBody()).getCall_id());
        return "call-response";
    }
}
