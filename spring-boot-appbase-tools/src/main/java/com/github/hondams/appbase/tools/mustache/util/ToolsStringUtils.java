package com.github.hondams.appbase.tools.mustache.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ToolsStringUtils {

    public static void fillMustacheUtils(Map<String, Object> templateContext) {
        templateContext.put("util.toUpperCase",
                ToolsLambdaUtils.toLambdaObject(ToolsStringUtils::toUpperCase));
        templateContext.put("util.toLowerCase",
                ToolsLambdaUtils.toLambdaObject(ToolsStringUtils::toLowerCase));
        templateContext.put("util.toLowerSnakeCase",
                ToolsLambdaUtils.toLambdaObject(ToolsStringUtils::toLowerSnakeCase));
        templateContext.put("util.toUpperSnakeCase",
                ToolsLambdaUtils.toLambdaObject(ToolsStringUtils::toUpperSnakeCase));
        templateContext.put("util.toLowerTrainCase",
                ToolsLambdaUtils.toLambdaObject(ToolsStringUtils::toLowerTrainCase));
        templateContext.put("util.toUpperTrainCase",
                ToolsLambdaUtils.toLambdaObject(ToolsStringUtils::toUpperTrainCase));
        templateContext.put("util.toLowerCamelCase",
                ToolsLambdaUtils.toLambdaObject(ToolsStringUtils::toLowerCamelCase));
        templateContext.put("util.toUpperCamelCase",
                ToolsLambdaUtils.toLambdaObject(ToolsStringUtils::toUpperCamelCase));
    }

    public static String toUpperCase(String text) {
        return text.toUpperCase();
    }

    public static String toLowerCase(String text) {
        return text.toLowerCase();
    }

    public static String toLowerSnakeCase(String text) {
        if (ToolsStringUtils.isEmpty(text) || //
                ToolsStringUtils.isLowerSeparatingCase(text, '_')) {
            return text;
        }
        return String.join("_", ToolsStringUtils.splitLowerWords(text));
    }

    public static String toLowerTrainCase(String text) {
        if (ToolsStringUtils.isEmpty(text) || //
                ToolsStringUtils.isLowerSeparatingCase(text, '-')) {
            return text;
        }
        return String.join("-", ToolsStringUtils.splitLowerWords(text));
    }

    public static String toUpperSnakeCase(String text) {
        if (ToolsStringUtils.isEmpty(text) || //
                ToolsStringUtils.isUpperSeparatingCase(text, '_')) {
            return text;
        }
        return String.join("_", ToolsStringUtils.splitUpperWords(text));
    }

    public static String toUpperTrainCase(String text) {
        if (ToolsStringUtils.isEmpty(text) || //
                ToolsStringUtils.isUpperSeparatingCase(text, '-')) {
            return text;
        }
        return String.join("-", ToolsStringUtils.splitUpperWords(text));
    }

    public static String toLowerCamelCase(String text) {
        if (ToolsStringUtils.isEmpty(text) || //
                ToolsStringUtils.isLowerCamelCase(text)) {
            return text;
        }
        return ToolsStringUtils.toLowerCamelCaseFromWords(ToolsStringUtils.splitLowerWords(text));
    }

    public static String toUpperCamelCase(String text) {
        if (ToolsStringUtils.isEmpty(text) || //
                ToolsStringUtils.isUpperCamelCase(text)) {
            return text;
        }
        return ToolsStringUtils.toUpperCamelCaseFromWords(ToolsStringUtils.splitLowerWords(text));
    }

    private static final boolean isEmpty(String text) {
        return text == null || text.length() == 0;
    }

    private static final boolean isLowerCamelCase(String text) {
        if (!ToolsStringUtils.isLowerAlphabet(text.charAt(0))) {
            return false;
        }
        for (int i = 1; i < text.length(); i++) {
            if (!ToolsStringUtils.isAlphabetOrNumber(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static final boolean isUpperCamelCase(String text) {
        if (!ToolsStringUtils.isUpperAlphabet(text.charAt(0))) {
            return false;
        }
        for (int i = 1; i < text.length(); i++) {
            if (!ToolsStringUtils.isAlphabetOrNumber(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static final boolean isLowerSeparatingCase(String text, char separator) {
        if (!ToolsStringUtils.isLowerAlphabet(text.charAt(0))) {
            return false;
        }
        int lastIndex = text.length() - 1;
        for (int i = 1; i < lastIndex; i++) {
            if (!ToolsStringUtils.isLowerAlphabetOrNumberOrSeparator(text.charAt(i), separator)) {
                return false;
            }
        }
        if (!ToolsStringUtils.isLowerAlphabet(text.charAt(lastIndex))) {
            return false;
        }
        return true;
    }

    private static final boolean isUpperSeparatingCase(String text, char separator) {
        if (!ToolsStringUtils.isUpperAlphabet(text.charAt(0))) {
            return false;
        }
        int lastIndex = text.length() - 1;
        for (int i = 1; i < lastIndex; i++) {
            if (!ToolsStringUtils.isUpperAlphabetOrNumberOrSeparator(text.charAt(i), separator)) {
                return false;
            }
        }
        if (!ToolsStringUtils.isUpperAlphabet(text.charAt(lastIndex))) {
            return false;
        }
        return true;
    }

    private static final boolean isLowerAlphabet(char ch) {
        return 'a' <= ch && ch <= 'z';
    }

    private static final boolean isUpperAlphabet(char ch) {
        return 'A' <= ch && ch <= 'Z';
    }

    private static final boolean isNumber(char ch) {
        return '0' <= ch && ch <= '9';
    }

    private static final boolean isLowerAlphabetOrNumberOrSeparator(char ch, char separator) {
        return ToolsStringUtils.isLowerAlphabet(ch) || ToolsStringUtils.isNumber(ch)
                || ch == separator;
    }

    private static final boolean isUpperAlphabetOrNumberOrSeparator(char ch, char separator) {
        return ToolsStringUtils.isUpperAlphabet(ch) || ToolsStringUtils.isNumber(ch)
                || ch == separator;
    }

    private static final boolean isAlphabetOrNumber(char ch) {
        return ToolsStringUtils.isLowerAlphabet(ch) || ToolsStringUtils.isUpperAlphabet(ch)
                || ToolsStringUtils.isNumber(ch);
    }

    private static final String toLowerCamelCaseFromWords(List<String> words) {
        StringBuilder newText = new StringBuilder();
        for (String word : words) {
            newText.append(Character.isUpperCase(word.charAt(0)));
            for (int i = 1; i < word.length(); i++) {
                newText.append(word.charAt(i));
            }
        }
        return newText.toString();
    }

    private static final String toUpperCamelCaseFromWords(List<String> words) {
        StringBuilder newText = new StringBuilder();
        boolean first = true;
        for (String word : words) {
            if (first) {
                newText.append(word.charAt(0));
                first = false;
            } else {
                newText.append(Character.isUpperCase(word.charAt(0)));
            }
            for (int i = 1; i < word.length(); i++) {
                newText.append(word.charAt(i));
            }
        }
        return newText.toString();
    }

    private static List<String> splitLowerWords(String text) {
        List<String> words = ToolsStringUtils.splitWords(text);
        for (int i = 0; i < words.size(); i++) {
            words.set(i, words.get(i).toLowerCase());
        }
        return words;
    }

    private static List<String> splitUpperWords(String text) {
        List<String> words = ToolsStringUtils.splitWords(text);
        for (int i = 0; i < words.size(); i++) {
            words.set(i, words.get(i).toUpperCase());
        }
        return words;
    }

    private static List<String> splitWords(String text) {
        List<String> words = new ArrayList<>();

        int wordStartIndex = -1;
        SplitWordType splitWordType = null;

        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ToolsStringUtils.isLowerAlphabet(ch)) {
                if (wordStartIndex == -1) {
                    wordStartIndex = i;
                    splitWordType = SplitWordType.ANY;
                } else {
                    switch (splitWordType) {
                        case FIRST_UPPER_ALPHABET:
                            splitWordType = SplitWordType.ANY;
                            break;
                        case ALL_UPPER_ALPHABET: {
                            words.add(text.substring(wordStartIndex, i - 1));
                            wordStartIndex = i - 1;
                            splitWordType = SplitWordType.ANY;
                            break;
                        }
                        case ALL_NUMBER: {
                            words.add(text.substring(wordStartIndex, i));
                            wordStartIndex = i;
                            splitWordType = SplitWordType.ANY;
                            break;
                        }
                        case ANY:
                        default:
                            break;
                    }
                }
            } else if (ToolsStringUtils.isUpperAlphabet(ch)) {
                if (wordStartIndex == -1) {
                    wordStartIndex = i;
                    splitWordType = SplitWordType.FIRST_UPPER_ALPHABET;
                } else {
                    switch (splitWordType) {
                        case FIRST_UPPER_ALPHABET:
                            splitWordType = SplitWordType.ALL_UPPER_ALPHABET;
                            break;
                        case ALL_NUMBER:
                        case ANY: {
                            words.add(text.substring(wordStartIndex, i));
                            wordStartIndex = i;
                            splitWordType = SplitWordType.FIRST_UPPER_ALPHABET;
                            break;
                        }
                        case ALL_UPPER_ALPHABET:
                        default:
                            break;
                    }
                }
            } else if (ToolsStringUtils.isNumber(ch)) {
                if (wordStartIndex == -1) {
                    wordStartIndex = i;
                    splitWordType = SplitWordType.ALL_NUMBER;
                } else {
                    switch (splitWordType) {
                        case FIRST_UPPER_ALPHABET:
                        case ALL_NUMBER:
                        case ALL_UPPER_ALPHABET:
                        case ANY:
                        default:
                            break;
                    }
                }
            } else {
                if (wordStartIndex != -1) {
                    words.add(text.substring(wordStartIndex, i));
                    wordStartIndex = -1;
                    splitWordType = null;
                }
            }
        }

        if (wordStartIndex != -1) {
            words.add(text.substring(wordStartIndex));
        }

        return words;
    }

    private enum SplitWordType {
        FIRST_UPPER_ALPHABET, //
        ALL_NUMBER, //
        ALL_UPPER_ALPHABET, //
        ANY//
    }
}
