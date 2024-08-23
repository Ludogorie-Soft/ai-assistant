package bg.ludogoriesoft.brokerbot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class CallResponse {
    private String status;
    private String message;
    private String call_id;
    private String batch_id;
}
