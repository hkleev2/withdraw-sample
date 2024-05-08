package kr.co.fintech.lib.queue;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = { "key", "value" })
public class FairMessage<K, V> {

    private final K key;

    private final V value;

    public static <K, V> FairMessage<K, V> of(K fairKey, V message) {
        return new FairMessage<>(fairKey, message);
    }

}
