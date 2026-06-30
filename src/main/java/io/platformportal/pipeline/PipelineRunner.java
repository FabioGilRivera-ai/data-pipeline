package io.platformportal.pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class PipelineRunner {

    private static final Logger log = LoggerFactory.getLogger(PipelineRunner.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        String inputPath = System.getenv().getOrDefault("INPUT_PATH", "data/input.json");
        String outputPath = System.getenv().getOrDefault("OUTPUT_PATH", "data/output.json");

        log.info("Data Pipeline v1.0.0 starting");
        log.info("Input: {}, Output: {}", inputPath, outputPath);

        PipelineRunner runner = new PipelineRunner();
        runner.run(inputPath, outputPath);
        log.info("Pipeline completed successfully");
    }

    public void run(String inputPath, String outputPath) throws Exception {
        List<Map<String, Object>> records = extract(inputPath);
        log.info("Extracted {} records", records.size());

        List<Map<String, Object>> transformed = transform(records);
        log.info("Transformed to {} records", transformed.size());

        load(transformed, outputPath);
        log.info("Loaded output to {}", outputPath);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> extract(String path) throws Exception {
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            // Return sample data when no input file
            return List.of(
                Map.of("id", 1, "name", "Alice", "score", 95, "active", true),
                Map.of("id", 2, "name", "Bob", "score", 72, "active", false),
                Map.of("id", 3, "name", "Carol", "score", 88, "active", true)
            );
        }
        return mapper.readValue(p.toFile(), List.class);
    }

    public List<Map<String, Object>> transform(List<Map<String, Object>> records) {
        return records.stream()
            .filter(r -> Boolean.TRUE.equals(r.get("active")))
            .map(r -> {
                Map<String, Object> out = new LinkedHashMap<>(r);
                int score = ((Number) r.getOrDefault("score", 0)).intValue();
                out.put("grade", score >= 90 ? "A" : score >= 80 ? "B" : score >= 70 ? "C" : "F");
                out.put("processed_at", new Date().toString());
                return out;
            })
            .collect(Collectors.toList());
    }

    public void load(List<Map<String, Object>> records, String path) throws Exception {
        Path p = Paths.get(path);
        Files.createDirectories(p.getParent());
        mapper.writerWithDefaultPrettyPrinter().writeValue(p.toFile(), records);
    }
}
