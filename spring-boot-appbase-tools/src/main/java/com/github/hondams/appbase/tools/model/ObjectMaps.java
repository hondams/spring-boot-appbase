package com.github.hondams.appbase.tools.model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import com.github.hondams.appbase.tools.exception.ToolsException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@ToString
@Slf4j
public class ObjectMaps {

    @Getter
    private final String objectsName;

    @Getter
    private final List<Map<String, Object>> objectMaps = new ArrayList<>();

    @Getter
    @Setter
    private List<ObjectMapColumn> columns;

    @Getter
    @Setter
    private Path path;

    @Getter
    @Setter
    private int objectsNameLineNumber;

    @Getter
    @Setter
    private int columnsLineNumber;

    public void addObjectMap(Map<String, Object> objectMap) {
        this.objectMaps.add(objectMap);
    }

    public Map<String, Object> findObjectMapByKey(Map<String, Object> objectMap) {
        Map<String, Object> key = getKey(objectMap);
        return findObjectMap(key);
    }

    public Map<String, Object> findRequiredObjectMapByKey(Map<String, Object> objectMap) {
        Map<String, Object> key = getKey(objectMap);
        return findRequiredObjectMap(key);
    }


    public Object getColumnValueByKey(Map<String, Object> objectMap, String columnName) {
        Map<String, Object> key = getKey(objectMap);
        return getColumnValue(key, columnName);
    }

    public Object getColumnValue(Map<String, Object> searchingObjectMap, String columnName) {
        Map<String, Object> foundObjectMap = findRequiredObjectMap(searchingObjectMap);
        Object columnValue = foundObjectMap.get(columnName);
        if (columnValue == null) {
            throw new ToolsException("not found columnValue. objectsName=" + this.objectsName
                    + ", searchingObjectMap=" + searchingObjectMap + ", columnName=" + columnName);
        }
        return columnValue;
    }

    public List<Map<String, Object>> findObjectMapsByParentKey(Map<String, Object> objectMap) {
        Map<String, Object> parentKey = getParentKey(objectMap);
        return findObjectMaps(parentKey);
    }

    public Map<String, Object> findRequiredObjectMap(Map<String, Object> searchingObjectMap) {
        Map<String, Object> foundObjectMap = findObjectMap(searchingObjectMap);
        if (foundObjectMap == null) {
            throw new ToolsException("not found objectMap. objectsName=" + this.objectsName
                    + ", searchingObjectMap=" + searchingObjectMap);
        }
        return foundObjectMap;
    }

    public Map<String, Object> findObjectMap(Map<String, Object> searchingObjectMap) {
        List<Map<String, Object>> objectMaps = findObjectMaps(searchingObjectMap);
        switch (objectMaps.size()) {
            case 0:
                return null;
            case 1:
                return objectMaps.get(0);
            default:
                throw new ToolsException("duplicated objectMap. objectsName=" + this.objectsName
                        + ", searchingObjectMap=" + searchingObjectMap);
        }
    }

    public List<Map<String, Object>> findObjectMaps(Map<String, Object> searchingObjectMap) {
        List<Map<String, Object>> objectMaps = new ArrayList<>();
        for (Map<String, Object> objectMap : this.objectMaps) {
            if (matchesColumnValues(objectMap, searchingObjectMap)) {
                objectMaps.add(objectMap);
            }
        }
        return objectMaps;
    }

    public String getKeyText(Map<String, Object> objectMap) {
        List<String> key = getKey(objectMap).values().stream().map(o -> String.valueOf(o))
                .collect(Collectors.toList());
        return String.join(";", key);
    }

    public Map<String, Object> getKey(Map<String, Object> objectMap) {
        Map<String, Object> key = new LinkedHashMap<>();
        for (ObjectMapColumn column : this.columns) {
            if (column.isKey()) {
                Object columnValue = objectMap.get(column.getColumnName());
                key.put(column.getColumnName(), columnValue);
            }
        }
        return key;
    }

    private Map<String, Object> getParentKey(Map<String, Object> objectMap) {
        Map<String, Object> key = new LinkedHashMap<>();
        for (ObjectMapColumn column : this.columns) {
            if (column.isKey()) {
                Object columnValue = objectMap.get(column.getColumnName());
                if (columnValue == null) {
                    break;
                }
                key.put(column.getColumnName(), columnValue);
            }
        }
        return key;
    }

    private boolean matchesColumnValues(Map<String, Object> objectMap,
            Map<String, Object> searchingObjectKeyMap) {

        for (Map.Entry<String, Object> entry : searchingObjectKeyMap.entrySet()) {
            String searchingColumnName = entry.getKey();
            Object searchingColumnValue = entry.getValue();
            Object columnValue = objectMap.get(searchingColumnName);
            if (!Objects.equals(searchingColumnValue, columnValue)) {
                return false;
            }
        }
        return true;
    }

    public void fillColumnValues(RootObjectsMap rootObjectsMap) {

        for (Map<String, Object> objectMap : this.objectMaps) {
            for (ObjectMapColumn column : this.columns) {
                Object columnValue = objectMap.get(column.getColumnName());
                if (columnValue == null) {
                    try {
                        columnValue = column.getFillValue(rootObjectsMap, objectMap);
                    } catch (ToolsException e) {
                        log.warn(
                                "fail to fill. fillingObjectsName={}, fillingKey={}, fillingColumnName={} [{}]",
                                this.objectsName, getKey(objectMap), column.getColumnName(),
                                e.getMessage());
                    }
                    objectMap.put(column.getColumnName(), columnValue);
                }
            }
        }
    }
}
