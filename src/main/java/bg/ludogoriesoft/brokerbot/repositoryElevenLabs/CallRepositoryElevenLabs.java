package bg.ludogoriesoft.brokerbot.repositoryElevenLabs;

import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallElevenLabs;
import bg.ludogoriesoft.brokerbot.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface CallRepositoryElevenLabs extends JpaRepository<CallElevenLabs, Long> {
    List<CallElevenLabs> findByUser(User user);
    List<CallElevenLabs> findByUserAndIsVisitConfirmedTrue(User user);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.user = :user AND c.isVisitConfirmed = false")
    List<CallElevenLabs> findByUserAndNotConfirmed(@Param("user") User user);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.user = :user AND c.isVisitConfirmed IS NULL")
    List<CallElevenLabs> findUnansweredCallsByUser(@Param("user") User user);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.type = 'inbound'")
    List<CallElevenLabs> findInboundCalls();

    @Query("SELECT c FROM CallElevenLabs c WHERE c.convId = :conv_id")
    CallElevenLabs findConversationsByConvId(@Param("conv_id") String conv_id);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.to = :number AND (c.callDate + c.callTime) > :date AND c.isVisitConfirmed IS NULL")
    List<CallElevenLabs> findUnansweredCallsByNumber(@Param("number") String number, @Param("date") LocalDateTime date);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.to = :number AND c.isVisitConfirmed IS NOT NULL ORDER BY c.callDate ASC, c.callTime ASC")
    List<CallElevenLabs> findLastAnsweredCallByNumber(@Param("number") String number);
}
