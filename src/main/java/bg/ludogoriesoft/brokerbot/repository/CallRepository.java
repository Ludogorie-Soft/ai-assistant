package bg.ludogoriesoft.brokerbot.repository;

import bg.ludogoriesoft.brokerbot.model.CallResponse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<CallResponse, Long> {
}