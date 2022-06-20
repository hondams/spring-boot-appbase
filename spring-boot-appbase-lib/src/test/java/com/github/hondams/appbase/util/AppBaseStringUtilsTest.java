package com.github.hondams.appbase.util;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class AppBaseStringUtilsTest {

    @Test
    void testToLowerSnakeCase() {
        Assertions.assertEquals("", AppBaseStringUtils.toLowerSnakeCase(""));
        Assertions.assertEquals("", AppBaseStringUtils.toLowerSnakeCase("_"));

        testToLowerSnakeCase("abc", "abc");

        testToLowerSnakeCase("abc_def", "abcDef");
        testToLowerSnakeCase("abc_def", "AbcDef");
        testToLowerSnakeCase("abc_def", "ABCDef");

        testToLowerSnakeCase("abc_def", "abc_def");
        testToLowerSnakeCase("abc_def", "ABC_DEF");

        testToLowerSnakeCase("abc_def", "Abc_Def");
        testToLowerSnakeCase("abc_def", "abc_Def");
        testToLowerSnakeCase("abc_def", "Abc_def");

        testToLowerSnakeCase("ab", "ab");
        testToLowerSnakeCase("a_b", "aB");
        testToLowerSnakeCase("ab", "Ab");
        testToLowerSnakeCase("ab", "AB");

        testToLowerSnakeCase("a", "a");
        testToLowerSnakeCase("a", "A");

        testToLowerSnakeCase("a_b", "a_b");
        testToLowerSnakeCase("a_b", "A_b");
        testToLowerSnakeCase("a_b", "a_B");
        testToLowerSnakeCase("a_b", "A_B");
    }

    private void testToLowerSnakeCase(String expected, String text) {
        Assertions.assertEquals(expected, AppBaseStringUtils.toLowerSnakeCase(text));
        Assertions.assertEquals(expected, AppBaseStringUtils.toLowerSnakeCase("_" + text));
        Assertions.assertEquals(expected, AppBaseStringUtils.toLowerSnakeCase(text + "_"));
        Assertions.assertEquals(expected, AppBaseStringUtils.toLowerSnakeCase("_" + text + "_"));
    }

    @Test
    void testToLowerTrainCase() {
        Assertions.assertEquals("", AppBaseStringUtils.toLowerTrainCase(""));
        Assertions.assertEquals("", AppBaseStringUtils.toLowerTrainCase("-"));

        testToLowerTrainCase("abc", "abc");

        testToLowerTrainCase("abc-def", "abcDef");
        testToLowerTrainCase("abc-def", "AbcDef");
        testToLowerTrainCase("abc-def", "ABCDef");

        testToLowerTrainCase("abc-def", "abc_def");
        testToLowerTrainCase("abc-def", "ABC_DEF");

        testToLowerTrainCase("abc-def", "Abc_Def");
        testToLowerTrainCase("abc-def", "abc_Def");
        testToLowerTrainCase("abc-def", "Abc_def");

        testToLowerTrainCase("ab", "ab");
        testToLowerTrainCase("a-b", "aB");
        testToLowerTrainCase("ab", "Ab");
        testToLowerTrainCase("ab", "AB");

        testToLowerTrainCase("a", "a");
        testToLowerTrainCase("a", "A");

        testToLowerTrainCase("a-b", "a_b");
        testToLowerTrainCase("a-b", "A_b");
        testToLowerTrainCase("a-b", "a_B");
        testToLowerTrainCase("a-b", "A_B");
    }

    private void testToLowerTrainCase(String expected, String text) {
        Assertions.assertEquals(expected, AppBaseStringUtils.toLowerTrainCase(text));
        Assertions.assertEquals(expected, AppBaseStringUtils.toLowerTrainCase("-" + text));
        Assertions.assertEquals(expected, AppBaseStringUtils.toLowerTrainCase(text + "-"));
        Assertions.assertEquals(expected, AppBaseStringUtils.toLowerTrainCase("-" + text + "_"));
    }

    @Test
    void testSplitWords() {
        testSplitWords(List.of("a"), "a");
        testSplitWords(List.of("A"), "A");
        testSplitWords(List.of("1"), "1");
        testSplitWords(List.of(), "_");

        testSplitWords(List.of("aa"), "aa");
        testSplitWords(List.of("a", "A"), "aA");
        testSplitWords(List.of("a1"), "a1");

        testSplitWords(List.of("Aa"), "Aa");
        testSplitWords(List.of("AA"), "AA");
        testSplitWords(List.of("A1"), "A1");

        testSplitWords(List.of("1", "a"), "1a");
        testSplitWords(List.of("1", "A"), "1A");
        testSplitWords(List.of("11"), "11");

        testSplitWords(List.of("a", "a"), "a_a");
        testSplitWords(List.of("a", "A"), "a_A");
        testSplitWords(List.of("a", "1"), "a_1");

        testSplitWords(List.of("A", "a"), "A_a");
        testSplitWords(List.of("A", "A"), "A_A");
        testSplitWords(List.of("A", "1"), "A_1");

        testSplitWords(List.of("1", "a"), "1_a");
        testSplitWords(List.of("1", "A"), "1_A");
        testSplitWords(List.of("1", "1"), "1_1");

        testSplitWords(List.of("aaa"), "aaa");
        testSplitWords(List.of("aa", "A"), "aaA");
        testSplitWords(List.of("aa1"), "aa1");

        testSplitWords(List.of("a", "Aa"), "aAa");
        testSplitWords(List.of("a", "AA"), "aAA");
        testSplitWords(List.of("a", "A1"), "aA1");

        testSplitWords(List.of("a1a"), "a1a");
        testSplitWords(List.of("a1", "A"), "a1A");
        testSplitWords(List.of("a11"), "a11");

        testSplitWords(List.of("Aaa"), "Aaa");
        testSplitWords(List.of("Aa", "A"), "AaA");
        testSplitWords(List.of("Aa1"), "Aa1");

        testSplitWords(List.of("A", "Aa"), "AAa");
        testSplitWords(List.of("AAA"), "AAA");
        testSplitWords(List.of("AA1"), "AA1");

        testSplitWords(List.of("A1a"), "A1a");
        testSplitWords(List.of("A1A"), "A1A");
        testSplitWords(List.of("A11"), "A11");

        testSplitWords(List.of("1", "aa"), "1aa");
        testSplitWords(List.of("1", "a", "A"), "1aA");
        testSplitWords(List.of("1", "a1"), "1a1");

        testSplitWords(List.of("1", "Aa"), "1Aa");
        testSplitWords(List.of("1", "AA"), "1AA");
        testSplitWords(List.of("1", "A1"), "1A1");

        testSplitWords(List.of("11", "a"), "11a");
        testSplitWords(List.of("11", "A"), "11A");
        testSplitWords(List.of("111"), "111");

        testSplitWords(List.of("Aaa", "Bbb"), "AaaBbb");
        testSplitWords(List.of("Aaa", "BBB"), "AaaBBB");
        testSplitWords(List.of("Aaa111"), "Aaa111");

        testSplitWords(List.of("AAA", "Bbb"), "AAABbb");
        testSplitWords(List.of("AAABBB"), "AAABBB");
        testSplitWords(List.of("AAA111"), "AAA111");

        testSplitWords(List.of("111", "bbb"), "111bbb");
        testSplitWords(List.of("111", "Bbb"), "111Bbb");
        testSplitWords(List.of("111", "BBB"), "111BBB");
        testSplitWords(List.of("111222"), "111222");
    }

    private void testSplitWords(List<String> expected, String text) {
        Assertions.assertIterableEquals(expected, //
                ReflectionTestUtils.invokeMethod(//
                        AppBaseStringUtils.class, "splitWords", text));
        Assertions.assertIterableEquals(expected, //
                ReflectionTestUtils.invokeMethod(//
                        AppBaseStringUtils.class, "splitWords", "-" + text));
        Assertions.assertIterableEquals(expected, //
                ReflectionTestUtils.invokeMethod(//
                        AppBaseStringUtils.class, "splitWords", text + "-"));
        Assertions.assertIterableEquals(expected, //
                ReflectionTestUtils.invokeMethod(//
                        AppBaseStringUtils.class, "splitWords", "-" + text + "-"));
    }
}
