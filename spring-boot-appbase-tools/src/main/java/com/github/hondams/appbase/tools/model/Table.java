package com.github.hondams.appbase.tools.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class Table {

    @Getter
    private List<TableRow> rows = new ArrayList<>();


    public TableRow getRow(int rowIndex) {
        if (rowIndex < 0 || this.rows.size() <= rowIndex) {
            return null;
        }
        return this.rows.get(rowIndex);
    }

    public TableRow newRow() {
        TableRow row = new TableRow(getNextLineCount());
        this.rows.add(row);
        return row;
    }

    public String getValue(int rowIndex, int columnIndex) {
        TableRow row = getRow(rowIndex);
        if (row == null) {
            return null;
        }
        return row.getValue(columnIndex);
    }

    public String[][] getValues() {
        String[][] values = new String[this.rows.size()][];
        for (int i = 0; i < values.length; i++) {
            TableRow row = getRow(i);
            values[i] = row.getValues();
        }
        return values;
    }

    public int getRowCount() {
        return this.rows.size();
    }

    public int getColumnCount() {
        int columnCount = 0;
        for (TableRow row : this.rows) {
            columnCount = Math.max(columnCount, row.getColumnCount());
        }
        return columnCount;
    }

    @Override
    public String toString() {
        return String.valueOf(this.rows);
    }

    public static Table create(String[][] data) {

        Table rows = new Table();
        for (String[] values : data) {
            TableRow row = rows.newRow();
            for (String value : values) {
                row.addColumn(value);
            }
        }
        return rows;
    }

    private int getNextLineCount() {
        if (this.rows.isEmpty()) {
            return 1;
        }
        TableRow lastRow = this.rows.get(this.rows.size() - 1);

        int lineCount = lastRow.getLineNumber() + 1;

        for (String value : lastRow.getColumns()) {
            lineCount = lineCount + getLineCount(value);
        }

        return lineCount;
    }

    private int getLineCount(String text) {
        int lineCount = 0;
        boolean cr = false;
        for (char ch : text.toCharArray()) {
            switch (ch) {
                case '\r':
                    cr = true;
                    lineCount++;
                    break;
                case '\n':
                    if (!cr) {
                        lineCount++;
                    }
                    cr = false;
                    break;
                default:
                    cr = false;
                    break;
            }
        }
        return lineCount;
    }
}
