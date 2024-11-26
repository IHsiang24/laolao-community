package com.xiangkai.community.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class HotKeyDetector {

    private static final Logger logger = LoggerFactory.getLogger(HotKeyDetector.class);

    private static final int WINDOW_SIZE_SECONDS = 60;

    private static final int MAX_KEYS_TO_TRACK = 10;

    private final Map<String, Integer> keyAccessCounts = new HashMap<>();

    private final Map<String, Long> lastAccessTimes = new HashMap<>();

    private volatile long windowStartTime = System.currentTimeMillis();

    public void recordAccess(String key) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - windowStartTime > TimeUnit.SECONDS.toMillis(WINDOW_SIZE_SECONDS)) {
            // 滑动窗口，清除旧的访问记录
            keyAccessCounts.entrySet().removeIf(entry -> {
                long lastAccessTime = getLastAccessTime(entry.getKey());
                return lastAccessTime < windowStartTime;
            });
            windowStartTime = currentTime;
        }
        keyAccessCounts.put(key, keyAccessCounts.getOrDefault(key, 0) + 1);
        setLastAccessTime(key, currentTime);
    }

    public boolean isHotKey(String key) {
        HashMap<String, Integer> hotKeyMap = keyAccessCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(MAX_KEYS_TO_TRACK)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        HashMap::new
                ));

        return hotKeyMap.containsKey(key);
    }

    private Long getLastAccessTime(String key) {
        // 假设使用一个额外的有序集合来记录每个key的最后访问时间
        return lastAccessTimes.getOrDefault(key, 0L);
    }

    private void setLastAccessTime(String key, Long lastAccessTime) {
        // 假设使用一个额外的有序集合来记录每个key的最后访问时间
        lastAccessTimes.put(key, lastAccessTime);
    }
}
