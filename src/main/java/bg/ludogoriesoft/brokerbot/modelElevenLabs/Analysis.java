package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Analysis {

    @JsonProperty("transcript_summary")
    private String transcriptSummary;

    @JsonProperty("data_collection_results")
    private DataCollectionResults dataCollectionResults;
}
