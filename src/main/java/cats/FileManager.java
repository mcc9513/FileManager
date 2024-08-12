package cats;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.StandardCopyOption;



public class FileManager {
    private static final CustomLogger customLogger = new CustomLogger();
    private final Path catsDirectory = Paths.get("src/main/Cats");


    public FileManager() {
        if (!Files.exists(catsDirectory)) {
            try {
                Files.createDirectory(catsDirectory);
            } catch (IOException e) {
                customLogger.logError("Could not create Cats directory", e);
            }
        }
    }

    public Path getPath(String fileName) {
        return catsDirectory.resolve(fileName);
    }


    public List<String> displayDirectoryContents() {
        List<String> contents = new ArrayList<>();
        System.out.println("Checking contents of directory: " + catsDirectory.toAbsolutePath()); // Debugging line
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(catsDirectory)) {
            for (Path entry : stream) {
                System.out.println("Checking entry: " + entry.toAbsolutePath()); // Debugging line
                String entryType = Files.isDirectory(entry) ? "Directory" : "File";
                String fileInfo = entryType + ": " + entry.getFileName() +
                        ", Size: " + Files.size(entry) +
                        ", Last Modified: " + Files.getLastModifiedTime(entry);
                contents.add(fileInfo);
                System.out.println(fileInfo); // Debugging line
            }
        } catch (IOException e) {
            customLogger.logError("Error reading directory contents", e);
        }
        return contents;
    }

    public String readFileContents(String fileName) {
        Path filePath = catsDirectory.resolve(fileName);
        StringBuilder fileContents = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                fileContents.append(line).append("\n");
            }
        } catch (IOException e) {
            customLogger.logError("Error reading file contents", e);
            return "Error reading file: " + e.getMessage();
        }
        return fileContents.toString();
    }

    public void copyFile(String sourceFileName, String targetFileName) {
        Path source = catsDirectory.resolve(sourceFileName);
        Path target = catsDirectory.resolve(targetFileName);
        try {
            Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
            customLogger.logInfo("Copied " + source + " to " + target);
        } catch (IOException e) {
            customLogger.logError("Error copying file", e);
        }
    }

    public void moveFile(String sourceFileName, String targetFileName) {
        Path source = catsDirectory.resolve(sourceFileName);
        Path target = catsDirectory.resolve(targetFileName);
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            customLogger.logInfo("Moved " + source + " to " + target);
        } catch (IOException e) {
            customLogger.logError("Error moving file", e);
        }
    }

    public void deleteFile(String fileName) {
        Path file = catsDirectory.resolve(fileName);
        try {
            Files.delete(file);
            customLogger.logInfo("Deleted " + file);
        } catch (IOException e) {
            customLogger.logError("Error deleting file", e);
        }
    }

    public void createDirectory(String dirName) {
        Path dir = catsDirectory.resolve(dirName);
        try {
            Files.createDirectory(dir);
            customLogger.logInfo("Created directory: " + dir.toAbsolutePath());
            if (Files.isDirectory(dir)) {
                System.out.println("Directory created successfully: " + dir.toAbsolutePath());
            } else {
                System.out.println("Failed to create directory: " + dir.toAbsolutePath());
            }
        } catch (IOException e) {
            customLogger.logError("Error creating directory", e);
        }
    }


    public void deleteDirectory(String dirName) {
        Path dir = catsDirectory.resolve(dirName);
        try {
            Files.delete(dir);
            customLogger.logInfo("Deleted directory " + dir);
        } catch (IOException e) {
            customLogger.logError("Error deleting directory", e);
        }
    }

    public List<String> searchFiles(String query) {
        List<String> results = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(catsDirectory, "*" + query + "*")) {
            for (Path entry : stream) {
                results.add(entry.getFileName().toString());
            }
        } catch (IOException e) {
            customLogger.logError("Error searching files", e);
        }
        return results;
    }
}
