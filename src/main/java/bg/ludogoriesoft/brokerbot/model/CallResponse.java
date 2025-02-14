package bg.ludogoriesoft.brokerbot.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CallResponse {

    private String call_id;

    @JsonProperty("created_at")
    private Instant created_at;

    @JsonProperty("call_length")
    private double call_length;

    @JsonProperty("to")
    private String to;

    @JsonProperty("summary")
    private String summary;

}
