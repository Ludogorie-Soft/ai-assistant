package bg.ludogoriesoft.brokerbot.serviceElevenLabs;

import bg.ludogoriesoft.brokerbot.clientElevenLabs.BotClientElevenLabs;
import bg.ludogoriesoft.brokerbot.mapper.CallMapper;
import bg.ludogoriesoft.brokerbot.mapperElevenLabs.CallElevenLabsMapper;
import bg.ludogoriesoft.brokerbot.model.Call;
import bg.ludogoriesoft.brokerbot.model.CallResponse;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallDataResponseElevenLabs;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallElevenLabs;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallResponseElevenLabs;
import bg.ludogoriesoft.brokerbot.repository.CallRepository;
import bg.ludogoriesoft.brokerbot.repositoryElevenLabs.CallRepositoryElevenLabs;
import bg.ludogoriesoft.brokerbot.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ASyncServiceElevenLabs {

    private final BotClientElevenLabs botClientElevenLabs;
    private final CallRepositoryElevenLabs callRepositoryElevenLabs;

    @Async
    public void waitForCallAndProcess(String callId, User user) {
        try {
            Thread.sleep(180000);//180000
            CallDataResponseElevenLabs callDataResponseElevenLabs = getInfoByCallId(callId);//callId
            if (callDataResponseElevenLabs == null) {
                throw new RuntimeException("Call response is null!");
            }
            saveCall(callDataResponseElevenLabs, user);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private CallDataResponseElevenLabs getInfoByCallId(String callId) {
        ResponseEntity<CallDataResponseElevenLabs> callResponseBody = botClientElevenLabs.getCallInfo(callId);
        return callResponseBody.getBody();
    }

    private void saveCall(CallDataResponseElevenLabs callResponseElevenLabs, User user) {
        CallElevenLabs callElevenLabs = CallElevenLabsMapper.toCall(callResponseElevenLabs);
        callElevenLabs.setUser(user);
        if(callResponseElevenLabs.getAnalysis() != null){
            callElevenLabs.setIsVisitConfirmed(callResponseElevenLabs.getAnalysis().getDataCollectionResults().getIsVisitConfirmed().getIsVisitConfirmed());
        }

        callRepositoryElevenLabs.save(callElevenLabs);
    }
}
