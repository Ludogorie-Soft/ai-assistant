package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Agent {
    public Prompt prompt;
    public String first_message;
}
