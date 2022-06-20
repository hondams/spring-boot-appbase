package com.github.hondams.appbase.test.sample.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doCallRealMethod;
import java.util.regex.Pattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DoCallRealMethodTest {

    private static int callCount;
    private static String log;

    @BeforeEach
    void setUpTestMethod() {
        log = null;
        callCount = 0;
    }


    @Mock
    private SomeInterface someInterfaceMock;

    @Test
    void test_someInterfaceMock() {
        // インターフェイスで作ったモックは、doCallRealMethod()で、実メソッドの実行を登録できない。
        assertThrowsExactly(MockitoException.class, () -> {
            doCallRealMethod().when(this.someInterfaceMock).someMethod();
        });
        // doCallRealMethod().when(this.someInterfaceMock).someMethod();
        // this.someInterfaceMock.someMethod();
        // assertEquals("", log);
    }


    @Mock
    private SomeInterfaceHavingDefault someInterfaceHavingDefaultMock;

    @Test
    void test_someInterfaceHavingDefaultMock() {
        doCallRealMethod().when(this.someInterfaceHavingDefaultMock).someMethod();
        this.someInterfaceHavingDefaultMock.someMethod();
        assertTrue(Pattern.matches(
                "DoCallRealMethodTest\\$SomeInterfaceHavingDefault\\$MockitoMock\\$\\d+\\{\\}:1",
                log));
    }


    @Mock
    private SomeAbstractClass someAbstractClassMock;

    @Test
    void test_someAbstractClassMock() {
        doCallRealMethod().when(this.someAbstractClassMock).someMethod();
        this.someAbstractClassMock.someMethod();
        assertTrue(Pattern.matches(
                "DoCallRealMethodTest\\$SomeAbstractClass\\$MockitoMock\\$\\d+\\{\\}:1", log));
    }


    @Mock
    private SomeClass someClassMock;

    @Test
    void test_someClassMock() {
        doCallRealMethod().when(this.someClassMock).someMethod();
        this.someClassMock.someMethod();
        assertEquals("SomeClass{}:1", log);
    }


    @Mock
    private SomeClassWithouNoArgConstructor someClassWithouNoArgConstructorMock;

    @Test
    void test_someClassWithouNoArgConstructorMock() {
        doCallRealMethod().when(this.someClassWithouNoArgConstructorMock).someMethod();
        this.someClassWithouNoArgConstructorMock.someMethod();
        assertEquals("SomeClassWithouNoArgConstructor{someField=0}:1", log);
    }


    @Mock
    private SomeClassWithTwoConstructor someClassWithTwoConstructorMock;

    @Test
    void test_someClassWithTwoConstructorMock() {
        doCallRealMethod().when(this.someClassWithTwoConstructorMock).someMethod();
        this.someClassWithTwoConstructorMock.someMethod();
        assertEquals("SomeClassWithTwoConstructor{someField1=0, someField2=0}:1", log);
    }


    @Spy
    private SomeInterface someInterfaceSpy;

    @Test
    void test_someInterfaceSpy() {
        // インターフェイスで作ったモックは、doCallRealMethod()で、実メソッドの実行を登録できない。
        assertThrowsExactly(MockitoException.class, () -> {
            doCallRealMethod().when(this.someInterfaceSpy).someMethod();
        });
        // doCallRealMethod().when(this.someInterfaceSpy).someMethod();
        // this.someInterfaceSpy.someMethod();
        // assertEquals("", log);
    }


    @Spy
    private SomeInterfaceHavingDefault someInterfaceHavingDefaultSpy;

    @Test
    void test_someInterfaceHavingDefaultSpy() {
        doCallRealMethod().when(this.someInterfaceHavingDefaultSpy).someMethod();
        this.someInterfaceHavingDefaultSpy.someMethod();
        assertTrue(Pattern.matches(
                "DoCallRealMethodTest\\$SomeInterfaceHavingDefault\\$MockitoMock\\$\\d+\\{\\}:1",
                log));
    }


    @Spy
    private SomeAbstractClass someAbstractClassSpy;

    @Test
    void test_someAbstractClassSpy() {
        doCallRealMethod().when(this.someAbstractClassSpy).someMethod();
        this.someAbstractClassSpy.someMethod();
        assertTrue(Pattern.matches(
                "DoCallRealMethodTest\\$SomeAbstractClass\\$MockitoMock\\$\\d+\\{\\}:1", log));
    }


    @Spy
    private SomeClass someClassSpy;

    @Test
    void test_someClassSpy() {
        doCallRealMethod().when(this.someClassSpy).someMethod();
        this.someClassSpy.someMethod();
        assertEquals("SomeClass{}:1", log);
    }

    // 引数なしデフォルトコンストラクタないと、@Spyの未初期化変数は定義できない。
    // @Spy
    // private SomeClassWithouNoArgConstructor someClassWithouNoArgConstructorSpy;

    @Spy
    private SomeClassWithouNoArgConstructor someClassWithouNoArgConstructorSpy =
            new SomeClassWithouNoArgConstructor(10);

    @Test
    void test_someClassWithouNoArgConstructorSpy() {
        doCallRealMethod().when(this.someClassWithouNoArgConstructorSpy).someMethod();
        this.someClassWithouNoArgConstructorSpy.someMethod();
        assertEquals("SomeClassWithouNoArgConstructor{someField=10}:1", log);
    }

    // 引数なしデフォルトコンストラクタないと、@Spyの未初期化変数は定義できない。
    // @Spy
    // private SomeClassWithTwoConstructor someClassWithTwoConstructorSpy;

    @Spy
    private SomeClassWithTwoConstructor someClassWithTwoConstructorSpy =
            new SomeClassWithTwoConstructor(10, 20);

    @Test
    void test_someClassWithTwoConstructorSpy() {
        doCallRealMethod().when(this.someClassWithTwoConstructorSpy).someMethod();
        this.someClassWithTwoConstructorSpy.someMethod();
        assertEquals("SomeClassWithTwoConstructor{someField1=10, someField2=20}:1", log);
    }


    public interface SomeInterface {

        void someMethod();
    }

    public interface SomeInterfaceHavingDefault {
        default void someMethod() {
            callCount++;
            log = getClass().getSimpleName() + "{}:" + callCount;
        }
    }

    public static abstract class SomeAbstractClass {
        public void someMethod() {
            callCount++;
            log = getClass().getSimpleName() + "{}:" + callCount;
        }
    }

    public static class SomeClass {
        public void someMethod() {
            callCount++;
            log = getClass().getSimpleName() + "{}:" + callCount;
        }
    }

    public static class SomeClassWithouNoArgConstructor {

        private int someField = -1;

        public SomeClassWithouNoArgConstructor(int someField) {
            this.someField = someField;
        }

        public void someMethod() {
            callCount++;
            log = getClass().getSimpleName() + "{someField=" + this.someField + "}:" + callCount;
        }
    }

    public static class SomeClassWithTwoConstructor {

        private int someField1 = -1;

        private int someField2 = -1;

        public SomeClassWithTwoConstructor(int someField1) {
            this.someField1 = someField1;
            this.someField1 = -2;
        }

        public SomeClassWithTwoConstructor(int someField1, int someField2) {
            this.someField1 = someField1;
            this.someField2 = someField2;
        }

        public void someMethod() {
            callCount++;
            log = getClass().getSimpleName() + "{someField1=" + this.someField1 + ", someField2="
                    + this.someField2 + "}:" + callCount;
        }
    }
}
