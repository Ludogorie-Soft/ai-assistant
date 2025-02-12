package bg.ludogoriesoft.brokerbot.service;

import bg.ludogoriesoft.brokerbot.client.BotClient;
import bg.ludogoriesoft.brokerbot.exception.InvalidPhoneNumberException;
import bg.ludogoriesoft.brokerbot.exception.UserNotAuthenticatedException;
import bg.ludogoriesoft.brokerbot.model.AnalyzeRequest;
import bg.ludogoriesoft.brokerbot.model.AnalyzeResponse;
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


@Service
@RequiredArgsConstructor
public class BotService {
    private final BotClient botClient;
    private final CallRepository callRepository;
    private final UserRepository userRepository;

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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotAuthenticatedException("User not authenticated!"));

        ResponseEntity<CallResponse> response = makeCall(requestDto);

        CallResponse callResponse = response.getBody();
        if (callResponse == null) {
            throw new RuntimeException("Call response is null!");
        }

        callRepository.save(CallResponse.builder()
                .status(callResponse.getStatus())
                .message(callResponse.getMessage())
                .call_id(callResponse.getCall_id())
                .batch_id(callResponse.getBatch_id())
                .summary(callResponse.getSummary())
                .user(user)
                .isVisitConfirmed(callResponse.getCallLength() != 0 ?
                        getIsVisitConfirmed(callResponse.getCall_id()) : null)
                .build());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("call_id", callResponse.getCall_id());
        return responseBody;
    }

    private String formatAsBGPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 9) {
            throw new InvalidPhoneNumberException("Incorrect phone number length");
        }
        return "+359" + phoneNumber;
    }

    private Boolean getIsVisitConfirmed(String callId) {
        AnalyzeRequest request = AnalyzeRequest.builder()
                .questions(List.of(List.of("Did the person agree to visit the property?", "boolean")))
                .build();
        Boolean result = null;
        int attempts = 0;

        //This is included because sometimes the bot returns NULL instead of FALSE as result for rejections.
        while (attempts < 3) {
            ResponseEntity<AnalyzeResponse> response = botClient.analyzeCall(callId, request);
            result = response.getBody().getAnswers().getFirst();

            if (result == Boolean.TRUE || result == Boolean.FALSE) {
                break;
            }
            attempts++;
        }

/*
       String successfulCallExampleId = "525b301e-2fdb-4cb5-85f3-82bd447da3f8";
       String unsuccessfulCallExampleId = "bd73b30b-654b-4cb5-b71e-73dc2dff4b2f";
*/
        return result;
    }

}
