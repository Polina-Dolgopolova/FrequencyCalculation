import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Service to calculate and sort statistics.
 * Note: might be needed an interface(contract) for real functionality.
 */
class StatisticsService {
    private static final String CHARSET_NAME = "utf8";
    private static final Pattern WORDLIKE_PATTERN = Pattern.compile("\\s+|\r\n|[\n\r\u2028\u2029\u0085]");
    private static final Set<Character> PUNCTUATION_MARKS = new HashSet<>();
    private static final String DASH = "-";

    static {
        PUNCTUATION_MARKS.add('.');
        PUNCTUATION_MARKS.add(',');
        PUNCTUATION_MARKS.add(';');
        PUNCTUATION_MARKS.add(':');
        PUNCTUATION_MARKS.add('!');
        PUNCTUATION_MARKS.add('?');
    }

    /**
     * @param filePath file to analyze for words/punctuation marks frequency
     * @return statistics of word/punctuation marks occurrence frequency.
     */
    Map<String, Integer> calculateStatistics(Path filePath) throws IOException {
        // validate parameter
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new ValidationException("There is no file named '" + filePath.getFileName() + "'");
        }

        Map<String, Integer> statisticsMap = new HashMap<>();
        // Using stream & scanner to avoid loading the whole file into memory at once
        try (FileInputStream inputStream = new FileInputStream(file);
             Scanner scanner = new Scanner(inputStream, CHARSET_NAME).useDelimiter(WORDLIKE_PATTERN);) {
            while (scanner.hasNext()) {
                processLineAndEnrichStatistics(scanner.next(), statisticsMap);
            }
            // process suppressed exceptions
            if (scanner.ioException() != null) {
                throw new RuntimeException("An error occurred while reading data from file.", scanner.ioException());
            }
        }
        return statisticsMap;
    }

    private void processLineAndEnrichStatistics(String wordlike, Map<String, Integer> statisticsMap) {
        if (wordlike.equals(DASH)) {
            addWordToMap(wordlike, statisticsMap);
            return;
        }
        StringBuilder currentWordBuilder = new StringBuilder();
        for (Character currentChar : wordlike.toCharArray()) {
            if (PUNCTUATION_MARKS.contains(currentChar)) {
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

    TreeSet<StatisticsEntry> sortStatistics(Map<String, Integer> map) {
        // this approach should provide O(log(n)) complexity for sorting
        return map.entrySet().stream()
                .map(entry -> new StatisticsEntry(entry.getKey(), entry.getValue()))
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
