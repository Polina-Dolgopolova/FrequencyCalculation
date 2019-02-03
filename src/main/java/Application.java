import exception.ValidationException;
import service.StatisticsService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class Application {
    private static StatisticsService statisticsService = new StatisticsService();

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No file path argument defined");
        }
        Path filePath = Paths.get(args[0]);

        try {
            StatisticsEntry[] sortedStatistics = sortStatistics(statisticsService.calculateStatistics(filePath));
            if (sortedStatistics.length == 0) {
                System.out.println("File " + filePath.getFileName() + " is empty, no data available.");
                return;
            }

            System.out.println("===================== " + filePath.getFileName() + " Statistics =====================");
            System.out.println("Frequency | Word | Length");

            Stream.of(sortedStatistics).forEach(entry ->
                    System.out.println(entry.getCount() + " | " + entry.getWord() + " | " + entry.getWord().length()));

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("An error occurred while calculating statistics");
        }
    }

    private static StatisticsEntry[] sortStatistics(Map<String, Integer> map) {
        StatisticsEntry[] statisticsEntryArray = map.entrySet().stream()
                .map(entry -> new StatisticsEntry(entry.getKey(), entry.getValue()))
                .toArray(size -> new StatisticsEntry[size]);

        // TODO comment
        Arrays.parallelSort(statisticsEntryArray);

        return statisticsEntryArray;
    }

    private static final class StatisticsEntry implements Comparable<StatisticsEntry> {
        private final String word;
        private final Integer count;

        StatisticsEntry(String word, Integer count) {
            this.word = word;
            this.count = count;
        }

        String getWord() {
            return word;
        }
        Integer getCount() {
            return count;
        }

        @Override
        public int compareTo(StatisticsEntry o) {
            Integer currentCount = getCount();
            Integer thatCount = o.getCount();
            if (!currentCount.equals(thatCount)) {
                return currentCount.compareTo(thatCount);
            }
            return getWord().compareTo(o.getWord());
        }
    }
}
