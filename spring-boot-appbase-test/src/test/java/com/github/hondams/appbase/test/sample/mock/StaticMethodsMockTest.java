package com.github.hondams.appbase.test.sample.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.hondams.appbase.test.sample.TestSampleException;

@ExtendWith(MockitoExtension.class)
class StaticMethodsMockTest {

    // 呼び出し回数をカウントするオブジェクト。Mockがかかっても、適切にカウントできるようにする。
    private TestSampleStaticMethods.CallInfo callInfo = TestSampleStaticMethods.getCallInfo();

    @Mock
    private MockedStatic<TestSampleStaticMethods> staticMethodsMock;

    @BeforeEach
    void setUpTestMethod() {
        this.callInfo.clear();
    }

    @Test
    @DisplayName("引数なし、戻り値あり")
    void test_invokeStringReturnMethod() {

        AtomicInteger counter = new AtomicInteger();
        this.staticMethodsMock.when(() -> TestSampleStaticMethods.invokeStringReturnMethod())//
                .thenReturn("test1", "test2")//
                .thenCallRealMethod()//
                .thenThrow(new TestSampleException("test-error"))//
                .thenAnswer(invocation -> "test1:" + counter.getAndIncrement())//
                .thenReturn("test3")//
                .thenAnswer(invocation -> "test2:" + counter.getAndIncrement());

        // thenReturnで、戻り値を登録する（可変協引数で、複数回文を伊勝登録できる）。
        // thenCallRealMethodで、元の呼出メソッドを登録する。
        // thenThrowで、例外スローを登録する。
        // thenAnswerで、動的な戻り値を登録する。

        assertEquals(0, this.callInfo.getCallCountOfInvokeStringReturnMethod());

        assertEquals("test1", TestSampleStaticMethods.invokeStringReturnMethod());
        assertEquals("test2", TestSampleStaticMethods.invokeStringReturnMethod());
        // thenCallRealMethod以外は、実際のメソッドを呼ばない。
        assertEquals(0, this.callInfo.getCallCountOfInvokeStringReturnMethod());
        assertEquals("invokeStringReturnMethod:0",
                TestSampleStaticMethods.invokeStringReturnMethod());
        assertEquals(1, this.callInfo.getCallCountOfInvokeStringReturnMethod());
        assertThrows(TestSampleException.class,
                () -> TestSampleStaticMethods.invokeStringReturnMethod());
        assertEquals("test1:0", TestSampleStaticMethods.invokeStringReturnMethod());
        assertEquals("test3", TestSampleStaticMethods.invokeStringReturnMethod());

        // 登録したthen○○より多くメソッドを呼び出した場合、最後に登録されたふるまいとなる。
        assertEquals("test2:1", TestSampleStaticMethods.invokeStringReturnMethod());
        assertEquals("test2:2", TestSampleStaticMethods.invokeStringReturnMethod());

        // 一致する引数で呼ばれた回数
        this.staticMethodsMock.verify(() -> TestSampleStaticMethods.invokeStringReturnMethod(),
                times(8));

        assertEquals(1, this.callInfo.getCallCountOfInvokeStringReturnMethod());
    }

    @Test
    @DisplayName("引数なし、戻り値なし")
    void test_invokeVoidMethod() {

        this.staticMethodsMock.when(() -> TestSampleStaticMethods.invokeVoidMethod())//
                .then(invocation -> null)//
                .thenCallRealMethod()//
                .thenThrow(new TestSampleException("test-error"));

        // thenCallRealMethodで、元の呼出メソッドを登録する。
        // thenThrowで、例外スローを登録する。

        TestSampleStaticMethods.invokeVoidMethod();

        assertEquals(0, this.callInfo.getCallCountOfInvokeVoidMethod());
        TestSampleStaticMethods.invokeVoidMethod();
        assertEquals(1, this.callInfo.getCallCountOfInvokeVoidMethod());
        assertThrows(TestSampleException.class, () -> TestSampleStaticMethods.invokeVoidMethod());

        // 登録したthen○○より多くメソッドを呼び出した場合、最後に登録されたふるまいとなる。
        assertThrows(TestSampleException.class, () -> TestSampleStaticMethods.invokeVoidMethod());

        // 一致する引数で呼ばれた回数
        this.staticMethodsMock.verify(() -> TestSampleStaticMethods.invokeVoidMethod(), times(4));

        assertEquals(1, this.callInfo.getCallCountOfInvokeVoidMethod());
    }

