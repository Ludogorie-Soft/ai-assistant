package bg.ludogoriesoft.brokerbot.service;

import bg.ludogoriesoft.brokerbot.client.BotClient;
import bg.ludogoriesoft.brokerbot.exception.InvalidPhoneNumberException;
import bg.ludogoriesoft.brokerbot.exception.UserNotAuthenticatedException;
import bg.ludogoriesoft.brokerbot.model.Call;
import bg.ludogoriesoft.brokerbot.model.CallBody;
import bg.ludogoriesoft.brokerbot.model.CallResponse;
import bg.ludogoriesoft.brokerbot.model.Request;
import bg.ludogoriesoft.brokerbot.repository.CallRepository;
import bg.ludogoriesoft.brokerbot.repository.UserRepository;
import bg.ludogoriesoft.brokerbot.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BotService {
    private final BotClient botClient;
    private final CallRepository callRepository;
    private final UserRepository userRepository;
    private final ASyncService aSyncService;

    public ResponseEntity<CallResponse> makeCall(Request request) {
        CallBody callRequestBody = new CallBody();
        if (!request.getPrompt().isEmpty()) {
            callRequestBody.setTask(request.getPrompt());
        }

        callRequestBody.setTask(callRequestBody.getTask() +
                String.format(
                        "Името на брокера, на когото си асистент е %s. В случай, че те питат за" +
                                "имота. Ти предоставям повече информация за него: %s",
                        request.getAssistantName(),
                        request.getPropertyInfo()));
        if (request.getFlexRadio() != null) {
            callRequestBody.setModel(request.getFlexRadio());
        }
        callRequestBody.setPhone_number(formatAsBGPhoneNumber(request.getPhoneNumber()));

        return botClient.makeCall(callRequestBody);
    }

    public Map<String, Object> processCall(Request requestDto, String email) {
        User user = getUserOrThrow(email);

        ResponseEntity<CallResponse> response = makeCall(requestDto);

        aSyncService.waitForCallAndProcess(Objects.requireNonNull(response.getBody()).getCall_id(), user);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("call_id", response.getBody().getCall_id());
        return responseBody;
    }

    public List<Call> getMyCalls(String email) {
        return callRepository.findByUser(getUserOrThrow(email));
    }

    public List<Call> getMySuccessfulCalls(String email){
        return callRepository.findByUserAndIsVisitConfirmedTrue(getUserOrThrow(email));
    }

    public List<Call> getMyUnansweredCalls(String email){
        return callRepository.findUnansweredCallsByUser(getUserOrThrow(email));
    }

    public List<Call> getMyFailedCalls(String email){
        return callRepository.findByUserAndNotConfirmed(getUserOrThrow(email));
    }

    private String formatAsBGPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 9 || !phoneNumber.matches("\\d+")) {
            throw new InvalidPhoneNumberException("Incorrect phone number length");
        }
        return "+359" + phoneNumber;
    }

    private User getUserOrThrow(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotAuthenticatedException("User not authenticated!"));
    }
}