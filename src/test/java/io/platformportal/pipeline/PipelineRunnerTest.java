package io.platformportal.pipeline;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PipelineRunnerTest {

    private final PipelineRunner runner = new PipelineRunner();

    @Test
    void transformFiltersInactiveRecords() {
        List<Map<String, Object>> input = new ArrayList<>();
        Map<String, Object> r1 = new HashMap<>();
        r1.put("id", 1); r1.put("name", "Alice"); r1.put("score", 95); r1.put("active", true);
        Map<String, Object> r2 = new HashMap<>();
        r2.put("id", 2); r2.put("name", "Bob"); r2.put("score", 72); r2.put("active", false);
        input.add(r1); input.add(r2);
        List<Map<String, Object>> result = runner.transform(input);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).get("name"));
    }

    @Test
    void transformAssignsCorrectGrades() {
        List<Map<String, Object>> input = new ArrayList<>();
        for (int[] pair : new int[][]{{1, 95}, {2, 82}, {3, 70}, {4, 50}}) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", pair[0]); row.put("score", pair[1]); row.put("active", true);
            input.add(row);
        }
        List<Map<String, Object>> result = runner.transform(input);
        assertEquals("A", result.get(0).get("grade"));
        assertEquals("B", result.get(1).get("grade"));
        assertEquals("C", result.get(2).get("grade"));
        assertEquals("F", result.get(3).get("grade"));
    }

    @Test
    void extractReturnsSampleDataWhenNoFile() throws Exception {
        List<Map<String, Object>> result = runner.extract("non-existent-file.json");
        assertFalse(result.isEmpty());
    }
}
