import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.TreeSet;

public class Application {
    private static StatisticsService statisticsService = new StatisticsService();

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No file path argument defined");
        }
        Path filePath = Paths.get(args[0]);

        try {
            long start = System.currentTimeMillis();

            TreeSet<StatisticsEntry> sortedStatistics = statisticsService.sortStatistics(statisticsService.calculateStatistics(filePath));
            if (sortedStatistics.size() == 0) {
                System.out.println("File " + filePath.getFileName() + " is empty, no data available.");
                return;
            }

            System.out.println("===================== " + filePath.getFileName() + " statistics =====================");
            System.out.println("Frequency | Word | Length");

            sortedStatistics.forEach(entry ->
                    System.out.println(entry.getCount() + " | " + entry.getWord() + " | " + entry.getWord().length()));

            System.out.println("Time to calculate: " + (System.currentTimeMillis() - start));

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("An error occurred while calculating statistics");
        }
    }
}
