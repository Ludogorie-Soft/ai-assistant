package bg.ludogoriesoft.brokerbot.service;

import bg.ludogoriesoft.brokerbot.mapper.UserMapper;
import bg.ludogoriesoft.brokerbot.repository.UserRepository;
import bg.ludogoriesoft.brokerbot.user.User;
import bg.ludogoriesoft.brokerbot.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public String register(UserDto userDto, RedirectAttributes redirectAttributes) {
        if (!userRepository.existsByEmail(userDto.getEmail())) {
            if (userDto.getPassword().equals(userDto.getConfirmPassword())) {
                User user = UserMapper.toEntity(userDto);
                user.setPassword(passwordEncoder.encode(userDto.getPassword()));
                userRepository.save(user);
                redirectAttributes.addFlashAttribute("successReg", "Успешно направена регистрация!");
                return "redirect:/";
            }
            redirectAttributes.addFlashAttribute("nonMatchingPw", "Паролите не съвпадат!");
            return "redirect:/";
        }
        redirectAttributes.addFlashAttribute("existingEmail", "Имейлът е вече регистриран!");
        return "redirect:/";
    }
}
