package com.github.hondams.appbase.tools.mustache.util;

import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ToolsLambdaUtils {

    public static final <T, R> Function<T, R> toLambdaObject(Function<T, R> lambda) {
        return lambda;
    }
}
