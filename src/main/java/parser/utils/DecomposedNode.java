package parser.utils;

import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecomposedNode {

    private final Map<String, String> map;

    private DecomposedNode() {
        this.map = new HashMap<>();
    }

    public static Map<String, String> getMap(Node node) {
        DecomposedNode decomposedNode = new DecomposedNode();
        decomposedNode.findAllNodes(node);
        return decomposedNode.map;
    }

    private void findAllNodes(Node node) {
        List<Node> childNodes = node.childNodes();
        if (!childNodes.isEmpty())
            childNodes.forEach(this::findAllNodes);
        else if (node instanceof TextNode text) {
            map.put(text.parent().attributes().get("class"), text.text());
        }
    }
}
