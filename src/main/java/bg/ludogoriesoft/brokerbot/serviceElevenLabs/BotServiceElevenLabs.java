package bg.ludogoriesoft.brokerbot.serviceElevenLabs;

import bg.ludogoriesoft.brokerbot.clientElevenLabs.BotClientElevenLabs;
import bg.ludogoriesoft.brokerbot.exception.InvalidPhoneNumberException;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BotServiceElevenLabs {
    private final BotClientElevenLabs botClientElevenLabs;

    public Map<String, Object> processCall(RequestElevenLabs requestDto) {

        ResponseEntity<CallResponseElevenLabs> response = makeCall(requestDto);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("call_id", response.getBody().getConversation_id());
        return responseBody;
    }

    public ResponseEntity<CallResponseElevenLabs> makeCall(RequestElevenLabs requestElevenLabs) {
        CallBodyElevenLabs callBodyElevenLabs = new CallBodyElevenLabs();

        callBodyElevenLabs.setTo_number(formatAsBGPhoneNumber(requestElevenLabs.getPhoneNumber()));
        //Get Prompt
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
}
