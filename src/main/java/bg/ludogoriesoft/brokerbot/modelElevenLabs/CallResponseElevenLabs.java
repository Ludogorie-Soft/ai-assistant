package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallResponseElevenLabs {

    private boolean success;
    private String message;
    private String conversation_id;
    private String callSid;
}
