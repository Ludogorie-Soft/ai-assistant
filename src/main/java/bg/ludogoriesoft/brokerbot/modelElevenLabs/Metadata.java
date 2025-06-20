package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Metadata {

    @JsonProperty("call_duration_secs")
    private int callDurationSecs;

    @JsonProperty("start_time_unix_secs")
    private Integer startTimeUnixSecs;

    @JsonProperty("phone_call")
    private PhoneCall phoneCall;

}
