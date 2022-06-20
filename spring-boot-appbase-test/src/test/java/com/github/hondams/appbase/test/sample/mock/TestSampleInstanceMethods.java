package com.github.hondams.appbase.test.sample.mock;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class TestSampleInstanceMethods {

    @Getter
    private static CallInfo callInfo = new CallInfo();

    public void invokeVoidMethod() {
        callInfo.callCountOfInvokeVoidMethod++;
    }

    public String invokeStringReturnMethod() {
        return "invokeStringReturnMethod:" + callInfo.callCountOfInvokeStringReturnMethod++;
    }

    public void invokeVoidMethodByArg(String arg1) {
        callInfo.argsOfInvokeVoidMethodByArg.add(arg1);
        callInfo.callCountOfInvokeVoidMethodByArg++;
    }

    public String invokeStringReturnMethodByArg(String arg1) {
        callInfo.argsOfInvokeStringReturnMethodByArg.add(arg1);
        return "invokeStringReturnMethodByArg:"//
                + callInfo.callCountOfInvokeStringReturnMethodByArg++ + ":" + arg1;
    }

    public static class CallInfo {
        @Getter
        private int callCountOfInvokeVoidMethod;
        @Getter
        private int callCountOfInvokeStringReturnMethod;
        @Getter
        private int callCountOfInvokeVoidMethodByArg;
        @Getter
        private int callCountOfInvokeStringReturnMethodByArg;
        @Getter
        private List<String> argsOfInvokeVoidMethodByArg = new ArrayList<>();
        @Getter
        private List<String> argsOfInvokeStringReturnMethodByArg = new ArrayList<>();

        public void clear() {
            this.callCountOfInvokeVoidMethod = 0;
            this.callCountOfInvokeStringReturnMethod = 0;
            this.callCountOfInvokeVoidMethodByArg = 0;
            this.callCountOfInvokeStringReturnMethodByArg = 0;
            this.argsOfInvokeVoidMethodByArg.clear();
            this.argsOfInvokeStringReturnMethodByArg.clear();
        }
    }
}
