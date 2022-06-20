package com.github.hondams.appbase.test.sample.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.hondams.appbase.test.sample.TestSampleException;

@ExtendWith(MockitoExtension.class)
class InstanceMethodsMockTest {

    // 呼び出し回数をカウントするオブジェクト。Mockがかかっても、適切にカウントできるようにする。
    private TestSampleInstanceMethods.CallInfo callInfo = TestSampleInstanceMethods.getCallInfo();

    @Mock
    private TestSampleInstanceMethods instanceMethods;

    @BeforeEach
    void setUpTestMethod() {
        this.callInfo.clear();
    }

    @Test
    @DisplayName("引数なし、戻り値あり")
    void test_invokeStringReturnMethod() {

        AtomicInteger counter = new AtomicInteger();

        doReturn("test1", "test2")//
                .doCallRealMethod()//
                .doThrow(new TestSampleException("test-error"))//
                .doAnswer(invocation -> "test1:" + counter.getAndIncrement())//
                .doReturn("test3")//
                .doAnswer(invocation -> "test2:" + counter.getAndIncrement())//
                .when(this.instanceMethods).invokeStringReturnMethod();

        // doReturnで、戻り値を登録する（可変協引数で、複数回文を伊勝登録できる）。
        // doCallRealMethodで、元の呼出メソッドを登録する。
        // doThrowで、例外スローを登録する。
        // doAnswerで、動的な戻り値を登録する。

        assertEquals(0, this.callInfo.getCallCountOfInvokeStringReturnMethod());

        assertEquals("test1", this.instanceMethods.invokeStringReturnMethod());
        assertEquals("test2", this.instanceMethods.invokeStringReturnMethod());
        // doCallRealMethod以外は、実際のメソッドを呼ばない。
        assertEquals(0, this.callInfo.getCallCountOfInvokeStringReturnMethod());
        assertEquals("invokeStringReturnMethod:0", this.instanceMethods.invokeStringReturnMethod());
        assertEquals(1, this.callInfo.getCallCountOfInvokeStringReturnMethod());
        assertThrows(TestSampleException.class,
                () -> this.instanceMethods.invokeStringReturnMethod());
        assertEquals("test1:0", this.instanceMethods.invokeStringReturnMethod());
        assertEquals("test3", this.instanceMethods.invokeStringReturnMethod());

        // 登録したdo○○より多くメソッドを呼び出した場合、最後に登録されたふるまいとなる。
        assertEquals("test2:1", this.instanceMethods.invokeStringReturnMethod());
        assertEquals("test2:2", this.instanceMethods.invokeStringReturnMethod());

        // 一致する引数で呼ばれた回数
        verify(this.instanceMethods, times(8)).invokeStringReturnMethod();

        assertEquals(1, this.callInfo.getCallCountOfInvokeStringReturnMethod());
    }

    @Test
    @DisplayName("引数なし、戻り値なし")
    void test_invokeVoidMethod() {

        doNothing()//
                .doCallRealMethod()//
                .doThrow(new TestSampleException("test-error"))//
                .when(this.instanceMethods).invokeVoidMethod();

        // thenCallRealMethodで、元の呼出メソッドを登録する。
        // thenThrowで、例外スローを登録する。

        this.instanceMethods.invokeVoidMethod();

        assertEquals(0, this.callInfo.getCallCountOfInvokeVoidMethod());
        this.instanceMethods.invokeVoidMethod();
        assertEquals(1, this.callInfo.getCallCountOfInvokeVoidMethod());
        assertThrows(TestSampleException.class, () -> this.instanceMethods.invokeVoidMethod());

        // 登録したthen○○より多くメソッドを呼び出した場合、最後に登録されたふるまいとなる。
        assertThrows(TestSampleException.class, () -> this.instanceMethods.invokeVoidMethod());

        // 一致する引数で呼ばれた回数
        verify(this.instanceMethods, times(4)).invokeVoidMethod();

        assertEquals(1, this.callInfo.getCallCountOfInvokeVoidMethod());
    }

