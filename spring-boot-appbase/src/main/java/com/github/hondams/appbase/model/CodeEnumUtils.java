package com.github.hondams.appbase.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeEnumUtils {

    private static final Map<Class<?>, Map<String, CodeEnum>> codeEnumMapMap =
            new ConcurrentHashMap<>();

    public static <T extends CodeEnum> T getCodeEnum(Class<T> codeEnumType, String code) {
        T codeEnum = getCodeEnumMap(codeEnumType).get(code);
        return codeEnum;
    }

    public static <T extends CodeEnum> Map<String, T> getCodeEnumMap(Class<T> codeEnumType) {
        Map<String, CodeEnum> codeEnumMap = codeEnumMapMap.get(codeEnumType);
        if (codeEnumMap == null) {
            codeEnumMap = createCodeEnumMap(codeEnumType);
            codeEnumMapMap.put(codeEnumType, codeEnumMap);
        }
        @SuppressWarnings("unchecked")
        Map<String, T> map = (Map<String, T>) codeEnumMap;
        return map;
    }

    private static <T extends CodeEnum> Map<String, CodeEnum> createCodeEnumMap(
            Class<T> codeEnumType) {
        Map<String, CodeEnum> codeEnumMap = new LinkedHashMap<>();
        for (T codeEnum : codeEnumType.getEnumConstants()) {
            codeEnumMap.put(codeEnum.getCode(), codeEnum);
        }
        return Collections.unmodifiableMap(codeEnumMap);
    }
}
