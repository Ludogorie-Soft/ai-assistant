package bg.ludogoriesoft.brokerbot.service;

import bg.ludogoriesoft.brokerbot.client.BotClient;
import bg.ludogoriesoft.brokerbot.mapper.CallMapper;
import bg.ludogoriesoft.brokerbot.model.AnalyzeRequest;
import bg.ludogoriesoft.brokerbot.model.AnalyzeResponse;
import bg.ludogoriesoft.brokerbot.model.Call;
import bg.ludogoriesoft.brokerbot.model.CallResponse;
import bg.ludogoriesoft.brokerbot.repository.CallRepository;
import bg.ludogoriesoft.brokerbot.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class ASyncService {

    private final CallRepository callRepository;
    private final BotClient botClient;
    @Async
    public void waitForCallAndProcess(String callId, User user) {
        try {
            Thread.sleep(180000);
            CallResponse callResponse = getInfoByCallId(callId);
            if (callResponse == null) {
                throw new RuntimeException("Call response is null!");
            }
            saveCall(callResponse, user);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private CallResponse getInfoByCallId(String callId) {
        ResponseEntity<CallResponse> callResponseBody = botClient.getCallInfo(callId);
        return callResponseBody.getBody();
    }

    private void saveCall(CallResponse callResponse, User user) {
        Call call = CallMapper.toCall(callResponse);
        call.setUser(user);
        call.setIsVisitConfirmed(getIsVisitConfirmed(call.getCallId()));
        if(callResponse.getPathway_id() != null){
            call.setPathwaySelected(true);
        }
        callRepository.save(call);
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
            result = Objects.requireNonNull(response.getBody()).getAnswers().getFirst();

            if (result == Boolean.TRUE || result == Boolean.FALSE) {
                break;
            }
            attempts++;
        }

/*
       successfulCallExampleId = "525b301e-2fdb-4cb5-85f3-82bd447da3f8";
       unsuccessfulCallExampleId = "bd73b30b-654b-4cb5-b71e-73dc2dff4b2f";
*/
        return result;
    }
}
