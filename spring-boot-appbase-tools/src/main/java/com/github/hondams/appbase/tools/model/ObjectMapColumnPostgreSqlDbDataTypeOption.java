package com.github.hondams.appbase.tools.model;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.github.hondams.appbase.tools.exception.ToolsException;
import lombok.Getter;

public class ObjectMapColumnPostgreSqlDbDataTypeOption implements ObjectMapColumnOption {

    private static final ObjectMapColumnOption INSTANCE =
            new ObjectMapColumnPostgreSqlDbDataTypeOption();

    public static ObjectMapColumnOption createColumnOption(List<String> optionValues) {
        if (optionValues.size() != 1 || !StringUtils.isEmpty(optionValues.get(0))) {
            throw new ToolsException("invalid optionValues. optionValues=" + optionValues);
        }
        return INSTANCE;
    }

    @Getter
    private final String defaultColumnName = "dbDataType";

    @Override
    public Object getFillValue(RootObjectsMap rootObjectsMap, Map<String, Object> objectMap) {
        String dataValueType = (String) objectMap.get("dataValueType");
        switch (dataValueType) {
            case "真偽値":
                return "bool";
            case "列挙値":
                return getEnumDbDataType(rootObjectsMap, objectMap);
            case "文字列":
                return getStringDbDataType(rootObjectsMap, objectMap);
            case "日付":
                return "date";
            case "タイムスタンプ":
                return "timestamptz";
            case "小数":
                return getDecimalDbDataType(rootObjectsMap, objectMap);
            case "整数":
                return getIntegerDbDataType(rootObjectsMap, objectMap);
            default:
                throw new ToolsException(
                        "not supported dataValueType. dataValueType=" + dataValueType);
        }
    }

    private Object getIntegerDbDataType(RootObjectsMap rootObjectsMap,
            Map<String, Object> objectMap) {

        ObjectMaps numDataDics = rootObjectsMap.getObjectMaps("numDataDics");
        String min = (String) numDataDics.getColumnValueByKey(objectMap, "min");
        String max = (String) numDataDics.getColumnValueByKey(objectMap, "max");
        min = StringUtils.removeStart(min, "-");
        max = StringUtils.removeStart(max, "-");

        if (StringUtils.isEmpty(min) && StringUtils.isEmpty(max)) {
            return "int";
        }
        int digit = Math.max(min.length(), max.length());
        if (digit <= 9) {
            return "int";
        } else if (digit <= 18) {
            return "int8";
        } else {
            return "numeric(" + digit + ")";
        }
    }

    private Object getDecimalDbDataType(RootObjectsMap rootObjectsMap,
            Map<String, Object> objectMap) {

        ObjectMaps numDataDics = rootObjectsMap.getObjectMaps("numDataDics");
        String min = (String) numDataDics.getColumnValueByKey(objectMap, "min");
        String max = (String) numDataDics.getColumnValueByKey(objectMap, "max");
        int scale = Integer.parseInt((String) numDataDics.getColumnValueByKey(objectMap, "scale"));
        min = StringUtils.removeStart(min, "-");
        max = StringUtils.removeStart(max, "-");
        min = StringUtils.substringBefore(min, ".");
        max = StringUtils.substringBefore(max, ".");

        // 23.5141という数値の精度は6で位取りは4
        int precision = Math.max(min.length(), max.length()) + scale;
        return "decimal(" + precision + ", " + scale + ")";
    }

    private Object getStringDbDataType(RootObjectsMap rootObjectsMap,
            Map<String, Object> objectMap) {

        ObjectMaps textDataDics = rootObjectsMap.getObjectMaps("textDataDics");
        int minCharCount = Integer
                .parseInt((String) textDataDics.getColumnValueByKey(objectMap, "minCharCount"));
        int maxCharCount = Integer
                .parseInt((String) textDataDics.getColumnValueByKey(objectMap, "maxCharCount"));
        if (minCharCount == maxCharCount) {
            return "char(" + maxCharCount + ")";
        } else {
            return "varchar(" + maxCharCount + ")";
        }
    }

    private Object getEnumDbDataType(RootObjectsMap rootObjectsMap, Map<String, Object> objectMap) {

        ObjectMaps enumDataDics = rootObjectsMap.getObjectMaps("enumDataDics");
        List<Map<String, Object>> objectMaps = enumDataDics.findObjectMapsByParentKey(objectMap);
        int length = 0;
        for (Map<String, Object> enumDataDicObjectMap : objectMaps) {
            String enumValue = (String) enumDataDicObjectMap.get("enumValue");
            length = Math.max(length, enumValue.length());
        }
        return "char(" + length + ")";
    }
}
