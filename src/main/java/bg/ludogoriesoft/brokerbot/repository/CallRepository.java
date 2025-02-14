package bg.ludogoriesoft.brokerbot.repository;

import bg.ludogoriesoft.brokerbot.model.Call;
import bg.ludogoriesoft.brokerbot.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CallRepository extends JpaRepository<Call, Long> {
    List<Call> findByUser(User user);
    List<Call> findByUserAndIsVisitConfirmedTrue(User user);

    @Query("SELECT c FROM Call c WHERE c.user = :user AND c.isVisitConfirmed = false")
    List<Call> findByUserAndNotConfirmed(@Param("user") User user);

    @Query("SELECT c FROM Call c WHERE c.user = :user AND c.isVisitConfirmed IS NULL")
    List<Call> findUnansweredCallsByUser(@Param("user") User user);

}