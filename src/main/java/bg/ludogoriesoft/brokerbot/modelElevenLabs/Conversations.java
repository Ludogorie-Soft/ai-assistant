package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Conversations {

    @JsonProperty("conversations")
    private List<ConversationData> conversationsData;
}
