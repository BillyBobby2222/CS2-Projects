/* Caleb Andrews
   Optimizing Network Packet Encoding
   COP3503 Computer Science 2
   NetworkPacket.java
*/

import java.util.*;

public class NetworkPacket {

    private Map<String, Integer> frequencies;
    private Map<String, String> huffmanCodes;
    private Node root;

    //internal node class for huffman tree
    private class Node implements Comparable<Node> {
        String symbol;
        int frequency;
        Node left, right;

        Node(String symbol, int frequency) {
            this.symbol = symbol;
            this.frequency = frequency;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.frequency, other.frequency);
        }
    }

    //counts the times each string appears, each treated as single symbol
    public void analyzeFrequencies(ArrayList<String> symbols) {
        frequencies = new HashMap<>();
        for (String s : symbols) {
            frequencies.put(s, frequencies.getOrDefault(s, 0) + 1);
        }
    }

    //huffman tree using priority queue.
    public void buildHuffmanTree() {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            pq.add(new Node(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            Node parent = new Node(null, left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;
            pq.add(parent);
        }

        root = pq.poll();
        huffmanCodes = new HashMap<>();
        generateCodes(root, "");
    }

    private void generateCodes(Node node, String code) {
        if (node == null) return;
        if (node.isLeaf()) {
            huffmanCodes.put(node.symbol, code);
            return;
        }
        generateCodes(node.left, code + "0");
        generateCodes(node.right, code + "1");
    }

    //binary string for encoded contents
    public String encode(ArrayList<String> symbols) {
        StringBuilder sb = new StringBuilder();
        for (String s : symbols) {
            sb.append(huffmanCodes.get(s));
        }
        return sb.toString();
    }

    //average bits per symbol
    public double getHuffmanAvg(ArrayList<String> symbols) {
        String encoded = encode(symbols);
        return (double) encoded.length() / symbols.size();
    }


    //compression ratio    
    public double getRatio(double huffmanAvg) {
        int uniqueSymbols = frequencies.size();
        //calculate bits needed for encoding
        double fixedLengthAvg = Math.ceil(Math.log(uniqueSymbols) / Math.log(2));
        return fixedLengthAvg / huffmanAvg;
    }
}