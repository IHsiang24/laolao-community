package com.xiangkai.community.util;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
@SuppressWarnings("all")
public class BloomFilterUtil {
    private static final int EXPECTED_INSERTIONS = 1000000;  // 预计插入元素数量，根据实际情况调整
    private static final double FPP = 0.01;  // 误判率，可按需调整
    private BloomFilter<String> bloomFilter;

    @PostConstruct
    public void init() {
        bloomFilter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()),
                EXPECTED_INSERTIONS, FPP);

        // todo 从数据库导入数据进行布隆插入
    }

    public void put(String key) {
        bloomFilter.put(key);
    }

    public boolean mightContain(String key) {
        return bloomFilter.mightContain(key);
    }

    public List<String> getFalsePositiveKeys(List<String> allKeys) {
        List<String> falsePositiveKeys = new ArrayList<>();
        for (String key : allKeys) {
            if (mightContain(key) &&!actualContains(key)) {
                falsePositiveKeys.add(key);
            }
        }
        return falsePositiveKeys;
    }

    private boolean actualContains(String key) {
        // 这里可以添加实际从数据源（如数据库）检查元素是否存在的逻辑，此处暂省略
        return false;
    }
}
