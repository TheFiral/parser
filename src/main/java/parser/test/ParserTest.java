package parser.test;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import parser.utils.YandexMapParsing;
import parser.utils.DecomposedNode;
import parser.utils.ExcelCreator;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

public class ParserTest {

    private static final HashMap<String, Node> tempMap = new HashMap<>();
    private static final List<Map<String, String>> map = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        String filter = URLEncoder.encode("стрит", StandardCharsets.UTF_8);
        try {
            String url = "https://yandex.ru/maps/2/saint-petersburg/search/" + filter +
                    "/?ll=" + 30.3 + "%2C" + 60;

            Document doc = Jsoup.connect(url)
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("http://www.google.com")
                    .get();

            Objects.requireNonNull(doc
                            .getElementsByClass("search-list-view__list")
                            .first())
                    .childNodes()
                    .forEach(node -> {
                        if (!YandexMapParsing.setOfText.containsKey(((Element) node).text())) {
                            YandexMapParsing.setOfText.put(((Element) node).text(), node);
                            tempMap.put(((Element) node).text(), node);
                        }
                    });
        } catch (Exception ignored) {

        }
        tempMap.values().forEach(node ->
                map.add(DecomposedNode.getMap(node)));


        HSSFWorkbook book = ExcelCreator.build(map);
        LocalDateTime local = LocalDateTime.now();
        StringBuilder sb = new StringBuilder()
                .append("result-D_")
                .append(local.toLocalDate())
                .append("-T_")
                .append(local.toLocalTime().getHour())
                .append("-")
                .append(local.toLocalTime().getMinute())
                .append("-")
                .append(local.toLocalTime().getSecond())
                .append(".xls");
        book.write(new FileOutputStream(sb.toString()));
    }
}
