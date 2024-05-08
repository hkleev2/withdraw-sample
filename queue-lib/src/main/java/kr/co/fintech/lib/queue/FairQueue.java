package kr.co.fintech.lib.queue;

import java.util.*;

public class FairQueue<FairKey, Message> implements FintechQueue<FairMessage<FairKey, Message>> {

    private final Map<FairKey, Queue<FairMessage<FairKey, Message>>> queues = new HashMap<>();
    private final List<FairKey> fairKeys = new ArrayList<>();
    private final RecycleInteger recycleInteger = new RecycleInteger();

    @Override
    public synchronized void offer(FairMessage<FairKey, Message> message) {
        FairKey key = message.getKey();

        Queue<FairMessage<FairKey, Message>> q = queues.compute(key, (k, v) -> {
            if (v == null) {
                fairKeys.add(key);
                return new LinkedList<>();
            }
            return v;
        });

        q.add(message);
    }

    @Override
    public synchronized void offer(List<FairMessage<FairKey, Message>> messages) {
        for (FairMessage<FairKey, Message> message : messages) {
            offer(message);
        }
    }

    @Override
    public synchronized FairMessage<FairKey, Message> poll() {
        if (fairKeys.isEmpty()) {
            return null;
        }

        for (int i = 0; i < fairKeys.size(); i++) {
            Queue<FairMessage<FairKey, Message>> messageQueue = getQueue();
            FairMessage<FairKey, Message> message = messageQueue.poll();

            if (message != null) {
                return message;
            }
        }

        return null;
    }

    private Queue<FairMessage<FairKey, Message>> getQueue() {
        FairKey fairKey = fairKeys.get(recycleInteger.getAndIncrement() % fairKeys.size());
        return queues.get(fairKey);
    }

    private static class RecycleInteger {
        private int value;
        public int getAndIncrement() {
            if (value == Integer.MAX_VALUE) {
                value = 0;
            }

            return value++;
        }
    }

}
