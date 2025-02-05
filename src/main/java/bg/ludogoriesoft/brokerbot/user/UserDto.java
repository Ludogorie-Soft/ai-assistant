package bg.ludogoriesoft.brokerbot.user;

import lombok.Data;

@Data
public class UserDto {
    private String email;
    private String password;
    private String confirmPassword;
}
