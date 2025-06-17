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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class BotController {

    private final BotService botService;

    @GetMapping("/bland")
    public String getForm(Model model, HttpSession session, RedirectAttributes redirectAttributes,  @RequestParam(required = false) String clearSuccessLog) {
        String loginError = (String) session.getAttribute("loginError");
        if (loginError != null) {
            model.addAttribute("loginError", "Въведено е невалидно име или парола!");
            session.removeAttribute("loginError");
        }
        if (model.containsAttribute("userNotLoggedIn")) {
            model.addAttribute("showLoginModal", true);
            model.addAttribute("historyAccessDenied", "Трябва да се впишете за да достъпите страницата");
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

    @GetMapping("/history")
    public String getCallHistory(Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            redirectAttributes.addFlashAttribute("userNotLoggedIn", true);
            return "redirect:/";
        }
        String userName = userDetails.getUsername();
        model.addAttribute("allCalls", botService.getMyCalls(userName));
        model.addAttribute("successfulCalls", botService.getMySuccessfulCalls(userName));
        model.addAttribute("unansweredCalls", botService.getMyUnansweredCalls(userName));
        model.addAttribute("failedCalls", botService.getMyFailedCalls(userName));
        return "call-history";
    }
}
