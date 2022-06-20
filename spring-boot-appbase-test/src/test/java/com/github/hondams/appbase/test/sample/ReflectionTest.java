package com.github.hondams.appbase.test.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import com.github.hondams.appbase.test.sample.mock.TestSamplePrivateMembers;

class ReflectionTest {

    @BeforeEach
    void setUpTestMethod() {
        TestSamplePrivateMembers.clear();
    }

    @Test
    void testStaticField() {
        assertEquals(null, ReflectionTestUtils.getField(TestSamplePrivateMembers.class, "staticField"));
        ReflectionTestUtils.setField(TestSamplePrivateMembers.class, "staticField", "A");
        assertEquals("A", ReflectionTestUtils.getField(TestSamplePrivateMembers.class, "staticField"));
    }

    @Test
    void testInstanceField() {
        TestSamplePrivateMembers target = new TestSamplePrivateMembers();
        assertEquals(null, ReflectionTestUtils.getField(target, "instanceField"));
        ReflectionTestUtils.setField(target, "instanceField", "A");
        assertEquals("A", ReflectionTestUtils.getField(target, "instanceField"));
    }

    @Test
    void testInvokeStaticMethod() {
        ReflectionTestUtils.setField(TestSamplePrivateMembers.class, "staticField", "A");
        assertEquals("A:B",
                ReflectionTestUtils.invokeMethod(TestSamplePrivateMembers.class, "invokeStaticMethod", "B"));
    }

    @Test
    void testInvokeInstanceMethod() {
        TestSamplePrivateMembers target = new TestSamplePrivateMembers();
        ReflectionTestUtils.setField(target, "instanceField", "A");
        assertEquals("A:B", ReflectionTestUtils.invokeMethod(target, "invokeInstanceMethod", "B"));
    }
}
