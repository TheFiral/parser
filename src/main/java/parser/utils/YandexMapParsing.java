package parser.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.nodes.Node;
import parser.runnables.ParserRunnable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class YandexMapParsing {

    public static final ConcurrentHashMap<String, Node> setOfText = new ConcurrentHashMap<>();
    public static final ExecutorService executor = Executors.newCachedThreadPool();
    public static final List<Map<String, String>> map = new ArrayList<>();

    public static boolean parsing(String searchPath) throws ExecutionException, InterruptedException {
        for (double x = 60.08; x > 59.83; x -= 0.004)
            executor.execute(new ParserRunnable(x, searchPath));

        while (true) {
            if (((ThreadPoolExecutor) executor).getActiveCount() == 0) {
                break;
            }
        }

        HSSFWorkbook book = ExcelCreator.build(map);
        LocalDateTime local = LocalDateTime.now();
        StringBuilder fileName = new StringBuilder()
                .append(searchPath)
                .append("_D_")
                .append(local.toLocalDate())
                .append("_T_")
                .append(local.toLocalTime().getHour())
                .append("-")
                .append(local.toLocalTime().getMinute())
                .append("-")
                .append(local.toLocalTime().getSecond())
                .append(".xls");

        try {
            book.write(new FileOutputStream(fileName.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

