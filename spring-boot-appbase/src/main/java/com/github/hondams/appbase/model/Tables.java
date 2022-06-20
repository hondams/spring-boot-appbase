package com.github.hondams.appbase.model;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.hondams.appbase.exception.SystemException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Tables {

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

    public static Table readAsCsv(String text) {
        try {
            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
            CsvSchema schema = mapper.schema();
            String[][] rows = mapper.readerFor(String[][].class).with(schema).readValue(text);
            return create(rows);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static Table readAsCsv(Path path, Charset charset) {
        try {
            String text = Files.readString(path, charset);
            return readAsCsv(text);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static Table readAsTsv(String text) {
        try {
            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
            CsvSchema schema = mapper.schema().withColumnSeparator('\t');
            String[][] rows = mapper.readerFor(String[][].class).with(schema).readValue(text);
            return create(rows);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static Table readAsTsv(Path path, Charset charset) {
        try {
            String text = Files.readString(path, charset);
            return readAsTsv(text);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static String toStringAsCsv(Table table) {
        try {
            CsvMapper mapper = new CsvMapper();
            return mapper.writeValueAsString(table.getValues());
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static void writeAsCsv(Path path, Table table, Charset charset) {
        try {
            String text = toStringAsCsv(table);
            Files.writeString(path, text, charset, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static String toStringAsTsv(Table table) {
        try {
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema = mapper.schemaFor(String[].class).withColumnSeparator('\t');
            return mapper.writer(schema).writeValueAsString(table.getValues());
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }

    public static void writeAsTsv(Path path, Table table, Charset charset) {
        try {
            String text = toStringAsTsv(table);
            Files.writeString(path, text, charset, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }
}
