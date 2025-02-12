package bg.ludogoriesoft.brokerbot.model;

import bg.ludogoriesoft.brokerbot.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@Table(name = "call_response")
public class CallResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    private String status;
    private double callLength;
    private String message;
    private String call_id;
    private String batch_id;
    private String summary;

    private Boolean isVisitConfirmed;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;
}
