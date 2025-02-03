package bg.ludogoriesoft.brokerbot.controller;

import bg.ludogoriesoft.brokerbot.service.UserService;
import bg.ludogoriesoft.brokerbot.user.User;
import bg.ludogoriesoft.brokerbot.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> registerUser(@ModelAttribute UserDto registerUser) {
        return ResponseEntity.ok(userService.registerUser(registerUser));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> authenticateUser(@ModelAttribute User user) {
        return ResponseEntity.ok(userService.authenticateUser(user));
    }
}
