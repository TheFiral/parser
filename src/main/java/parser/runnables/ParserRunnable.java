package parser.runnables;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import parser.utils.YandexMapParsing;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;

public record ParserRunnable(double x_before, String searchPath) implements Runnable {

    @Override
    public void run() {
        String searchPathUTF_8 = URLEncoder.encode(searchPath, StandardCharsets.UTF_8);
        HashMap<String, Node> tempMap = new HashMap<>();
        for (double y = 30.15; y < 30.5; y += 0.004) {
            try {
                String url = "https://yandex.ru/maps/2/saint-petersburg/search/" + searchPathUTF_8 +
                        "/?ll=" + y + "%2C" + x_before;

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
        }
        YandexMapParsing.executor.execute(new DecomposedNodeRunnable(tempMap));
    }
}
