package com.github.hondams.appbase.test.sample.mock;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestSampleCompSubClass {

    private int count;

    public String execute(String arg1) {
        return "CompSubClass{" + arg1 + "," + this.count++ + "}";
    }
}
