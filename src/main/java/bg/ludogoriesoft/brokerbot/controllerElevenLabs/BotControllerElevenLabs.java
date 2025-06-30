package bg.ludogoriesoft.brokerbot.controllerElevenLabs;

import bg.ludogoriesoft.brokerbot.exception.UserNotAuthenticatedException;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.RequestElevenLabs;
import bg.ludogoriesoft.brokerbot.serviceElevenLabs.BotServiceElevenLabs;
import bg.ludogoriesoft.brokerbot.user.User;
import bg.ludogoriesoft.brokerbot.user.UserDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
//@RequestMapping("/eleven-labs")
public class BotControllerElevenLabs {

    private final BotServiceElevenLabs botServiceElevenLabs; //Eleven++

    @GetMapping
    public String getForm(Model model, HttpSession session, RedirectAttributes redirectAttributes, @RequestParam(required = false) String clearSuccessLog) {
        String loginError = (String) session.getAttribute("loginError");
        if (loginError != null) {
            model.addAttribute("loginError", "Въведено е невалидно име или парола!");
            session.removeAttribute("loginError");
        }
        if (model.containsAttribute("userNotLoggedIn")) {
            model.addAttribute("showLoginModal", true);
            model.addAttribute("historyAccessDenied", "Трябва да се впишете за да достъпите страницата");
        }
        model.addAttribute("request_eleven_labs", new RequestElevenLabs());
        model.addAttribute("user", new User());
        model.addAttribute("registerUser", new UserDto());
        return "form-eleven-labs";
    }

    @PostMapping("/eleven-labs/call")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> makeCall(@ModelAttribute RequestElevenLabs requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new UserNotAuthenticatedException("User not authenticated!");
        }
        Map<String, Object> responseBody = botServiceElevenLabs.processCall(requestDto, userDetails.getUsername());

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/eleven-labs/history")
    public String getCallHistory(Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            redirectAttributes.addFlashAttribute("userNotLoggedIn", true);
            return "redirect:/";
        }
        String userName = userDetails.getUsername();
        model.addAttribute("allCallsElevenLabs", botServiceElevenLabs.getMyCalls(userName));
        model.addAttribute("successfulCallsElevenLabs", botServiceElevenLabs.getMySuccessfulCalls(userName));
        model.addAttribute("unansweredCallsElevenLabs", botServiceElevenLabs.getMyUnansweredCalls(userName));
        model.addAttribute("unsuccessfulCallsElevenLabs", botServiceElevenLabs.getMyUnsuccessfulCalls(userName));
        model.addAttribute("inboundCallsElevenLabs", botServiceElevenLabs.getInboundCalls());
        return "call-history-eleven-labs";
    }

}
