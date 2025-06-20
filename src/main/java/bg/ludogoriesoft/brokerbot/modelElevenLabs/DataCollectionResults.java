package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataCollectionResults {

    @JsonProperty("is_visit_confirmed")
    private IsVisitConfirmed isVisitConfirmed;
}
