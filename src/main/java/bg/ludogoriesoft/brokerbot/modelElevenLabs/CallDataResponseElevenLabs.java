package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.LinkedHashMap;

@Getter
@Setter
public class CallDataResponseElevenLabs {

    @JsonProperty("conversation_id")
    private String conversation_id;

    @JsonProperty("status")
    private String status;

    @JsonProperty("metadata")
    private Metadata metadata;

    @JsonProperty("analysis")
    private Analysis analysis;
}
