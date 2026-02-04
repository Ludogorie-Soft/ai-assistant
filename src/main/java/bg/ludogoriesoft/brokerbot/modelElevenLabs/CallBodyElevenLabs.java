package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CallBodyElevenLabs {

    private String agent_id;
    private String agent_phone_number_id;
    private String to_number;
    public ConversationInitiationClientData conversation_initiation_client_data;
}
