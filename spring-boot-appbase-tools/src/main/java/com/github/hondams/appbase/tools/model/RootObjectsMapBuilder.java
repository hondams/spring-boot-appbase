package com.github.hondams.appbase.tools.model;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import com.github.hondams.appbase.tools.config.ToolsProperties;
import com.github.hondams.appbase.tools.exception.ToolsException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RootObjectsMapBuilder {

    private static final Map<String, ObjectMapColumnOptionFactory> COLUMN_OPTION_FACTORY_MAP =
            Map.of(//
                    "key", ObjectMapColumnKeyOption::createColumnOption, //
                    "ref", ObjectMapColumnRefOption::createColumnOption, //
                    "children", ObjectMapColumnChildrenOption::createColumnOption, //
                    "name", ObjectMapColumnNameOption::createColumnOption, //
                    "dbDataType:postgresql",
                    ObjectMapColumnPostgreSqlDbDataTypeOption::createColumnOption, //
                    "javaDataType", ObjectMapColumnJavaDataTypeOption::createColumnOption, //
                    "javaValidations", ObjectMapColumnJavaValidationsOption::createColumnOption);

    private final ToolsProperties properties;

    @Getter
    private final RootObjectsMap rootObjectMap = new RootObjectsMap();

    public void addData(Path path) throws IOException {

        Table table = createTable(path);

        ObjectMaps objectMaps = null;
        List<ObjectMapColumn> columns = null;
        for (TableRow row : table.getRows()) {
            if (isEmpty(row)) {
                objectMaps = null;
                columns = null;
            } else if (objectMaps == null) {
                String objectsName = getObjectsName(row);
                objectMaps = this.rootObjectMap.createOrGetObjectMaps(objectsName);
                objectMaps.setObjectsNameLineNumber(row.getLineNumber());
                objectMaps.setPath(path);
                columns = null;
            } else if (columns == null) {
                columns = getColumns(path, row);

                if (objectMaps.getColumns() == null) {
                    objectMaps.setColumns(columns);
                    objectMaps.setColumnsLineNumber(row.getLineNumber());
                } else {
                    if (!objectMaps.getColumns().equals(columns)) {
                        throw new ToolsException("mismatch columns. "//
                                + "old.columns.lineNumber=" + objectMaps.getColumnsLineNumber() //
                                + ", new.columns.lineNumber=" + row.getLineNumber() //
                                + ", old.columns=" + objectMaps.getColumns() //
                                + ", new.columns=" + columns //
                                + ", path=" + path);
                    }
                    objectMaps.setColumnsLineNumber(row.getLineNumber());
                }
            } else {
                Map<String, Object> objectMap = getObjectMap(objectMaps, row);
                objectMaps.addObjectMap(objectMap);
            }
        }
    }

    private Map<String, Object> getObjectMap(ObjectMaps objectMaps, TableRow row) {

        List<ObjectMapColumn> columns = objectMaps.getColumns();
        if (columns == null) {
            throw new ToolsException("mismatch column count. "//
                    + "values.lineNumber=" + row.getLineNumber() //
                    + ", values.count=" + row.getColumnCount() //
                    + ", path=" + objectMaps.getPath());
        }

        if (columns.size() != row.getColumnCount()) {
            throw new ToolsException("mismatch column count. "//
                    + "columns.lineNumber=" + objectMaps.getColumnsLineNumber() //
                    + ", values.lineNumber=" + row.getLineNumber() //
                    + ", columns.count=" + columns.size() //
                    + ", values.count=" + row.getColumnCount() //
                    + ", path=" + objectMaps.getPath());
        }

        Map<String, Object> objectMap = new LinkedHashMap<>();

        for (int i = 0; i < columns.size(); i++) {
            ObjectMapColumn column = columns.get(i);
            String value = row.getValue(i);
            String columnName = column.getColumnName();
            objectMap.put(columnName, value);
        }

        return objectMap;
    }

    private Table createTable(Path path) throws IOException {
        Charset charset = ToolsUtils.getDetectedCharset(path);
        String extension = FilenameUtils.getExtension(path.getFileName().toString());
        switch (extension.toLowerCase()) {
            case "csv":
                return TableUtils.readAsCsv(path, charset);
            case "tsv":
                return TableUtils.readAsTsv(path, charset);
            default:
                throw new ToolsException("not supported extension. " //
                        + "path=" + path);
        }
    }

    private List<ObjectMapColumn> getColumns(Path path, TableRow row) {

        List<ObjectMapColumn> columns = new ArrayList<>();
        for (String rawColumn : row.getColumns()) {
            ObjectMapColumn column = createColumn(path, row, rawColumn);

            if (columns.stream().anyMatch(c -> column.getColumnName().equals(c.getColumnName()))) {
                throw new ToolsException("duplicated columnName. " //
                        + "lineNumber=" + row.getLineNumber() //
                        + ", rawColumn=" + rawColumn //
                        + ", columnName=" + column.getColumnName() //
                        + ", path=" + path);
            }
            columns.add(column);
        }
        return columns;
    }

    private ObjectMapColumn createColumn(Path path, TableRow row, String rawColumn) {

        if (rawColumn.length() <= 2 || //
                !rawColumn.startsWith("[") || !rawColumn.endsWith("]")) {
            throw new ToolsException("invalid column format. " //
                    + "lineNumber=" + row.getLineNumber() //
                    + ", rawColumn=" + rawColumn //
                    + ", path=" + path);
        }

        String[] values = rawColumn.substring(1, rawColumn.length() - 1).split("@");
        boolean key = false;
        String columnName = values[0];

        List<ObjectMapColumnOption> columnOptions = new ArrayList<>();
        for (int i = 1; i < values.length; i++) {
            String optionText = values[i];
            String optionName = StringUtils.substringBefore(optionText, "=");
            String optionValueText = StringUtils.substringAfter(optionText, "=");
            String[] optionValues = optionValueText.split(";");
            ObjectMapColumnOption columnOption = null;
            try {
                columnOption = createColumnOption(optionName, Arrays.asList(optionValues));
            } catch (ToolsException e) {
                throw new ToolsException("invalid option format. " //
                        + "lineNumber=" + row.getLineNumber() //
                        + ", rawColumn=" + rawColumn //
                        + ", path=" + path, e);
            }

            if (columnOption instanceof ObjectMapColumnKeyOption) {
                key = true;
            } else {
                columnOptions.add(columnOption);
            }
        }

        if (StringUtils.isEmpty(columnName)) {
            for (ObjectMapColumnOption columnOption : columnOptions) {
                if (!StringUtils.isEmpty(columnOption.getDefaultColumnName())) {
                    columnName = columnOption.getDefaultColumnName();
                    break;
                }
            }
        }

        if (StringUtils.isEmpty(columnName)) {
            throw new ToolsException("not found columnName. " //
                    + "lineNumber=" + row.getLineNumber() //
                    + ", rawColumn=" + rawColumn //
                    + ", path=" + path);
        }

        return ObjectMapColumn.builder()//
                .columnName(columnName).key(key)
                .options(Collections.unmodifiableList(columnOptions)).build();
    }

    private ObjectMapColumnOption createColumnOption(String optionName, List<String> optionValues) {

        String revisedOptionName = optionName;
        switch (optionName) {
            case "dbDataType":
                revisedOptionName = revisedOptionName + ":" + this.properties.getDatabaseType();
                break;
            default:
                break;
        }

        ObjectMapColumnOptionFactory columnOptionFactory =
                COLUMN_OPTION_FACTORY_MAP.get(revisedOptionName);
        if (columnOptionFactory == null) {
            throw new ToolsException("not supported option. optionName=" + revisedOptionName);
        }
        return columnOptionFactory.createColumnOption(optionValues);
    }

    private boolean isEmpty(TableRow row) {
        return row.getColumnCount() == 1 && StringUtils.isEmpty(row.getValue(0));
    }

    private String getObjectsName(TableRow row) {
        if (row.getColumns().size() != 1) {
            return null;
        }

        String value = row.getColumns().get(0);
        if (value.length() <= 4 || //
                !value.startsWith("[[") || !value.endsWith("]]")) {
            return null;
        }
        return value.substring(2, value.length() - 2);
    }

}
