package bg.ludogoriesoft.brokerbot.repositoryElevenLabs;

import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallElevenLabs;
import bg.ludogoriesoft.brokerbot.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CallRepositoryElevenLabs extends JpaRepository<CallElevenLabs, Long> {
    @Query("SELECT c FROM CallElevenLabs c WHERE c.user = :user ORDER BY c.callDateTime DESC")
    List<CallElevenLabs> findByUser(User user);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.user = :user AND c.isVisitConfirmed = true ORDER BY c.callDateTime DESC")
    List<CallElevenLabs> findByUserAndIsVisitConfirmedTrue(User user);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.user = :user AND c.isVisitConfirmed = false ORDER BY c.callDateTime DESC")
    List<CallElevenLabs> findByUserAndNotConfirmed(@Param("user") User user);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.user = :user AND c.isVisitConfirmed IS NULL ORDER BY c.callDateTime DESC")
    List<CallElevenLabs> findUnansweredCallsByUser(@Param("user") User user);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.type = 'inbound' ORDER BY c.callDateTime DESC")
    List<CallElevenLabs> findInboundCalls();

    @Query("SELECT c FROM CallElevenLabs c WHERE c.convId = :conv_id")
    CallElevenLabs findConversationsByConvId(@Param("conv_id") String conv_id);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.to = :number AND c.callDateTime > :date AND c.isVisitConfirmed IS NULL")
    List<CallElevenLabs> findUnansweredCallsByNumber(@Param("number") String number, @Param("date") LocalDateTime date);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.to = :number ORDER BY c.callDateTime DESC")
    List<CallElevenLabs> findAllCallsByNumber(@Param("number") String number);

    @Query("SELECT c FROM CallElevenLabs c WHERE c.to = :number AND c.isVisitConfirmed IS NOT NULL ORDER BY c.callDateTime ASC")
    List<CallElevenLabs> findLastAnsweredCallByNumber(@Param("number") String number);
}
