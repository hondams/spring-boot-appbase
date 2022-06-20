package com.github.hondams.appbase.tools.model;

import java.util.List;

@FunctionalInterface
public interface ObjectMapColumnOptionFactory {

    ObjectMapColumnOption createColumnOption(List<String> optionValues);
}
