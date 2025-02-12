package bg.ludogoriesoft.brokerbot.controller;

import bg.ludogoriesoft.brokerbot.exception.UserNotAuthenticatedException;
import bg.ludogoriesoft.brokerbot.model.Request;
import bg.ludogoriesoft.brokerbot.service.BotService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;

    @GetMapping
    public String getForm(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String loginError = (String) session.getAttribute("loginError");
        if (loginError != null) {
            model.addAttribute("loginError", "Въведено е невалидно име или парола!");
            session.removeAttribute("loginError");
        }
        String loggedIn = (String) session.getAttribute("loggedIn");
        if (loggedIn != null) {
            redirectAttributes.addFlashAttribute("successLog", "Успешно вписване!");
        }
        model.addAttribute("request", new Request());
        model.addAttribute("user", new User());
        model.addAttribute("registerUser", new UserDto());
        return "form";
    }

    @PostMapping("/call")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> makeCall(@ModelAttribute Request requestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new UserNotAuthenticatedException("User not authenticated!");
        }

        Map<String, Object> responseBody = botService.processCall(requestDto, userDetails.getUsername());

        return ResponseEntity.ok(responseBody);
    }
}
