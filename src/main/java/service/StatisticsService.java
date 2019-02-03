package service;

import exception.ValidationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * TODO might be needed an interface (contract).
 */
public class StatisticsService {
    private static final String CHARSET_NAME = "utf8";

    /**
     * @param filePath file to analyze for words/punctuation marks frequency
     * @return TODO
     */
    // TODO check if Long/Integer
    public Map<String, Long> calculateStatistics(Path filePath) throws IOException {
        // validate parameter
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new ValidationException("There is no file named '" + filePath.getFileName() + "'");
        }

        Map<String, Long> statisticsMap = new HashMap();

        try (FileInputStream inputStream = new FileInputStream(file);
             Scanner scanner = new Scanner(inputStream, CHARSET_NAME);) {
            while (scanner.hasNextLine()) {
                processLineAndEnrichStatistics(scanner.nextLine(), statisticsMap);
            }
            // process suppressed exceptions
            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        }
        // TODO
        return statisticsMap;
    }

    private void processLineAndEnrichStatistics(String line, Map<String, Long> statisticsMap) {

    }
}
