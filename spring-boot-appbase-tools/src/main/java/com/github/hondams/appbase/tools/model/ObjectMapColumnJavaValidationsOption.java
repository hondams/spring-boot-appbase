package com.github.hondams.appbase.tools.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.github.hondams.appbase.tools.exception.ToolsException;
import lombok.Getter;

public class ObjectMapColumnJavaValidationsOption implements ObjectMapColumnOption {

    private static final ObjectMapColumnOption INSTANCE =
            new ObjectMapColumnJavaValidationsOption();

    public static ObjectMapColumnOption createColumnOption(List<String> optionValues) {
        if (optionValues.size() != 1 || !StringUtils.isEmpty(optionValues.get(0))) {
            throw new ToolsException("invalid optionValues. optionValues=" + optionValues);
        }
        return INSTANCE;
    }

    @Getter
    private final String defaultColumnName = "javaValidations";

    @Override
    public Object getFillValue(RootObjectsMap rootObjectsMap, Map<String, Object> objectMap) {
        List<Map<String, Object>> validations = new ArrayList<>();
        return validations;
    }
}
