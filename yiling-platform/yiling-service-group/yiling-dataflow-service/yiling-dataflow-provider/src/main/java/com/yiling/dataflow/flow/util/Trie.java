package com.yiling.dataflow.flow.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Node {
    public Map<String, Node> nexts; // 子节点
    public String            type;
    public String            key;
    public int               end;
    public String            endStr;

    public Node() {
        this.nexts = new HashMap<String, Node>();
        this.end = 0;
    }
}

public class Trie {
    Node root = new Node();

    public void insert(String word, String type, String endStr) {
        if (word == null)
            return;
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            String str = "" + word.charAt(i);
            if (node.nexts.get(str) == null)
                node.nexts.put(str, new Node());
            node = node.nexts.get(str);
        }
        node.key = word;
        node.type = type;
        node.endStr = endStr;
        node.end = 1;
    }

    public boolean startWith(String preWord) {
        Node node = root;
        for (int i = 0; i < preWord.length(); i++) {
            String str = "" + preWord.charAt(i);
            if (node.nexts.get(str) == null)
                return false;
            node = node.nexts.get(str);
        }
        return true;
    }

    List<String> list = new ArrayList<String>();

    public Node getStartData(String preword) {
        Node node = root;
        for (int i = 0; i < preword.length(); i++) {
            String str = "" + preword.charAt(i);
            if (node.nexts.get(str) == null)
                return null;
            node = node.nexts.get(str);
            if (node.end == 1) {
                return node;
            }

        }
        return node;
    }


    public Node getMData(String preword) {
        Node node = root;
        for (int i = 0; i < preword.length(); i++) {
            String str = "" + preword.charAt(i);
            if (node.nexts.get(str) == null) {
                Node node1 = getMData(preword.substring(1));
                if (node1 == null) {
                    return null;
                }
                if (node1.end == 1) {
                    return node1;
                }
            }
            node = node.nexts.get(str);
            if (node == null) {
                return null;
            }
            if (node.end == 1) {
                return node;
            }
        }
        return node;
    }

    public List<String> getData(String preword) {
        list.clear();
        if (!startWith(preword))
            return null;
        else {
            StringBuilder str = new StringBuilder("");
            str.append(preword);
            Node node = root;
            for (int i = 0; i < preword.length(); i++)
                node = node.nexts.get("" + preword.charAt(i));
            dfs(node, str);
        }
        return list;
    }

    private void dfs(Node root, StringBuilder str) {
        if (root.end == 1) {
            list.add(str.toString());
            if (root.nexts.size() == 0)
                return;
        }
        Node node = root;
        for (String s : node.nexts.keySet()) {
            str.append(s);
            dfs(node.nexts.get(s), str);
            str.delete(str.length() - 1, str.length());
        }
    }
}
