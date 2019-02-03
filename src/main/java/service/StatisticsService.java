package service;

import exception.ValidationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Service to calculate statistics
 * Note: might be needed an interface(contract) for real functionality.
 */
public class StatisticsService {
    private static final String CHARSET_NAME = "utf8";
    private static final Pattern WORDLIKE_PATTERN = Pattern.compile("\\s+|\r\n|[\n\r\u2028\u2029\u0085]");
    private static final String PUNCTUATION_MARKS = ".,!?;:";
    private static final char DASH = '-';

    /**
     * @param filePath file to analyze for words/punctuation marks frequency
     * @return statistics of word/punctuation marks occurrence frequency.
     */
    public Map<String, Integer> calculateStatistics(Path filePath) throws IOException {
        // validate parameter
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new ValidationException("There is no file named '" + filePath.getFileName() + "'");
        }

        Map<String, Integer> statisticsMap = new HashMap<>();

        try (FileInputStream inputStream = new FileInputStream(file);
             Scanner scanner = new Scanner(inputStream, CHARSET_NAME).useDelimiter(WORDLIKE_PATTERN);) {
            while (scanner.hasNext()) {
                processLineAndEnrichStatistics(scanner.next(), statisticsMap);
            }
            // process suppressed exceptions
            if (scanner.ioException() != null) {
                throw new RuntimeException("", scanner.ioException());
            }
        }
        return statisticsMap;
    }

    private void processLineAndEnrichStatistics(String wordlike, Map<String, Integer> statisticsMap) {
        char[] wordlikeArray = wordlike.toCharArray();
        if (wordlikeArray.length == 1 && DASH == wordlikeArray[0]) {
            addWordToMap(String.valueOf(DASH), statisticsMap);
            return;
        }
        StringBuilder currentWordBuilder = new StringBuilder();
        for (Character currentChar : wordlikeArray) {
            if (PUNCTUATION_MARKS.contains(String.valueOf(currentChar))) {
                if (currentWordBuilder.length() > 0) {
                    addWordToMap(currentWordBuilder.toString(), statisticsMap);
                    // clean up word container
                    currentWordBuilder = new StringBuilder();
                }
                addWordToMap(String.valueOf(currentChar), statisticsMap);
            } else { //this is alphanumeric char
                currentWordBuilder.append(currentChar);
            }
        }
        if (currentWordBuilder.length() > 0) {
            addWordToMap(currentWordBuilder.toString(), statisticsMap);
        }
    }

    private void addWordToMap(String word, Map<String, Integer> statisticsMap) {
        Integer value = statisticsMap.get(word);
        if (value == null) {
            value = 0;
        }
        statisticsMap.put(word, value + 1);
    }
}
