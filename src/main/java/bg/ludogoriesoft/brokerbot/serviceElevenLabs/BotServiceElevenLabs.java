package bg.ludogoriesoft.brokerbot.serviceElevenLabs;

import bg.ludogoriesoft.brokerbot.clientElevenLabs.BotClientElevenLabs;
import bg.ludogoriesoft.brokerbot.exception.InvalidPhoneNumberException;
import bg.ludogoriesoft.brokerbot.exception.UserNotAuthenticatedException;
import bg.ludogoriesoft.brokerbot.model.Call;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.*;
import bg.ludogoriesoft.brokerbot.repository.UserRepository;
import bg.ludogoriesoft.brokerbot.repositoryElevenLabs.CallRepositoryElevenLabs;
import bg.ludogoriesoft.brokerbot.service.ASyncService;
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
public class BotServiceElevenLabs {
    private final BotClientElevenLabs botClientElevenLabs;
    private final UserRepository userRepository;
    private final CallRepositoryElevenLabs callRepositoryElevenLabs;
    private final ASyncServiceElevenLabs aSyncServiceElevenLabs;

    public Map<String, Object> processCall(RequestElevenLabs requestDto, String email) {
        User user = getUserOrThrow(email);
        ResponseEntity<CallResponseElevenLabs> response = makeCall(requestDto);

        aSyncServiceElevenLabs.waitForCallAndProcess(Objects.requireNonNull(response.getBody()).getConversation_id(), user);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("call_id", response.getBody().getConversation_id());
        return responseBody;
    }

    public ResponseEntity<CallResponseElevenLabs> makeCall(RequestElevenLabs requestElevenLabs) {
        CallBodyElevenLabs callBodyElevenLabs = new CallBodyElevenLabs();

        callBodyElevenLabs.setTo_number(formatAsBGPhoneNumber(requestElevenLabs.getPhoneNumber()));
        //Get Prompt  and first message
        callBodyElevenLabs.setConversation_initiation_client_data(
                new ConversationInitiationClientData(
                        new ConversationConfigOverride(
                                new Agent(
                                        new Prompt(requestElevenLabs.getPrompt()), requestElevenLabs.getFirstMessage()))));

        return botClientElevenLabs.makeCall(callBodyElevenLabs);
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

    public List<CallElevenLabs> getMyCalls(String email) {
        return callRepositoryElevenLabs.findByUser(getUserOrThrow(email));
    }

    public List<CallElevenLabs> getMySuccessfulCalls(String email) {
        return callRepositoryElevenLabs.findByUserAndIsVisitConfirmedTrue(getUserOrThrow(email));
    }

    public List<CallElevenLabs> getMyUnansweredCalls(String email) {
        return callRepositoryElevenLabs.findUnansweredCallsByUser(getUserOrThrow(email));
    }

    public List<CallElevenLabs> getMyUnsuccessfulCalls(String email) {
        return callRepositoryElevenLabs.findByUserAndNotConfirmed(getUserOrThrow(email));
    }

    public List<CallElevenLabs> getInboundCalls(){
        return callRepositoryElevenLabs.findInboundCalls();
    }

}
