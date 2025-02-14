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

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Builder
@AllArgsConstructor
@Table(name = "calls")
public class Call {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    private Boolean isVisitConfirmed;

    private String callId;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "to_number")
    private String to;

    private double callLength;
    private LocalDate callDate;
    private LocalTime callTime;

}
