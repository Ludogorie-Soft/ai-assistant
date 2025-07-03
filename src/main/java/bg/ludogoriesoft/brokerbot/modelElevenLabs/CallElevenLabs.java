package bg.ludogoriesoft.brokerbot.modelElevenLabs;

import bg.ludogoriesoft.brokerbot.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@Table(name = "calls_eleven_labs")
public class CallElevenLabs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    private String convId;

    private Boolean isVisitConfirmed;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "to_number")
    private String to;

    private double call_duration_secs;

    private LocalDateTime callDateTime;
}
