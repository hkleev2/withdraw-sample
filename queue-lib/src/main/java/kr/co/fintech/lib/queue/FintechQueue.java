package kr.co.fintech.lib.queue;

import java.util.List;

public interface FintechQueue<Message> {

    void offer(Message message);

    void offer(List<Message> messages);

    /**
     * @return Nullable
     */
    Message poll();
}
