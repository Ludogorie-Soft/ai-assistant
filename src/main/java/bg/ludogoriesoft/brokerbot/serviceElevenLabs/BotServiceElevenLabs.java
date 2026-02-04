package bg.ludogoriesoft.brokerbot.serviceElevenLabs;

import bg.ludogoriesoft.brokerbot.clientElevenLabs.BotClientElevenLabs;
import bg.ludogoriesoft.brokerbot.exception.InvalidPhoneNumberException;
import bg.ludogoriesoft.brokerbot.exception.UserNotAuthenticatedException;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.*;
import bg.ludogoriesoft.brokerbot.repository.UserRepository;
import bg.ludogoriesoft.brokerbot.repositoryElevenLabs.CallRepositoryElevenLabs;
import bg.ludogoriesoft.brokerbot.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BotServiceElevenLabs {
    private final BotClientElevenLabs botClientElevenLabs;
    private final UserRepository userRepository;
    private final CallRepositoryElevenLabs callRepositoryElevenLabs;
    private final ASyncServiceElevenLabs aSyncServiceElevenLabs;

    @Value("${elevenlabs.agent.id}")
    private String agentId;

    @Value("${elevenlabs.agent.phone.id}")
    private String agentPhoneNumberId;

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

        callBodyElevenLabs.setAgent_id(agentId);
        callBodyElevenLabs.setAgent_phone_number_id(agentPhoneNumberId);
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
        return getLastUnansweredCalls(callRepositoryElevenLabs.findUnansweredCallsByUser(getUserOrThrow(email)), email);
    }

    private List<CallElevenLabs> getLastUnansweredCalls(List<CallElevenLabs> allUnansweredCalls, String email){
        List<CallElevenLabs> lastUnansweredCalls = new ArrayList<>();
        List<String> checkNumbers = new ArrayList<>();

        //check each number from allUnanswered calls for last answered call
        //if don`t have any then add this one to list
        for(CallElevenLabs call : allUnansweredCalls){
            if(!checkNumbers.contains(call.getTo())){
                List<CallElevenLabs> unansweredCalls = getUnansweredConversationsHistoryByNumber(call.getTo(), email);
                if(unansweredCalls == null){
                    //if unanswered calls is null
                    //then add this call to last unanswered
                    lastUnansweredCalls.add(call);
                    break;
                }
                lastUnansweredCalls.addAll(unansweredCalls);
                checkNumbers.add(call.getTo());
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

    public List<CallElevenLabs> getUnansweredConversationsHistoryByNumber(String number, String email){
        CallElevenLabs lastAnsweredCall = getLastTimeWhenNumberIsAnswer(number, email);
        if(lastAnsweredCall != null){
            return callRepositoryElevenLabs.findUnansweredCallsByNumber(number, lastAnsweredCall.getCallDateTime());
        } else {
            return null;
        }
    }

    public List<CallElevenLabs> getConversationHistoryByNumber(String number){
        return callRepositoryElevenLabs.findAllCallsByNumber(number);
    }

    public CallElevenLabs getLastTimeWhenNumberIsAnswer(String number, String email){
        List<CallElevenLabs> calls = callRepositoryElevenLabs.findLastAnsweredCallByNumber(number, getUserOrThrow(email));
        if(!calls.isEmpty()) {
            return calls.getLast();
        } else {
            return null;
        }
    }

}
