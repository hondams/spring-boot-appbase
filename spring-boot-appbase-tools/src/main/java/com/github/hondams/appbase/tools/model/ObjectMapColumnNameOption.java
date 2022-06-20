package com.github.hondams.appbase.tools.model;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.github.hondams.appbase.tools.exception.ToolsException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ObjectMapColumnNameOption implements ObjectMapColumnOption {

    public static ObjectMapColumnOption createColumnOption(List<String> optionValues) {
        if (optionValues.size() != 1 || StringUtils.isEmpty(optionValues.get(0))) {
            throw new ToolsException("invalid optionValues. optionValues=" + optionValues);
        }
        String jpNameColumnName = optionValues.get(0);
        return new ObjectMapColumnNameOption(jpNameColumnName);
    }

    @Getter
    private final String defaultColumnName = null;

    @Getter
    private final String jpNameColumnName;

    @Override
    public Object getFillValue(RootObjectsMap rootObjectsMap, Map<String, Object> objectMap) {
        rootObjectsMap.getObjectMaps("jpNames");
        return null;
    }
}
