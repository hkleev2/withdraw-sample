package kr.co.fintech.lib.queue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class BlockingEventLoggingWrapper<Message> {

    private final FintechQueue<Message> messageBroker;

    @Getter
    private final List<EventLog<Message>> eventLogs = new LinkedList<>();

    public BlockingEventLoggingWrapper(FintechQueue<Message> queue) {
        this.messageBroker = queue;
    }

    public synchronized void offer(Message message) {
        messageBroker.offer(message);
        eventLogs.add(new EventLog<>(LocalDateTime.now(), EventLog.EventType.OFFER, message));
    }

    public synchronized void offer(List<Message> messages) {
        messageBroker.offer(messages);

        for (Message message : messages) {
            eventLogs.add(new EventLog<>(LocalDateTime.now(), EventLog.EventType.OFFER, message));
        }
    }

    public synchronized Message poll() {
        Message message = messageBroker.poll();
        eventLogs.add(new EventLog<>(LocalDateTime.now(), EventLog.EventType.POLL, message));
        return message;
    }

}
