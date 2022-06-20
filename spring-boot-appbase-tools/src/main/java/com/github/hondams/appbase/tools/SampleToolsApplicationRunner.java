package com.github.hondams.appbase.tools;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.github.hondams.appbase.tools.config.ToolsProperties;
import com.github.hondams.appbase.tools.exception.ToolsException;
import com.github.hondams.appbase.tools.model.RootObjectsMap;
import com.github.hondams.appbase.tools.model.RootObjectsMapBuilder;
import com.github.hondams.appbase.tools.model.Table;
import com.github.hondams.appbase.tools.model.TableUtils;
import com.github.hondams.appbase.tools.model.ToolsUtils;
import com.github.hondams.appbase.tools.mustache.util.ToolsJavaUtils;
import com.github.hondams.appbase.tools.mustache.util.ToolsStringUtils;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SampleToolsApplicationRunner implements ApplicationRunner {

    private final ToolsProperties properties;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        log.info("properties={}", this.properties);

        switch (args.getNonOptionArgs().size()) {
            case 0: {
                Path basePath = Paths.get("");
                evaluateAllTemplates(basePath);
                break;
            }
            case 1: {
                Path basePath = Paths.get(args.getNonOptionArgs().get(0));
                evaluateAllTemplates(basePath);
                break;
            }
            default: {
                log.info("not supported args count. args.count={}", args.getNonOptionArgs().size());
                break;
            }
        }

        // Mustache.Compiler compiler = Mustache.compiler();
        // Template template = compiler.compile(null);
        // template.
    }

    private void evaluateAllTemplates2(Path basePath) throws IOException {
        {
            Path path =
                    Paths.get("C:\\projects\\boot\\spring-boot-sample-tools\\data\\symbols.tsv");
            Table table = TableUtils.readAsTsv(path, StandardCharsets.UTF_8);

            int i = 0;
            for (String[] values : table.getValues()) {
                int j = 0;
                log.info("values[{}].count={}", i, values.length);
                for (String value : values) {
                    log.info("values[{}][{}]={}", i, j, value);
                    j++;
                }
                i++;
            }

            Path output = path.getParent()
                    .resolve(FilenameUtils.getBaseName(path.getFileName().toString()) + "2."
                            + FilenameUtils.getExtension(path.getFileName().toString()));
            TableUtils.writeAsTsv(output, table, StandardCharsets.UTF_8);
        }
        {
            Path path =
                    Paths.get("C:\\projects\\boot\\spring-boot-sample-tools\\data\\symbols.csv");
            Table table = TableUtils.readAsCsv(path, StandardCharsets.UTF_8);

            int i = 0;
            for (String[] values : table.getValues()) {
                int j = 0;
                log.info("values[{}].count={}", i, values.length);
                for (String value : values) {
                    log.info("values[{}][{}]={}", i, j, value);
                    j++;
                }
                i++;
            }

            Path output = path.getParent()
                    .resolve(FilenameUtils.getBaseName(path.getFileName().toString()) + "2."
                            + FilenameUtils.getExtension(path.getFileName().toString()));
            TableUtils.writeAsCsv(output, table, StandardCharsets.UTF_8);
        }
    }

    private void evaluateAllTemplates(Path basePath) throws IOException {

        List<Path> templatePaths = ToolsUtils.getFilePaths(basePath, "mustache");
        List<Path> csvPaths = ToolsUtils.getFilePaths(basePath, "csv");
        List<Path> tsvPaths = ToolsUtils.getFilePaths(basePath, "tsv");
        Map<String, Path> templatePathMap = templatePaths.stream()
                .collect(Collectors.toMap(p -> p.getFileName().toString(), p -> p));

        log.info(
                "evaluate all templates. templates.count={}, csv.count={}, tsv.count={}, basePath={}", //
                templatePaths.size(), csvPaths.size(), tsvPaths.size(), //
                basePath.toAbsolutePath());

        RootObjectsMapBuilder builder = new RootObjectsMapBuilder(this.properties);
        for (Path tsvPath : tsvPaths) {
            log.info("tsvPath({})={}", ToolsUtils.getDetectedCharset(tsvPath), tsvPath);
            Table table = TableUtils.readAsTsv(tsvPath, StandardCharsets.UTF_8);
            log.info("table={}", table);
            int i = 0;
            for (Object[] values : table.getValues()) {
                int j = 0;
                log.info("values[{}].count={}", i, values.length);
                for (Object value : values) {
                    log.info("values[{}][{}]={}", i, j, value);
                    j++;
                }
                i++;
            }
            builder.addData(tsvPath);
        }
        for (Path csvPath : csvPaths) {
            log.info("tsvPath({})={}", ToolsUtils.getDetectedCharset(csvPath), csvPath);
            Table table = TableUtils.readAsTsv(csvPath, StandardCharsets.UTF_8);
            log.info("table={}", table);
            int i = 0;
            for (Object[] values : table.getValues()) {
                int j = 0;
                log.info("values[{}].count={}", i, values.length);
                for (Object value : values) {
                    log.info("values[{}][{}]={}", i, j, value);
                    j++;
                }
                i++;
            }
            builder.addData(csvPath);
        }

        RootObjectsMap rootObjectsMap = builder.getRootObjectMap();
        Map<String, Object> objectsMap = rootObjectsMap.getObjectsMap();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> generationRules =
                (List<Map<String, Object>>) objectsMap.get("generationRules");

        for (Map<String, Object> generationRule : generationRules) {

            String templateName = (String) generationRule.get("templateName");
            String objectsName = (String) generationRule.get("objectsName");
            String outputPathType = (String) generationRule.get("outputPathType");
            String lineSeparatorType = (String) generationRule.get("lineSeparatorType");

            Path templatePath = getTemplatesPath(templatePathMap, templateName);
            Charset charset = ToolsUtils.getDetectedCharset(templatePath);

            log.info("templateName={}, objectsName={}, outputPathType={}, lineSeparatorType={}," + //
                    " templatePath=({}){}", //
                    templateName, objectsName, outputPathType, lineSeparatorType, //
                    charset, templatePath);

            Mustache.Compiler compiler = Mustache.compiler();
            String templateText = Files.readString(templatePath, charset);
            Template template = compiler.compile(templateText);

            List<Map<String, Object>> templateContexts =
                    getTemplateContexts(rootObjectsMap, objectsName);
            for (Map<String, Object> templateContext : templateContexts) {
                StringWriter writer = new StringWriter();
                template.execute(templateContext, writer);
                String outputText = writer.toString();
                outputText = replaceLineSeparator(outputText, lineSeparatorType);
            }
        }
    }

    private void outputResult(Map<String, Object> templateContext, String text,
            String outputPathType, Charset charset) throws IOException {

        Path outputPath = getOutputPath(templateContext, outputPathType);
        Files.writeString(outputPath, text, charset, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    private Path getOutputPath(Map<String, Object> templateContext, String outputPathType) {
        // TODO Auto-generated method stub
        return null;
    }

    private String replaceLineSeparator(String text, String lineSeparatorType) throws IOException {
        String lineSeparator = getLineSeparator(lineSeparatorType);
        if (lineSeparator == null) {
            return text;
        }
        StringReader reader = new StringReader(text);
        List<String> lines = IOUtils.readLines(reader);
        return String.join(lineSeparator, lines);
    }

    private String getLineSeparator(String lineSeparatorType) {
        switch (lineSeparatorType) {
            case "":
                return null;
            case "CR":
                return "\r";
            case "CRLF":
                return "\r\n";
            case "LF":
                return "\n";
            default:
                throw new ToolsException(
                        "invalid lineSeparatorType. lineSeparatorType=" + lineSeparatorType);
        }
    }

    private List<Map<String, Object>> getTemplateContexts(RootObjectsMap rootObjectsMap,
            String objectsName) {
        switch (objectsName) {
            case "<root>": {
                List<Map<String, Object>> templateContexts = new ArrayList<>();
                Map<String, Object> templateContext =
                        new LinkedHashMap<>(rootObjectsMap.getObjectsMap());
                fillMustacheUtils(templateContext);
                templateContexts.add(templateContext);
                return templateContexts;
            }
            case "<test>": {
                List<Map<String, Object>> templateContexts = new ArrayList<>();
                Map<String, Object> templateContext = new LinkedHashMap<>(getTestTemplateContext());
                fillMustacheUtils(templateContext);
                templateContexts.add(templateContext);
                return templateContexts;
            }
            default: {
                List<Map<String, Object>> templateContexts = new ArrayList<>();

                List<Map<String, Object>> objectMaps =
                        rootObjectsMap.getObjectMaps(objectsName).getObjectMaps();
                for (Map<String, Object> objectMap : objectMaps) {
                    Map<String, Object> templateContext = new LinkedHashMap<>(objectMap);
                    fillMustacheUtils(templateContext);
                    templateContexts.add(templateContext);
                }
                return templateContexts;
            }
        }
    }

    private void fillMustacheUtils(Map<String, Object> templateContext) {
        ToolsStringUtils.fillMustacheUtils(templateContext);
        ToolsJavaUtils.fillMustacheUtils(templateContext);
    }

    private Map<String, Object> getTestTemplateContext() {
        Map<String, Object> objectMap = new LinkedHashMap<>();
        return objectMap;
    }

    private Path getTemplatesPath(Map<String, Path> templatePathMap, String templateName) {

        Path templatePath = templatePathMap.get(templateName);
        if (templatePath == null) {
            throw new ToolsException("not found template. templateName=" + templateName);
        }
        return templatePath;
    }

    private void evaluateAllTemplates3(Path basePath) throws IOException {

        List<Path> templatePaths = ToolsUtils.getFilePaths(basePath, "mustache");
        List<Path> csvPaths = ToolsUtils.getFilePaths(basePath, "csv");
        List<Path> tsvPaths = ToolsUtils.getFilePaths(basePath, "tsv");

        SampleToolsApplicationRunner.log.info(
                "evaluate all templates. templates.count={}, csv.count={}, tsv.count={}, basePath={}", //
                templatePaths.size(), csvPaths.size(), tsvPaths.size(), //
                basePath.toAbsolutePath());

        for (Path tsvPath : tsvPaths) {
            SampleToolsApplicationRunner.log.info("tsvPath({})={}",
                    ToolsUtils.getDetectedCharset(tsvPath), tsvPath);
            Table table = TableUtils.readAsTsv(tsvPath, StandardCharsets.UTF_8);
            SampleToolsApplicationRunner.log.info("table={}", table);
            int i = 0;
            for (Object[] values : table.getValues()) {
                int j = 0;
                SampleToolsApplicationRunner.log.info("values[{}].count={}", i, values.length);
                for (Object value : values) {
                    SampleToolsApplicationRunner.log.info("values[{}][{}]={}", i, j, value);
                    j++;
                }
                i++;
            }

            TableUtils.writeAsCsv(//
                    tsvPath.getParent().getParent().resolve("csv").resolve(
                            FilenameUtils.getBaseName(tsvPath.getFileName().toString()) + ".csv"), //
                    table, StandardCharsets.UTF_8);
            TableUtils.writeAsTsv(//
                    tsvPath.getParent().getParent().resolve("tsv").resolve(tsvPath.getFileName()), //
                    table, StandardCharsets.UTF_8);

        }
    }
    //
    // private void test() {
    //
    // CsvMapper mapper = new CsvMapper();
    // mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
    // mapper.schema().
    // // when wrapped as an array, we'll get array/List of arrays/Lists, for example:
    // String[][] rows = mapper.readValue(csvContent, String[][].class);
    // mapper.wr
    //
    // List<Map<String, Object>> dataList = new LinkedList<>();
    // CsvMapper mapper = new CsvMapper();
    // CsvSchema schema = CsvSchema.emptySchema().withoutHeader();
    // MappingIterator<List<String>> iterator =
    // mapper.readerFor(List.class).with(schema).readValues(file);
    // while (iterator.hasNext()) {
    // dataList.add(iterator.next());
    // }
    // return dataList;
    // }
    //

}
