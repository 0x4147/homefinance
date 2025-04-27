package ca.homefinance.helper;

import ca.homefinance.entity.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TransactionCategorizer {

    private static final String MAPPINGS_FILE = "category_mappings.json";
    private Map<String, String> entityToCategory = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    public TransactionCategorizer() {
        loadMappings();
    }

    public Category getCategory(String entityName) {
        String lowerEntity = entityName.toLowerCase();
        for (Map.Entry<String, String> entry : entityToCategory.entrySet()) {
            if (lowerEntity.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void loadMappings() {
        // Loading from resources as a stream
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(MAPPINGS_FILE)) {
            if (inputStream != null) {
                // Read the JSON file into a map
                entityToCategory = objectMapper.readValue(inputStream, new TypeReference<Map<String, String>>() {});
                System.out.println("Loaded mappings: " + entityToCategory);
            } else {
                System.out.println("No mappings file found, starting fresh.");
            }
        } catch (IOException e) {
            System.err.println("Failed to load mappings: " + e.getMessage());
        }
    }

    private void saveNewMapping(String entityKey, String category) {
        entityToCategory.put(entityKey, category);
        try {
            // Writing back the mappings, but consider writing to a different location if using the JAR
            Path path = Paths.get("target/classes/category_mappings.json"); // You can choose a writable location
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), entityToCategory);
            System.out.println("Saved new mapping: " + entityKey + " -> " + category);
        } catch (IOException e) {
            System.err.println("Failed to save new mapping: " + e.getMessage());
        }
    }
}
