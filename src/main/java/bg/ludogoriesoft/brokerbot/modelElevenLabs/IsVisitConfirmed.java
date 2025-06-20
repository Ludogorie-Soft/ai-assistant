package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IsVisitConfirmed {

    @JsonProperty("value")
    private Boolean isVisitConfirmed;
}
