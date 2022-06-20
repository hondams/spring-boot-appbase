package com.github.hondams.appbase.tools.model;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TableUtils {

    public static Table readAsCsv(String text) throws JacksonException {
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        CsvSchema schema = mapper.schema();
        String[][] rows = mapper.readerFor(String[][].class).with(schema).readValue(text);
        return Table.create(rows);
    }

    public static Table readAsCsv(Path path, Charset charset) throws IOException {
        String text = Files.readString(path, charset);
        return readAsCsv(text);
    }

    public static Table readAsTsv(String text) throws JacksonException {
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        CsvSchema schema = mapper.schema().withColumnSeparator('\t');
        String[][] rows = mapper.readerFor(String[][].class).with(schema).readValue(text);
        return Table.create(rows);
    }

    public static Table readAsTsv(Path path, Charset charset) throws IOException {
        String text = Files.readString(path, charset);
        return readAsTsv(text);
    }

    public static String toStringAsCsv(Table table) throws JacksonException {
        CsvMapper mapper = new CsvMapper();
        return mapper.writeValueAsString(table.getValues());
    }

    public static void writeAsCsv(Path path, Table table, Charset charset) throws IOException {
        String text = toStringAsCsv(table);
        Files.writeString(path, text, charset, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static String toStringAsTsv(Table table) throws JacksonException {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(String[].class).withColumnSeparator('\t');
        return mapper.writer(schema).writeValueAsString(table.getValues());
    }

    public static void writeAsTsv(Path path, Table table, Charset charset) throws IOException {
        String text = toStringAsTsv(table);
        Files.writeString(path, text, charset, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
