package kr.co.fintech.lib.queue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@RequiredArgsConstructor
public class EventLog<Message> {
    private final LocalDateTime localDateTime;
    private final EventType eventType;
    private final Message message;

    public enum EventType {
        OFFER, POLL
    }
}
