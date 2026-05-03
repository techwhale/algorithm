package org.practise.algorithm.leetcode.design.hard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 432. All O`one Data Structure
 *
 * Design a data structure to store the strings' count with the ability to return the strings with minimum and maximum counts.
 *
 * Implement the AllOne class:
 *
 * AllOne() Initializes the object of the data structure.
 * inc(String key) Increments the count of the string key by 1. If key does not exist in the data structure, insert it with count 1.
 * dec(String key) Decrements the count of the string key by 1. If the count of key is 0 after the decrement, remove it from the data structure. It is guaranteed that key exists in the data structure before the decrement.
 * getMaxKey() Returns one of the keys with the maximal count. If no element exists, return an empty string "".
 * getMinKey() Returns one of the keys with the minimum count. If no element exists, return an empty string "".
 * Note that each function must run in O(1) average time complexity.
 *
 *
 *
 * Example 1:
 *
 * Input
 * ["AllOne", "inc", "inc", "getMaxKey", "getMinKey", "inc", "getMaxKey", "getMinKey"]
 * [[], ["hello"], ["hello"], [], [], ["leet"], [], []]
 * Output
 * [null, null, null, "hello", "hello", null, "hello", "leet"]
 *
 * Explanation
 * AllOne allOne = new AllOne();
 * allOne.inc("hello");
 * allOne.inc("hello");
 * allOne.getMaxKey(); // return "hello"
 * allOne.getMinKey(); // return "hello"
 * allOne.inc("leet");
 * allOne.getMaxKey(); // return "hello"
 * allOne.getMinKey(); // return "leet"
 *
 *
 * Constraints:
 *
 * 1 <= key.length <= 10
 * key consists of lowercase English letters.
 * It is guaranteed that for each call to dec, key is existing in the data structure.
 * At most 5 * 104 calls will be made to inc, dec, getMaxKey, and getMinKey.
 */
public class AllOneDataStructure {
    class Node {
        Node prev;
        Node next;
        int count;
        Set<String> keys = new HashSet<>();
        public Node(int count) {
            this.count = count;
        }
    }

    Map<String, Node> map = new HashMap<>();
    Node head;
    Node tail;
    public AllOneDataStructure() {
        head = new Node(-1);
        tail = new Node(-1);
        head.next = tail;
        tail.prev = head;
    }

    public void inc(String key) {
        Node node = map.get(key);
        if (node != null) {
            int count = node.count;
            node.keys.remove(key);
            Node nextNode = node.next;
            if (nextNode == tail || nextNode.count != count + 1) {
                Node newNode = new Node(count + 1);
                newNode.keys.add(key);
                newNode.next = nextNode;
                nextNode.prev = newNode;
                newNode.prev = node;
                node.next = newNode;
                map.put(key, newNode);
            } else {
                nextNode.keys.add(key);
                map.put(key, nextNode);
            }
            if (node.keys.isEmpty()) {
                removeNode(node);
            }
        } else {
            Node firstNode = head.next;
            if (firstNode == tail || firstNode.count > 1) {
                Node newNode = new Node(1);
                newNode.keys.add(key);
                newNode.prev = head;
                newNode.next = firstNode;
                firstNode.prev = newNode;
                head.next = newNode;
                map.put(key, newNode);
            } else {
                firstNode.keys.add(key);
                map.put(key, firstNode);
            }
        }
    }

    private void removeNode(Node node) {
        Node next = node.next;
        Node prev = node.prev;

        prev.next = next;
        next.prev = prev;
    }

    public void dec(String key) {
        Node currNode = map.get(key);
        int count = currNode.count;
        currNode.keys.remove(key);

        if (count == 1) {
            map.remove(key);
        } else {
            Node prevNode = currNode.prev;
            if (prevNode == head || prevNode.count != count - 1) {
                Node node = new Node(count - 1);
                node.next = currNode;
                node.prev = prevNode;
                prevNode.next = node;
                currNode.prev = node;
                node.keys.add(key);
                map.put(key, node);
            } else {
                prevNode.keys.add(key);
                map.put(key, prevNode);
            }
        }

        if (currNode.keys.isEmpty()) {
            removeNode(currNode);
        }
    }

    public String getMaxKey() {
        Node maxNode = tail.prev;
        if (maxNode == head) {
            return "";
        }
        return maxNode.keys.iterator().next();
    }

    public String getMinKey() {
        Node minNode = head.next;
        if (minNode == tail) {
            return "";
        }
        return minNode.keys.iterator().next();
    }
}
