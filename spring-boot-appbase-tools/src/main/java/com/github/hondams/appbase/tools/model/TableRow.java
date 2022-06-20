package com.github.hondams.appbase.tools.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TableRow {

    @Getter
    private final int lineNumber;

    @Getter
    private final List<String> columns = new ArrayList<>();

    public void addColumn(String value) {
        this.columns.add(value);
    }

    public int getColumnCount() {
        return this.columns.size();
    }

    public String getValue(int columnIndex) {
        if (columnIndex < 0 || this.columns.size() <= columnIndex) {
            return null;
        }
        return this.columns.get(columnIndex);
    }

    public String[] getValues() {
        return this.columns.toArray(new String[this.columns.size()]);
    }

    @Override
    public String toString() {
        return String.valueOf(this.columns);
    }
}
