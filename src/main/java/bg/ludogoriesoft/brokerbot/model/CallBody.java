package bg.ludogoriesoft.brokerbot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CallBody {
    private String phone_number;
    private String from = "+3539870781";
    private String task;
    private String pathway_id = "4f891401-2191-41b2-84ab-5e4bb40555b2";
    private String start_node_id;
    private String voice = "nat";
    private String first_sentence;
    private Boolean block_interruption;
    private Integer interruption_threshold;
    private String model = "base";
    private String language = "bg";
    private Integer max_duration = 12;
    private Boolean answered_by_enabled = false;
    private Boolean wait_for_greeting = false;
    private Boolean record = false;
}