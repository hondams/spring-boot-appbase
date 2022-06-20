package com.github.hondams.appbase.tools.model;

import java.util.LinkedHashMap;
import java.util.Map;
import com.github.hondams.appbase.tools.exception.ToolsException;

public class RootObjectsMap {

    private Map<String, ObjectMaps> objectMapsMap = new LinkedHashMap<>();

    public ObjectMaps createOrGetObjectMaps(String objectsName) {
        return this.objectMapsMap.computeIfAbsent(//
                objectsName, ObjectMaps::new);
    }

    public ObjectMaps getObjectMaps(String objectsName) {
        ObjectMaps objectMaps = this.objectMapsMap.get(objectsName);
        if (objectMaps == null) {
            throw new ToolsException("not found ObjectMaps. objectsName=" + objectsName);
        }
        return objectMaps;
    }

    public Map<String, Object> getObjectsMap() {
        Map<String, Object> objectsMap = new LinkedHashMap<>();
        for (Map.Entry<String, ObjectMaps> entry : this.objectMapsMap.entrySet()) {
            String objectsName = entry.getKey();
            ObjectMaps objectMaps = entry.getValue();
            objectsMap.put(objectsName, objectMaps.getObjectMaps());
        }
        return objectsMap;
    }

    public void fillColumnValues() {
        for (ObjectMaps objectMaps : this.objectMapsMap.values()) {
            objectMaps.fillColumnValues(this);
        }
    }

    @Override
    public String toString() {
        return String.valueOf(this.objectMapsMap);
    }
}
