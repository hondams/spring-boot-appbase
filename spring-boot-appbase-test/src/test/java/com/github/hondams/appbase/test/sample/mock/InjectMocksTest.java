package com.github.hondams.appbase.test.sample.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

// テストクラスのインスタンスフィールドで、@InjectMocks、@Mockする場合は、@ExtendWithは必須。
@ExtendWith(MockitoExtension.class)
class InjectMocksTest {

    @InjectMocks
    private TestSampleCompMain main;

    // 試験対象インスタンス
    // 試験対象インスタンスから呼び出すインスタンスは、MockまたはSpyとする。
    @Mock
    private TestSampleCompSubInterface subInterface;

    @Spy
    private TestSampleCompSubClass subClass;

    @Test
    void test() {
        // Mockの登録をしないと、Mock化したメソッドの戻り値は、null
        assertEquals("CompMain{A,CompSubClass{A,0},null,0}", this.main.execute("A"));

        doReturn("test1")//
                .when(this.subClass).execute("A");
        doReturn("test2")//
                .when(this.subInterface).execute("A");

        assertEquals("CompMain{A,test1,test2,1}", this.main.execute("A"));

        // 一致する引数で呼ばれた回数
        verify(this.subClass, times(2)).execute("A");
        // なんらかの引数で呼ばれた回数
        verify(this.subClass, times(2)).execute(any());
        // 一致しない引数で呼ばれた回数
        verify(this.subClass, times(0)).execute("B");
    }


    @Test
    void test2() {
        doReturn("test1")//
                .when(this.subInterface).execute("A");
        doCallRealMethod()//
                .when(this.subClass).execute("A");

        assertEquals("CompMain{A,CompSubClass{A,0},test1,0}", this.main.execute("A"));
    }
}
