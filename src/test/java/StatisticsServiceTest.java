import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class StatisticsServiceTest {
    private StatisticsService statisticsService = new StatisticsService();

    @Test
    public void regularFileStatistics() throws IOException {
        Path filePath = Paths.get("./src/test/resources/regular.txt");
        Map<String, Integer> resultMap = statisticsService.calculateStatistics(filePath);

        assertThat(resultMap.size()).isEqualTo(9);
        assertThat(resultMap.get("text1")).isEqualTo(4);
        assertThat(resultMap.get("text2")).isEqualTo(5);
        assertThat(resultMap.get("text3")).isEqualTo(2);
        assertThat(resultMap.get("text5")).isEqualTo(1);
        assertThat(resultMap.get("text-6")).isEqualTo(1);
        assertThat(resultMap.get(".")).isEqualTo(2);
        assertThat(resultMap.get("!")).isEqualTo(2);
        assertThat(resultMap.get("?")).isEqualTo(2);
        assertThat(resultMap.get(";")).isEqualTo(1);
    }

    // Ideally some SLA could be tested here. But there are no SLA requirements.
    // Common sense idea is to minimize the time of statistics gathering.
    // So just checking that quite big ~100Mb file passes the test.
    @Test
    public void bigFileStatistics() throws IOException {
        Path filePath = Paths.get("./src/test/resources/big.txt");
        Map<String, Integer> resultMap = statisticsService.calculateStatistics(filePath);

        assertThat(resultMap).isNotEmpty();
    }

    @Test
    public void emptyFileStatistics() throws IOException {
        Path filePath = Paths.get("./src/test/resources/empty.txt");
        Map<String, Integer> resultMap = statisticsService.calculateStatistics(filePath);

        assertThat(resultMap).isEmpty();
    }

    @Test
    public void statisticsSort() {
        Map<String, Integer> map = new HashMap<>();
        map.put("text1", 4);
        map.put("text2", 5);
        map.put("text3", 2);
        map.put("text5", 1);
        map.put(".", 2);
        map.put("!", 1);
        map.put("?", 2);
        map.put(";", 1);

        StatisticsEntry[] result = statisticsService.sortStatistics(map);

        assertThat(result).isNotEmpty();
        // Checking all data for first element
        assertThat(result[0].getWord()).isEqualTo("!");
        assertThat(result[0].getCount()).isEqualTo(1);

        // checking texts for other elements to verify the order
        assertThat(result[1].getWord()).isEqualTo(";");
        assertThat(result[2].getWord()).isEqualTo("text5");
        assertThat(result[3].getWord()).isEqualTo(".");
        assertThat(result[4].getWord()).isEqualTo("?");
        assertThat(result[5].getWord()).isEqualTo("text3");
        assertThat(result[6].getWord()).isEqualTo("text1");
        assertThat(result[7].getWord()).isEqualTo("text2");
    }
}
