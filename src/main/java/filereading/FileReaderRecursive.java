package filereading;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileReaderRecursive {

    /**
     * Recursively reads all files under the given directory path.
     * @param startPath The directory or file path to start reading from.
     * @return A Map where the key is the absolute path of the file,
     *         and the value is the file content as a String.
     * @throws IOException If any file reading fails.
     */
    public static Map<String, String> readFiles(String startPath) throws IOException {
        Map<String, String> fileContents = new LinkedHashMap<>();

        Path start = Paths.get(startPath);

        if (!Files.exists(start)) {
            throw new IllegalArgumentException("Path does not exist: " + startPath);
        }

        if (Files.isRegularFile(start)) {
            fileContents.put(start.toAbsolutePath().toString(), Files.readString(start));
        } else {
            // Walk the directory recursively
            try (var paths = Files.walk(start)) {
                paths.filter(Files::isRegularFile)                    // only regular files
                        .filter(path -> path.toString().endsWith(".java")) // only .java files
                        .forEach(path -> {
                            try {
                                fileContents.put(path.toAbsolutePath().toString(), Files.readString(path));
                            } catch (IOException e) {
                                throw new RuntimeException("Failed to read file: " + path, e);
                            }
                        });
            }
        }

        return fileContents;
    }

    /**
     * Builds a combined string in the format:
     * ### {path}
     * {content}
     */
    public static String buildCombinedString(Map<String, String> files) {
        return files.entrySet()
                .stream()
                .map(entry -> "### " + entry.getKey() + "\n" + entry.getValue())
                .collect(Collectors.joining("\n\n"));
    }

    // Example usage
//    public static void main(String[] args) {
//        try {
//            String directory = "C:\\Users\\albukows\\SHIP_IT_DAY_2025_WORKSPACE\\UMLDiagramGeneration";
//            Map<String, String> files = readFiles(directory);
//
//            String combined = buildCombinedString(files);
//            System.out.println(combined);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
