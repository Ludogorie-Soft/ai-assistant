package bg.ludogoriesoft.brokerbot.service;

import bg.ludogoriesoft.brokerbot.mapper.UserMapper;
import bg.ludogoriesoft.brokerbot.repository.UserRepository;
import bg.ludogoriesoft.brokerbot.user.User;
import bg.ludogoriesoft.brokerbot.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public String registerUser(UserDto userDto) {
        User existingUser = userRepository.findByEmail(userDto.getEmail());
        if (existingUser == null) {
            if (userDto.getPassword().equals(userDto.getConfirmPassword())) {
                userRepository.save(UserMapper.toEntity(userDto));
                return "User created successfully!";
            }
            return "Password doesn't match!";
        }
        return "User with same email already exist!";
    }

    public String authenticateUser(User user) {
        User userFromDb = userRepository.findByEmail(user.getEmail());
        if (userFromDb != null) {
            if (user.getPassword().equals(userFromDb.getPassword())) {
                return "User logged in succesfully!";
            }
            return "Wrong password!";
        }
        return "Wrong email!";
    }
}
