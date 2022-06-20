package com.github.hondams.appbase.tools.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.github.hondams.appbase.tools.exception.ToolsException;
import com.github.hondams.appbase.tools.mustache.util.ToolsValueUtils;
import lombok.Getter;

public class ObjectMapColumnJavaDataTypeOption implements ObjectMapColumnOption {

    private static final ObjectMapColumnOption INSTANCE = new ObjectMapColumnJavaDataTypeOption();

    public static ObjectMapColumnOption createColumnOption(List<String> optionValues) {
        if (optionValues.size() != 1 || !StringUtils.isEmpty(optionValues.get(0))) {
            throw new ToolsException("invalid optionValues. optionValues=" + optionValues);
        }
        return INSTANCE;
    }

    @Getter
    private final String defaultColumnName = "javaDataType";

    @Override
    public Object getFillValue(RootObjectsMap rootObjectsMap, Map<String, Object> objectMap) {

        boolean required = ToolsValueUtils.toPrimitiveBoolean((String) objectMap.get("required"));
        String dataValueType = (String) objectMap.get("dataValueType");
        switch (dataValueType) {
            case "真偽値":
                if (required) {
                    return "boolean";
                } else {
                    return "Boolean";
                }
            case "列挙値":
                return getEnumJavaDataType(rootObjectsMap, objectMap);
            case "文字列":
                return "String";
            case "日付":
                return "java.time.LocalDate";
            case "タイムスタンプ":
                return "java.time.LocalDateTime";
            case "小数":
                return "java.math.BigDecimal";
            case "整数":
                return getIntegerJavaDataType(rootObjectsMap, objectMap, required);
            default:
                throw new ToolsException(
                        "not supported dataValueType. dataValueType=" + dataValueType);
        }
    }

    private String getEnumJavaDataType(RootObjectsMap rootObjectsMap,
            Map<String, Object> objectMap) {
        ObjectMaps dataDics = rootObjectsMap.getObjectMaps("dataDics");
        ObjectMaps javaClassNames = rootObjectsMap.getObjectMaps("javaClassNames");

        Map<String, Object> key = new LinkedHashMap<>();
        key.put("apDesignDataObjectsName", "dataDics");
        key.put("apDesignDataKey", dataDics.getKeyText(objectMap));

        String javaClassName = (String) javaClassNames.getColumnValue(key, "javaClassName");
        return javaClassName;
    }

    private String getIntegerJavaDataType(RootObjectsMap rootObjectsMap,
            Map<String, Object> objectMap, boolean required) {
        ObjectMaps numDataDics = rootObjectsMap.getObjectMaps("numDataDics");
        String min = (String) numDataDics.getColumnValueByKey(objectMap, "min");
        String max = (String) numDataDics.getColumnValueByKey(objectMap, "max");
        min = StringUtils.removeStart(min, "-");
        max = StringUtils.removeStart(max, "-");
        if (StringUtils.isEmpty(min) && StringUtils.isEmpty(max)) {
            if (required) {
                return "int";
            } else {
                return "Integer";
            }
        }
        int digit = Math.max(min.length(), max.length());
        if (digit <= 9) {
            if (required) {
                return "int";
            } else {
                return "Integer";
            }
        } else if (digit <= 18) {
            if (required) {
                return "long";
            } else {
                return "Long";
            }
        } else {
            return "java.math.BigInteger";
        }
    }
}
