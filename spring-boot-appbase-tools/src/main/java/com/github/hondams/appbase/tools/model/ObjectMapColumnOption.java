package com.github.hondams.appbase.tools.model;

import java.util.Map;

public interface ObjectMapColumnOption {

    String getDefaultColumnName();

    Object getFillValue(RootObjectsMap rootObjectsMap, Map<String, Object> objectMap);
}
