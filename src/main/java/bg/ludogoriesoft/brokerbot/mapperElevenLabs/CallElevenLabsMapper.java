package bg.ludogoriesoft.brokerbot.mapperElevenLabs;

import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallDataResponseElevenLabs;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallElevenLabs;

import java.time.*;

public class CallElevenLabsMapper {

    public static CallElevenLabs toCall(CallDataResponseElevenLabs callDataResponseElevenLabs) {
        return CallElevenLabs.builder()
                .convId(callDataResponseElevenLabs.getConversation_id())
                .summary(getSummary(callDataResponseElevenLabs))
                .to(callDataResponseElevenLabs.getMetadata().getPhoneCall().getExternalNumber())
                .type(callDataResponseElevenLabs.getMetadata().getPhoneCall().getDirection())
                .call_duration_secs(callDataResponseElevenLabs.getMetadata().getCallDurationSecs())
                .callDateTime(getCallDateTime(Instant.ofEpochSecond(callDataResponseElevenLabs.getMetadata().getStartTimeUnixSecs())))
                .build();
    }

    private static String getSummary(CallDataResponseElevenLabs callDataResponseElevenLabs){
        if(callDataResponseElevenLabs.getAnalysis() != null){
            return callDataResponseElevenLabs.getAnalysis().getTranscriptSummary();
        }
        return null;
    }

        private static LocalDateTime getCallDateTime(Instant callDateTime) {
        ZoneId zoneId = ZoneId.of("Europe/Sofia");
        return callDateTime.atZone(zoneId).toLocalDateTime();
    }
}
