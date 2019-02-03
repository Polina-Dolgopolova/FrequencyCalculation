import exception.ValidationException;
import service.StatisticsService;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class Application {
    private static StatisticsService statisticsService = new StatisticsService();

    public static void main(String[] args) {
        if (args.length < 1) {
            throw new ValidationException("No needed argument defined");
        }
        Path filePath = Paths.get(args[0]);

        try {
            Map<String, Long> map = statisticsService.calculateStatistics(filePath);
        } catch (IOException ex) {
            // TODO
        }

        // TODO output
    }
}
