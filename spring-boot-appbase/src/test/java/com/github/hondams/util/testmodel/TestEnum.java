package com.github.hondams.util.testmodel;

import com.github.hondams.appbase.model.CodeEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TestEnum implements CodeEnum {

    VALUE1("1"), //
    VALUE2("2");

    private final String code;
}
