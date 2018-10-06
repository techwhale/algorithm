package org.practise.algorithm.leetcode.linkedlist;

import java.util.HashMap;
import java.util.Map;

/**
 * https://leetcode.com/problems/lfu-cache/description/
 * 
 */
public class LRUCache {
    int maxCapacity;
    int currentCapacity;
    private Map<Integer, Node> cache;

    private Node head;

    private Node tail;

    class Node {
        int key;
        int value;
        Node prev;
        Node next;
        public Node() {}

        public Node(int key, int val) {
            this.key = key;
            this.value = val;
        }
    }

    public LRUCache(int capacity) {
        cache = new HashMap<>(capacity);

        maxCapacity = capacity;
        currentCapacity = 0;

        head = new Node();
        head.prev = null;

        tail = new Node();
        tail.next = null;

        head.next = tail;
        tail.prev = head;
    }

    public int get(int key) {
        if (! cache.containsKey(key)) {
            return -1;
        }
        Node node = cache.get(key);
        moveNodeToHead(node);
        return node.value;
    }

    public void put(int key, int value) {
        if (! cache.containsKey(key)) {
            currentCapacity++;
            Node node = new Node(key, value);

            addNodeToHead(node);
            cache.put(key, node);

            if (currentCapacity > maxCapacity) {
                Node lastNode = popLastNode();
                cache.remove(lastNode.key);
                currentCapacity--;
            }

        } else {
            Node node = cache.get(key);
            node.value = value;
            moveNodeToHead(node);
        }
    }


    private void addNodeToHead(Node node) {
        Node nextNode = head.next;
        node.next = nextNode;
        nextNode.prev = node;

        head.next = node;
        node.prev = head;
    }

    private void removeNode(Node node) {
        Node next = node.next;
        Node prev = node.prev;

        prev.next = next;
        next.prev = prev;
    }

    private void moveNodeToHead(Node node) {
        removeNode(node);
        addNodeToHead(node);
    }


    private Node popLastNode() {
        Node lastNode = tail.prev;
        removeNode(lastNode);
        return lastNode;
    }
}
