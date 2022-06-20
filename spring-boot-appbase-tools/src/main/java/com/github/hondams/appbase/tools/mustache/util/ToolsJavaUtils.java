package com.github.hondams.appbase.tools.mustache.util;

import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ToolsJavaUtils {

    public static void fillMustacheUtils(Map<String, Object> templateContext) {
        templateContext.put("util.toJavaPackageName",
                ToolsLambdaUtils.toLambdaObject(ToolsJavaUtils::toJavaPackageName));
        templateContext.put("util.toJavaSimpleClassName",
                ToolsLambdaUtils.toLambdaObject(ToolsJavaUtils::toJavaSimpleClassName));
    }

    public static String toJavaSimpleClassName(String text) {
        return StringUtils.substringAfter(text, ".");
    }

    public static String toJavaPackageName(String text) {
        return StringUtils.substringBefore(text, ".");
    }
}
