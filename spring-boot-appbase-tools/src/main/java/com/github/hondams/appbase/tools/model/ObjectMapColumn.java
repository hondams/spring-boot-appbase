package com.github.hondams.appbase.tools.model;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ObjectMapColumn {

    private final String columnName;

    private final List<ObjectMapColumnOption> options;

    private final boolean key;

    public Object getFillValue(RootObjectsMap rootObjectsMap, Map<String, Object> objectMap) {
        for (ObjectMapColumnOption option : this.options) {
            Object fillValue = option.getFillValue(rootObjectsMap, objectMap);
            if (fillValue != null) {
                return fillValue;
            }
        }
        return null;
    }
}
