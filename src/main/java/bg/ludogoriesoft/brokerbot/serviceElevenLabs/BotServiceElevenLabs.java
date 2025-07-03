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

import java.time.LocalDateTime;
import java.util.*;

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
        return getLastUnansweredCalls(callRepositoryElevenLabs.findUnansweredCallsByUser(getUserOrThrow(email)));
    }

    private List<CallElevenLabs> getLastUnansweredCalls(List<CallElevenLabs> allUnansweredCalls){
        List<CallElevenLabs> lastUnansweredCalls = new ArrayList<>();
        List<String> checkNumbers = new ArrayList<>();
        for(CallElevenLabs calls : allUnansweredCalls){
            if(!checkNumbers.contains(calls.getTo())){
                lastUnansweredCalls.addAll(getUnansweredConversationsHistoryByNumber(calls.getTo()));
                checkNumbers.add(calls.getTo());
            }
        }
        return lastUnansweredCalls;
    }

    public List<CallElevenLabs> getMyUnsuccessfulCalls(String email) {
        return callRepositoryElevenLabs.findByUserAndNotConfirmed(getUserOrThrow(email));
    }

    public List<CallElevenLabs> getInboundCalls(){
        return callRepositoryElevenLabs.findInboundCalls();
    }

    public List<CallElevenLabs> getUnansweredConversationsHistoryByNumber(String number){
        CallElevenLabs lastAnsweredCall = getLastTimeWhenNumberIsAnswer(number);
        return callRepositoryElevenLabs.findUnansweredCallsByNumber(number, lastAnsweredCall.getCallDateTime());
    }

    public List<CallElevenLabs> getConversationHistoryByNumber(String number){
        return callRepositoryElevenLabs.findAllCallsByNumber(number);
    }

    public CallElevenLabs getLastTimeWhenNumberIsAnswer(String number){
        return callRepositoryElevenLabs.findLastAnsweredCallByNumber(number).getLast();
    }

}
