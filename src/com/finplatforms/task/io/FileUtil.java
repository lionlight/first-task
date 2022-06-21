package com.finplatforms.task.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {
    private static void writeLinesToFile(List<File> files, Path path) throws IOException {
        List<List<String>> fileLines = new ArrayList<>();

        for (File file : files) {
            fileLines.add(Files.readAllLines(Paths.get(file.getPath()),
                    StandardCharsets.UTF_8));
        }

        File resultFile = new File(path + "//result.txt");

        try (BufferedWriter writer = Files.newBufferedWriter(resultFile.toPath())) {
            for (List<String> lines : fileLines) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        }
    }

    private static Path enterPath() {
        System.out.println("Enter the path to the directory");
        Scanner scanner = new Scanner(System.in);
        return Path.of(scanner.nextLine());
    }

    private static List<File> getAllTextFiles(Path path) {
        try (Stream<Path> streamPaths = Files.walk(path)) {
            return streamPaths.filter(Files::isRegularFile)
                    .filter(Files::isReadable)
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".txt"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void mergeContentsIntoOneTextFile() throws IOException {
        Path path = enterPath();
        List<File> files = getAllTextFiles(path);
        if (files != null) {
            writeLinesToFile(files, path);
        }
    }
}
