package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhoneCall {

    @JsonProperty("external_number")
    private String externalNumber;
}
