package org.practise.algorithm.leetcode.design;

import java.util.Iterator;
import java.util.LinkedList;

public class MyHashMap {
    private LinkedList<Node>[] nodes;
    /** Initialize your data structure here. */
    public MyHashMap() {
        nodes = new LinkedList[10000];
    }

    /** value will always be non-negative. */
    public void put(int key, int value) {
        int index = findIndex(key);
        if (nodes[index] == null) {
            nodes[index] = new LinkedList<>();
        }
        boolean notUpdated = true;
        for (Node node : nodes[index]) {
            if (node.key == key) {
                node.value = value;
                notUpdated = false;
            }
        }
        if (notUpdated) {
            nodes[index].offer(new Node(key, value));
        }
    }


    private int findIndex(int key) {
        return Integer.hashCode(key) % nodes.length;
    }

    /** Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key */
    public int get(int key) {
        int index = findIndex(key);
        if (nodes[index] == null) return -1;
        for (Node node : nodes[index]) {
            if (node.key == key) {
                return node.value;
            }
        }
        return -1;
    }

    /** Removes the mapping of the specified value key if this map contains a mapping for the key */
    public void remove(int key) {
        int index = findIndex(key);
        if (nodes[index] == null) return;
        Iterator iterator = nodes[index].iterator();
        while (iterator.hasNext()) {
            Node node =  (Node) iterator.next();
            if (node.key == key) {
                iterator.remove();
                break;
            }
        }
    }

    class Node {
        int key;
        int value;
        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