    @Test
    @DisplayName("引数あり、戻り値あり")
    void test_invokeStringReturnMethodByArg() {

        AtomicInteger counter = new AtomicInteger();
        doReturn("test1", "test2")//
                .doCallRealMethod()//
                .doThrow(new TestSampleException("test-error"))//
                .doAnswer(invocation -> "test1:" + counter.getAndIncrement())//
                .doReturn("test3")//
                .doAnswer(invocation -> "test2:" + counter.getAndIncrement())//
                .when(this.instanceMethods).invokeStringReturnMethodByArg("A");

        // thenReturnで、戻り値を登録する（可変協引数で、複数回文を伊勝登録できる）。
        // thenCallRealMethodで、元の呼出メソッドを登録する。
        // thenThrowで、例外スローを登録する。
        // thenAnswerで、動的な戻り値を登録する。

        assertEquals(0, this.callInfo.getCallCountOfInvokeStringReturnMethodByArg());

        assertEquals("test1", this.instanceMethods.invokeStringReturnMethodByArg("A"));
        assertEquals("test2", this.instanceMethods.invokeStringReturnMethodByArg("A"));
        // thenCallRealMethod以外は、実際のメソッドを呼ばない。
        assertEquals(0, this.callInfo.getCallCountOfInvokeStringReturnMethodByArg());
        assertEquals("invokeStringReturnMethodByArg:0:A",
                this.instanceMethods.invokeStringReturnMethodByArg("A"));
        assertEquals(1, this.callInfo.getCallCountOfInvokeStringReturnMethodByArg());
        assertThrows(TestSampleException.class,
                () -> this.instanceMethods.invokeStringReturnMethodByArg("A"));
        assertEquals("test1:0", this.instanceMethods.invokeStringReturnMethodByArg("A"));
        assertEquals("test3", this.instanceMethods.invokeStringReturnMethodByArg("A"));

        // 登録したthen○○より多くメソッドを呼び出した場合、最後に登録されたふるまいとなる。
        assertEquals("test2:1", this.instanceMethods.invokeStringReturnMethodByArg("A"));
        assertEquals("test2:2", this.instanceMethods.invokeStringReturnMethodByArg("A"));

        // 一致する引数で呼ばれた回数
        verify(this.instanceMethods, times(8)).invokeStringReturnMethodByArg("A");
        // なんらかの引数で呼ばれた回数
        verify(this.instanceMethods, times(8)).invokeStringReturnMethodByArg(any());
        // 一致しない引数で呼ばれた回数
        verify(this.instanceMethods, times(0)).invokeStringReturnMethodByArg("B");

        assertEquals(1, this.callInfo.getCallCountOfInvokeStringReturnMethodByArg());
    }

    @Test
    @DisplayName("引数あり、戻り値なし")
    void test_invokeVoidMethodByArg() {

        doNothing()//
                .doCallRealMethod()//
                .doThrow(new TestSampleException("test-error"))//
                .when(this.instanceMethods).invokeVoidMethodByArg("A");

        // thenCallRealMethodで、元の呼出メソッドを登録する。
        // thenThrowで、例外スローを登録する。

        this.instanceMethods.invokeVoidMethodByArg("A");

        assertEquals(0, this.callInfo.getCallCountOfInvokeVoidMethodByArg());
        this.instanceMethods.invokeVoidMethodByArg("A");
        assertEquals(1, this.callInfo.getCallCountOfInvokeVoidMethodByArg());
        assertThrows(TestSampleException.class,
                () -> this.instanceMethods.invokeVoidMethodByArg("A"));

        // 登録したthen○○より多くメソッドを呼び出した場合、最後に登録されたふるまいとなる。
        assertThrows(TestSampleException.class,
                () -> this.instanceMethods.invokeVoidMethodByArg("A"));

        // 一致する引数で呼ばれた回数
        verify(this.instanceMethods, times(4)).invokeVoidMethodByArg("A");
        // なんらかの引数で呼ばれた回数
        verify(this.instanceMethods, times(4)).invokeVoidMethodByArg(any());
        // 一致しない引数で呼ばれた回数
        verify(this.instanceMethods, times(0)).invokeVoidMethodByArg("B");

        assertEquals(1, this.callInfo.getCallCountOfInvokeVoidMethodByArg());
    }

    @Test
    @DisplayName("明示的な引数のみを定義した場合、引数に一致する場合のみ、Mockがで適用される。")
    void test_invokeStringReturnMethodByArg_ArgOnly() {

        doReturn("test1")//
                .when(this.instanceMethods).invokeStringReturnMethodByArg("1");
        doReturn("test2")//
                .when(this.instanceMethods).invokeStringReturnMethodByArg("2");

        // 引数に対応する戻り値が返却される。
        assertEquals("test1", this.instanceMethods.invokeStringReturnMethodByArg("1"));
        assertEquals("test2", this.instanceMethods.invokeStringReturnMethodByArg("2"));
        // 未設定の引数は、nullとなる。
        assertEquals(null, this.instanceMethods.invokeStringReturnMethodByArg("0"));
    }

    @Test
    @DisplayName("any()の引数のみを定義した場合、すべての呼出でMockがで適用される。")
    void test_invokeStringReturnMethodByArg_AnyOnly() {

        doReturn("test1")//
                .when(this.instanceMethods).invokeStringReturnMethodByArg(any());

        // 引数に対応する戻り値が返却される。
        assertEquals("test1", this.instanceMethods.invokeStringReturnMethodByArg(null));
        assertEquals("test1", this.instanceMethods.invokeStringReturnMethodByArg("1"));
        assertEquals("test1", this.instanceMethods.invokeStringReturnMethodByArg("2"));

        // 一致する引数で呼ばれた回数
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg(null);
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg("1");
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg("2");
        // なんらかの引数で呼ばれた回数
        verify(this.instanceMethods, times(3)).invokeStringReturnMethodByArg(any());
        // 一致しない引数で呼ばれた回数
        verify(this.instanceMethods, times(0)).invokeStringReturnMethodByArg("3");
    }