    @Test
    @DisplayName("引数あり、戻り値あり")
    void test_invokeStringReturnMethodByArg() {

        AtomicInteger counter = new AtomicInteger();
        this.staticMethodsMock
                .when(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("A"))//
                .thenReturn("test1", "test2")//
                .thenCallRealMethod()//
                .thenThrow(new TestSampleException("test-error"))//
                .thenAnswer(invocation -> "test1:" + counter.getAndIncrement())//
                .thenReturn("test3")//
                .thenAnswer(invocation -> "test2:" + counter.getAndIncrement());

        // thenReturnで、戻り値を登録する（可変協引数で、複数回文を伊勝登録できる）。
        // thenCallRealMethodで、元の呼出メソッドを登録する。
        // thenThrowで、例外スローを登録する。
        // thenAnswerで、動的な戻り値を登録する。

        assertEquals(0, this.callInfo.getCallCountOfInvokeStringReturnMethodByArg());

        assertEquals("test1", TestSampleStaticMethods.invokeStringReturnMethodByArg("A"));
        assertEquals("test2", TestSampleStaticMethods.invokeStringReturnMethodByArg("A"));
        // thenCallRealMethod以外は、実際のメソッドを呼ばない。
        assertEquals(0, this.callInfo.getCallCountOfInvokeStringReturnMethodByArg());
        assertEquals("invokeStringReturnMethodByArg:0:A",
                TestSampleStaticMethods.invokeStringReturnMethodByArg("A"));
        assertEquals(1, this.callInfo.getCallCountOfInvokeStringReturnMethodByArg());
        assertThrows(TestSampleException.class,
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg("A"));
        assertEquals("test1:0", TestSampleStaticMethods.invokeStringReturnMethodByArg("A"));
        assertEquals("test3", TestSampleStaticMethods.invokeStringReturnMethodByArg("A"));

        // 登録したthen○○より多くメソッドを呼び出した場合、最後に登録されたふるまいとなる。
        assertEquals("test2:1", TestSampleStaticMethods.invokeStringReturnMethodByArg("A"));
        assertEquals("test2:2", TestSampleStaticMethods.invokeStringReturnMethodByArg("A"));

        // 一致する引数で呼ばれた回数
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("A"), times(8));
        // なんらかの引数で呼ばれた回数
        this.staticMethodsMock.verify(
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg(any()), times(8));
        // 一致しない引数で呼ばれた回数
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("B"), times(0));

        assertEquals(1, this.callInfo.getCallCountOfInvokeStringReturnMethodByArg());
    }

    @Test
    @DisplayName("引数あり、戻り値なし")
    void test_invokeVoidMethodByArg() {

        this.staticMethodsMock.when(() -> TestSampleStaticMethods.invokeVoidMethodByArg("A"))//
                .then(invocation -> null)//
                .thenCallRealMethod()//
                .thenThrow(new TestSampleException("test-error"));

        // thenCallRealMethodで、元の呼出メソッドを登録する。
        // thenThrowで、例外スローを登録する。

        TestSampleStaticMethods.invokeVoidMethodByArg("A");

        assertEquals(0, this.callInfo.getCallCountOfInvokeVoidMethodByArg());
        TestSampleStaticMethods.invokeVoidMethodByArg("A");
        assertEquals(1, this.callInfo.getCallCountOfInvokeVoidMethodByArg());
        assertThrows(TestSampleException.class,
                () -> TestSampleStaticMethods.invokeVoidMethodByArg("A"));

        // 登録したthen○○より多くメソッドを呼び出した場合、最後に登録されたふるまいとなる。
        assertThrows(TestSampleException.class,
                () -> TestSampleStaticMethods.invokeVoidMethodByArg("A"));

        // 一致する引数で呼ばれた回数
        this.staticMethodsMock.verify(() -> TestSampleStaticMethods.invokeVoidMethodByArg("A"),
                times(4));
        // なんらかの引数で呼ばれた回数
        this.staticMethodsMock.verify(() -> TestSampleStaticMethods.invokeVoidMethodByArg(any()),
                times(4));
        // 一致しない引数で呼ばれた回数
        this.staticMethodsMock.verify(() -> TestSampleStaticMethods.invokeVoidMethodByArg("B"),
                times(0));

        assertEquals(1, this.callInfo.getCallCountOfInvokeVoidMethodByArg());
    }

    @Test
    @DisplayName("明示的な引数のみを定義した場合、引数に一致する場合のみ、Mockがで適用される。")
    void test_invokeOneArgWithReturn_ArgOnly() {

        this.staticMethodsMock
                .when(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("1"))//
                .thenReturn("test1");
        this.staticMethodsMock
                .when(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("2"))//
                .thenReturn("test2");

        // 引数に対応する戻り値が返却される。
        assertEquals("test1", TestSampleStaticMethods.invokeStringReturnMethodByArg("1"));
        assertEquals("test2", TestSampleStaticMethods.invokeStringReturnMethodByArg("2"));
        // 未設定の引数は、nullとなる。
        assertEquals(null, TestSampleStaticMethods.invokeStringReturnMethodByArg("0"));

    }

    @Test
    @DisplayName("any()の引数のみを定義した場合、すべての呼出でMockがで適用される。")
    void test_invokeStringReturnMethodByArg_AnyOnly() {

        this.staticMethodsMock
                .when(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg(any()))//
                .thenReturn("test1");

        // 引数に対応する戻り値が返却される。
        assertEquals("test1", TestSampleStaticMethods.invokeStringReturnMethodByArg(null));
        assertEquals("test1", TestSampleStaticMethods.invokeStringReturnMethodByArg("1"));
        assertEquals("test1", TestSampleStaticMethods.invokeStringReturnMethodByArg("2"));

        // 一致する引数で呼ばれた回数
        this.staticMethodsMock.verify(
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg(null), times(1));
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("1"), times(1));
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("2"), times(1));
        // なんらかの引数で呼ばれた回数
        this.staticMethodsMock.verify(
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg(any()), times(3));
        // 一致しない引数で呼ばれた回数
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("3"), times(0));
    }

    // any()を登録するなら、明示的な引数を設定すべきでない。
    @Test
    @DisplayName("明示的な引数を定義した後、any()の引数を定義した場合、すべての呼出でany()のふるまいとなるMockがで適用される。")
    void test_invokeStringReturnMethodByArg_ArgAndAny() {

        this.staticMethodsMock
                .when(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("1"))//
                .thenReturn("test1");
        this.staticMethodsMock
                .when(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg(any()))//
                .thenReturn("testAny");

        // あとから、any()の引数を指定すると、any()に対応した戻り値を返却する。
        assertEquals("testAny", TestSampleStaticMethods.invokeStringReturnMethodByArg(null));
        assertEquals("testAny", TestSampleStaticMethods.invokeStringReturnMethodByArg("1"));
        assertEquals("testAny", TestSampleStaticMethods.invokeStringReturnMethodByArg("2"));

        // 一致する引数で呼ばれた回数
        this.staticMethodsMock.verify(
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg(null), times(1));
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("1"), times(1));
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("2"), times(1));
        // なんらかの引数で呼ばれた回数
        this.staticMethodsMock.verify(
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg(any()), times(3));
        // 一致しない引数で呼ばれた回数
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("3"), times(0));
    }

    // any()を登録するなら、明示的な引数を設定すべきでない。
    @Test
    @DisplayName("any()の引数を定義した後、明示的な引数を定義した場合、明示的な引数に一致する場合のみ、それに対応するMockがで適用され、それ以外の呼出でany()のふるまいとなるMockがで適用される。")
    void test_invokeStringReturnMethodByArg_AnyAndArg() {

        this.staticMethodsMock
                .when(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg(any()))//
                .thenReturn("testAny");
        this.staticMethodsMock
                .when(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("1"))//
                .thenReturn("test1");

        // 明示的な引数に対応する戻り値を優先して返却する。
        // 明示的な引数に対応しない場合は、any()の引数に対応した戻り値を返却する。
        assertEquals("testAny", TestSampleStaticMethods.invokeStringReturnMethodByArg(null));
        assertEquals("test1", TestSampleStaticMethods.invokeStringReturnMethodByArg("1"));
        assertEquals("testAny", TestSampleStaticMethods.invokeStringReturnMethodByArg("2"));

        // 一致する引数で呼ばれた回数
        this.staticMethodsMock.verify(
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg(null), times(1));
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("1"), times(1));
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("2"), times(1));
        // なんらかの引数で呼ばれた回数
        this.staticMethodsMock.verify(
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg(any()), times(3));
        // 一致しない引数で呼ばれた回数
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("3"), times(0));
    }

    @Test
    @DisplayName("定義したMockを呼び出さなくても、呼び出し回数のverifyはできる。")
    void test_invokeStringReturnMethodByArg_NoCall() {

        this.staticMethodsMock
                .when(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("1"))//
                .thenReturn("test1");

        // 一致する引数で呼ばれた回数
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("1"), times(0));
        // なんらかの引数で呼ばれた回数
        this.staticMethodsMock.verify(
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg(any()), times(0));
        // 一致しない引数で呼ばれた回数
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("2"), times(0));
    }

    @Test
    @DisplayName("ArgumentCaptorの引数のみを定義した場合、すべての呼出でMockがで適用され、実行後、引数をチェックできる。")
    void test_invokeStringReturnMethodByArg_Captor() {

        ArgumentCaptor<String> arg1Captor = ArgumentCaptor.forClass(String.class);

        this.staticMethodsMock.when(
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg(arg1Captor.capture()))//
                .thenReturn("test1");

        // 引数に対応する戻り値が返却される。
        assertEquals("test1", TestSampleStaticMethods.invokeStringReturnMethodByArg(null));
        assertEquals("test1", TestSampleStaticMethods.invokeStringReturnMethodByArg("1"));
        assertEquals("test1", TestSampleStaticMethods.invokeStringReturnMethodByArg("2"));

        assertEquals(Arrays.asList(null, "1", "2"), arg1Captor.getAllValues());

        // 一致する引数で呼ばれた回数
        this.staticMethodsMock.verify(
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg(null), times(1));
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("1"), times(1));
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("2"), times(1));
        // なんらかの引数で呼ばれた回数
        this.staticMethodsMock.verify(
                () -> TestSampleStaticMethods.invokeStringReturnMethodByArg(any()), times(3));
        // 一致しない引数で呼ばれた回数
        this.staticMethodsMock
                .verify(() -> TestSampleStaticMethods.invokeStringReturnMethodByArg("3"), times(0));
    }

}
