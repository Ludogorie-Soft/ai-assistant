package bg.ludogoriesoft.brokerbot.mapperElevenLabs;

import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallDataResponseElevenLabs;
import bg.ludogoriesoft.brokerbot.modelElevenLabs.CallElevenLabs;

import java.time.*;

public class CallElevenLabsMapper {

    public static CallElevenLabs toCall(CallDataResponseElevenLabs callDataResponseElevenLabs) {
        return CallElevenLabs.builder()
                .conversation_id(callDataResponseElevenLabs.getConversation_id())
                .summary(callDataResponseElevenLabs.getAnalysis().getTranscriptSummary())
                .to(callDataResponseElevenLabs.getMetadata().getPhoneCall().getExternalNumber())
                .call_duration_secs(callDataResponseElevenLabs.getMetadata().getCallDurationSecs())
                .callDate(getCallDate(Instant.ofEpochSecond(callDataResponseElevenLabs.getMetadata().getStartTimeUnixSecs())))
                .callTime(getCallTime(Instant.ofEpochSecond(callDataResponseElevenLabs.getMetadata().getStartTimeUnixSecs())))
                .build();
    }

    private static LocalDate getCallDate(Instant callDateTime) {
        ZoneId zoneId = ZoneId.of("Europe/Sofia");
        LocalDateTime localDateTime = callDateTime.atZone(zoneId).toLocalDateTime();
        return localDateTime.toLocalDate();
    }

    private static LocalTime getCallTime(Instant callDateTime) {
        ZoneId zoneId = ZoneId.of("Europe/Sofia");
        LocalDateTime localDateTime = callDateTime.atZone(zoneId).toLocalDateTime();
        return localDateTime.toLocalTime();
    }
}
