package com.github.hondams.appbase.tools.mustache.util;

import org.apache.commons.lang3.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ToolsValueUtils {

    public static boolean toPrimitiveBoolean(String value) {
        switch (value) {
            case "○":
            case "〇":
                return true;
            default:
                return StringUtils.equalsAnyIgnoreCase(value, //
                        "true", "yes", "ｔｒｕｅ", "ｙｅｓ", "Y", "Ｙ");
        }
    }
}
