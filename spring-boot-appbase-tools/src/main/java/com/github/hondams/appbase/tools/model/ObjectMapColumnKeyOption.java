package com.github.hondams.appbase.tools.model;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.github.hondams.appbase.tools.exception.ToolsException;
import lombok.Getter;

public class ObjectMapColumnKeyOption implements ObjectMapColumnOption {

    private static final ObjectMapColumnOption INSTANCE = new ObjectMapColumnKeyOption();

    public static ObjectMapColumnOption createColumnOption(List<String> optionValues) {
        if (optionValues.size() != 1 || !StringUtils.isEmpty(optionValues.get(0))) {
            throw new ToolsException("invalid optionValues. optionValues=" + optionValues);
        }
        return INSTANCE;
    }

    @Getter
    private final String defaultColumnName = null;

    @Override
    public Object getFillValue(RootObjectsMap rootObjectsMap, Map<String, Object> objectMap) {
        return null;
    }
}
