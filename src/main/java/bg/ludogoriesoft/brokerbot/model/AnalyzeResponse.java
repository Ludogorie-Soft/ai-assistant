package bg.ludogoriesoft.brokerbot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnalyzeResponse {
    private String status;
    private String message;
    private List<Boolean> answers;
    private List<List<String>> questions;
    private double creditsUsed;
}
