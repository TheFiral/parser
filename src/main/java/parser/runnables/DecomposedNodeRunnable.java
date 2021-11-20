package parser.runnables;

import org.jsoup.nodes.Node;
import parser.utils.DecomposedNode;
import parser.utils.YandexMapParsing;

import java.util.HashMap;

public record DecomposedNodeRunnable(HashMap<String, Node> nodes) implements Runnable {

    @Override
    public void run() {
        nodes.values().forEach(node ->
                YandexMapParsing.map.add(DecomposedNode.getMap(node)));
    }
}
