package bg.ludogoriesoft.brokerbot.serviceElevenLabs;

import bg.ludogoriesoft.brokerbot.mapperElevenLabs.CallElevenLabsMapper;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallElevenLabs;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.Conversations;
import bg.ludogoriesoft.brokerbot.clientElevenLabs.BotClientElevenLabs;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallDataResponseElevenLabs;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.ConversationData;
import bg.ludogoriesoft.brokerbot.repositoryElevenLabs.CallRepositoryElevenLabs;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class ASyncElevenLabsInboundCallsData {

    private final BotClientElevenLabs botClientElevenLabs;
    private final CallRepositoryElevenLabs callRepositoryElevenLabs;

    @Async
    @Scheduled(fixedRate = 1800000)//1 hour 3 600 000  // 30 min  1 800 000
    public void getAllInboundCalls(){
        Conversations conversations = getConversationsDataList();
        for(ConversationData conversationData : conversations.getConversationsData()){
            if(conversationData.getStatus().equals("done") || conversationData.getStatus().equals("initiated")){
                if(findConversationInDatabaseById(conversationData.getConversationId()) == null){
                    getInboundCallInfoAndSave(conversationData);
                }
            }
        }
    }

    private void getInboundCallInfoAndSave(ConversationData conversationsData){
        CallDataResponseElevenLabs callDataResponseElevenLabs = getCallInfoById(conversationsData.getConversationId());

        if(callDataResponseElevenLabs.getMetadata().getPhoneCall().getDirection().equals("inbound")){
            saveCall(callDataResponseElevenLabs);
        }
    }

    private CallElevenLabs findConversationInDatabaseById(String convId){
        return callRepositoryElevenLabs.findConversationsByConvId(convId);
    }

    private Conversations getConversationsDataList(){
        ResponseEntity<Conversations> conversationsDataResponse = botClientElevenLabs.getAllCallsInfo();
        return conversationsDataResponse.getBody();
    }

    private CallDataResponseElevenLabs getCallInfoById(String callId) {
        ResponseEntity<CallDataResponseElevenLabs> callResponseBody = botClientElevenLabs.getCallInfo(callId);
        return callResponseBody.getBody();
    }

    protected void saveCall(CallDataResponseElevenLabs callResponseElevenLabs) {
        CallElevenLabs callElevenLabs = CallElevenLabsMapper.toCall(callResponseElevenLabs);
        if(callResponseElevenLabs.getAnalysis() != null){
            callElevenLabs.setIsVisitConfirmed(callResponseElevenLabs.getAnalysis().getDataCollectionResults().getIsVisitConfirmed().getIsVisitConfirmed());
        }

        callRepositoryElevenLabs.save(callElevenLabs);
    }
}
