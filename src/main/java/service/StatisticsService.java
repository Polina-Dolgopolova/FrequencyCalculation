package service;

import exception.ValidationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

/**
 *
 * TODO might be needed an interface (contract).
 */
public class StatisticsService {
    private static final String CHARSET_NAME = "utf8";
    private static final String WORDLIKE_PATTERN = "\\s+|\r\n|[\n\r\u2028\u2029\u0085]";
    private static final String PUNCTUATION_MARKS = ".,!?;:-";
    private static final String EMPTY_STRING = "";

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

        Map<String, Long> statisticsMap = new HashMap<>();

        try (FileInputStream inputStream = new FileInputStream(file);
             Scanner scanner = new Scanner(inputStream, CHARSET_NAME);) {
            while (scanner.hasNext(WORDLIKE_PATTERN)) {
                processLineAndEnrichStatistics(scanner.next(WORDLIKE_PATTERN), statisticsMap);
            }
            // process suppressed exceptions
            if (scanner.ioException() != null) {
                throw scanner.ioException();
            }
        }
        // TODO
        return statisticsMap;
    }

    private void processLineAndEnrichStatistics(String wordlike, Map<String, Long> statisticsMap) {
        char[] wordlikeArray = wordlike.toCharArray();
        StringBuilder currentWordBuilder = new StringBuilder();
        for (Character currentChar : wordlikeArray) {
            if (PUNCTUATION_MARKS.contains(String.valueOf(currentChar))) {
                if (currentWordBuilder.length() > 0) {
                    // TODO put word to map
                    // clean up the word
                    currentWordBuilder = new StringBuilder();
                }
                // TODO put p-mark to map
            } else { //this is alphanumeric char
                currentWordBuilder.append(currentChar);
            }
        }
        if (currentWordBuilder.length() > 0) {
            // TODO put word to map
        }
    }
}
