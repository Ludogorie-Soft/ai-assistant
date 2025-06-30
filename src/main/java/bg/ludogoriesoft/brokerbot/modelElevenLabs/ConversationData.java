package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConversationData {

    @JsonProperty("conversation_id")
    private String conversationId;

    @JsonProperty("status")
    private String status;
}