    // any()を登録するなら、明示的な引数を設定すべきでない。
    @Test
    @DisplayName("明示的な引数を定義した後、any()の引数を定義した場合、すべての呼出でany()のふるまいとなるMockがで適用される。")
    void test_invokeStringReturnMethodByArg_ArgAndAny() {

        // org.mockito.exceptions.misusing.UnnecessaryStubbingException: Unnecessary stubbings
        // detected.
        // 実行できないので、Mockを登録できない。
        // doReturn("test1")//
        // .when(this.instanceMethods).invokeOneArgWithReturn("1");
        doReturn("testAny")//
                .when(this.instanceMethods).invokeStringReturnMethodByArg(any());

        // あとから、any()の引数を指定すると、any()に対応した戻り値を返却する。
        assertEquals("testAny", this.instanceMethods.invokeStringReturnMethodByArg(null));
        assertEquals("testAny", this.instanceMethods.invokeStringReturnMethodByArg("1"));
        assertEquals("testAny", this.instanceMethods.invokeStringReturnMethodByArg("2"));

        // 一致する引数で呼ばれた回数
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg(null);
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg("1");
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg("2");
        // なんらかの引数で呼ばれた回数
        verify(this.instanceMethods, times(3)).invokeStringReturnMethodByArg(any());
        // 一致しない引数で呼ばれた回数
        verify(this.instanceMethods, times(0)).invokeStringReturnMethodByArg("3");

    }

    // any()を登録するなら、明示的な引数を設定すべきでない。
    @Test
    @DisplayName("any()の引数を定義した後、明示的な引数を定義した場合、明示的な引数に一致する場合のみ、それに対応するMockがで適用され、それ以外の呼出でany()のふるまいとなるMockがで適用される。")
    void test_invokeStringReturnMethodByArg_AnyAndArg() {

        doReturn("testAny")//
                .when(this.instanceMethods).invokeStringReturnMethodByArg(any());

        doReturn("test1")//
                .when(this.instanceMethods).invokeStringReturnMethodByArg("1");

        // 明示的な引数に対応する戻り値を優先して返却する。
        // 明示的な引数に対応しない場合は、any()の引数に対応した戻り値を返却する。
        assertEquals("testAny", this.instanceMethods.invokeStringReturnMethodByArg(null));
        assertEquals("test1", this.instanceMethods.invokeStringReturnMethodByArg("1"));
        assertEquals("testAny", this.instanceMethods.invokeStringReturnMethodByArg("2"));

        // 一致する引数で呼ばれた回数
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg(null);
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg("1");
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg("2");
        // なんらかの引数で呼ばれた回数
        verify(this.instanceMethods, times(3)).invokeStringReturnMethodByArg(any());
        // 一致しない引数で呼ばれた回数
        verify(this.instanceMethods, times(0)).invokeStringReturnMethodByArg("3");
    }

    @Test
    @DisplayName("定義したMockを呼び出さなくても、呼び出し回数のverifyはできる。")
    void test_invokeStringReturnMethodByArg_NoCall() {

        // org.mockito.exceptions.misusing.UnnecessaryStubbingException: Unnecessary stubbings
        // detected.
        // 実行できないので、Mockを登録できない。
        // doReturn("test1")//
        // .when(this.instanceMethods).invokeOneArgWithReturn("1");

        // 一致する引数で呼ばれた回数
        verify(this.instanceMethods, times(0)).invokeStringReturnMethodByArg("1");
        // なんらかの引数で呼ばれた回数
        verify(this.instanceMethods, times(0)).invokeStringReturnMethodByArg(any());
        // 一致しない引数で呼ばれた回数
        verify(this.instanceMethods, times(0)).invokeStringReturnMethodByArg("2");
    }

    @Test
    @DisplayName("ArgumentCaptorの引数のみを定義した場合、すべての呼出でMockがで適用され、実行後、引数をチェックできる。")
    void test_invokeStringReturnMethodByArg_Captor() {

        ArgumentCaptor<String> arg1Captor = ArgumentCaptor.forClass(String.class);

        doReturn("test1")//
                .when(this.instanceMethods).invokeStringReturnMethodByArg(arg1Captor.capture());

        // 引数に対応する戻り値が返却される。
        assertEquals("test1", this.instanceMethods.invokeStringReturnMethodByArg(null));
        assertEquals("test1", this.instanceMethods.invokeStringReturnMethodByArg("1"));
        assertEquals("test1", this.instanceMethods.invokeStringReturnMethodByArg("2"));

        assertEquals(Arrays.asList(null, "1", "2"), arg1Captor.getAllValues());

        // 一致する引数で呼ばれた回数
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg(null);
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg("1");
        verify(this.instanceMethods, times(1)).invokeStringReturnMethodByArg("2");
        // なんらかの引数で呼ばれた回数
        verify(this.instanceMethods, times(3)).invokeStringReturnMethodByArg(any());
        // 一致しない引数で呼ばれた回数
        verify(this.instanceMethods, times(0)).invokeStringReturnMethodByArg("3");
    }

}
