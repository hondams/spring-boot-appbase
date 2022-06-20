package com.github.hondams.appbase.test.sample.mock;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestSampleCompMain {

    private final TestSampleCompSubClass subClass;

    private final TestSampleCompSubInterface subInterface;

    private int count;

    public String execute(String arg1) {
        return "CompMain{" + arg1 + "," + this.subClass.execute(arg1) + ","
                + this.subInterface.execute(arg1) + "," + this.count++ + "}";
    }
}
