package io.platformportal.pipeline;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PipelineRunnerTest {

    private final PipelineRunner runner = new PipelineRunner();

    @Test
    void transformFiltersInactiveRecords() {
        var input = List.of(
            Map.of("id", 1, "name", "Alice", "score", 95, "active", true),
            Map.of("id", 2, "name", "Bob", "score", 72, "active", false)
        );
        var result = runner.transform(new ArrayList<>(input));
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).get("name"));
    }

    @Test
    void transformAssignsCorrectGrades() {
        var input = List.of(
            new HashMap<>(Map.of("id", 1, "score", 95, "active", true)),
            new HashMap<>(Map.of("id", 2, "score", 82, "active", true)),
            new HashMap<>(Map.of("id", 3, "score", 70, "active", true)),
            new HashMap<>(Map.of("id", 4, "score", 50, "active", true))
        );
        var result = runner.transform(input);
        assertEquals("A", result.get(0).get("grade"));
        assertEquals("B", result.get(1).get("grade"));
        assertEquals("C", result.get(2).get("grade"));
        assertEquals("F", result.get(3).get("grade"));
    }

    @Test
    void extractReturnsSampleDataWhenNoFile() throws Exception {
        var result = runner.extract("non-existent-file.json");
        assertFalse(result.isEmpty());
    }
}
