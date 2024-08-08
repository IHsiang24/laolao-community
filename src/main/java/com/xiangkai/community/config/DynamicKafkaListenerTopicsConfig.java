package com.xiangkai.community.config;

import com.xiangkai.community.annotation.EnumValues2Topics;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class DynamicKafkaListenerTopicsConfig {

    @PostConstruct
    public void dynamicKafkaListenerTopics() {
        String eventPackageName = "com.xiangkai.community.event";
        List<Class<?>> classes = scanClasses(eventPackageName);

        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                EnumValues2Topics enumValues2Topics = method.getAnnotation(EnumValues2Topics.class);
                if (enumValues2Topics == null) {
                    continue;
                }
                String[] enumValues = getEnumValues(enumValues2Topics.value(), enumValues2Topics.method());
                KafkaListener kafkaListener = method.getAnnotation(KafkaListener.class);
                setEnumValues(kafkaListener, enumValues);
            }
        }
    }

    private String[] getEnumValues(Class<? extends Enum<?>> enumClass, String methodName) {
        try {
            Method method = enumClass.getDeclaredMethod(methodName);
            Enum<?>[] enumConstants = enumClass.getEnumConstants();
            return Arrays.stream(enumConstants)
                    .map(enumConstant -> {
                                try {
                                    method.setAccessible(true);
                                    return method.invoke(enumConstant).toString();
                                } catch (IllegalAccessException | InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    )
                    .toArray(String[]::new);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Method " + methodName + " not found in enum " + enumClass.getName());
        }
    }

    @SuppressWarnings("unchecked")
    private void setEnumValues(Object object, String[] values) {
        try {
            if (Proxy.isProxyClass(object.getClass())) {
                InvocationHandler invocationHandler = Proxy.getInvocationHandler(object);
                setEnumValues(invocationHandler, values);
                return;
            }
            Class<?> clazz = object.getClass();
            Field field = clazz.getDeclaredField("memberValues");
            field.setAccessible(true);
            Object o = field.get(object);
            if (o instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) o;
                map.put("topics", values);
            }
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Class<?>> scanClasses(String packageName) {
        ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(packageName)
                .scan();

        return scanResult.getAllClasses().loadClasses();
    }
}
