package com.github.hondams.appbase.test.sample;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BeforeAfterTest {

    @BeforeAll
    static void setUpTestClass() {
        System.out.println("setUpTestClass");
    }

    @BeforeEach
    void setUpTestMethod() {
        System.out.println("setUpTestMethod");
    }

    @AfterEach
    void tearDownTestMethod() {
        System.out.println("tearDownTestMethod");
    }

    @AfterAll
    static void tearDownTestClass() {
        System.out.println("tearDownTestClass");
    }

    @Test
    void test1() {
        System.out.println("test1");
        assertTrue(true);
    }

    @Test
    void test2() {
        System.out.println("test2");
        assertTrue(true);
    }

}
