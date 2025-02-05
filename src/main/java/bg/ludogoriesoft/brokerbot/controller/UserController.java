package bg.ludogoriesoft.brokerbot.controller;

import bg.ludogoriesoft.brokerbot.service.UserService;
import bg.ludogoriesoft.brokerbot.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserDto registerUser, RedirectAttributes redirectAttributes) {
        return userService.register(registerUser, redirectAttributes);
    }
}
