package com.demo;

import java.util.HashMap;
import java.util.Map;

public class TestSolution {
    public static void main(String[] args) {


    }


    static class LRUCache {
        int capacity;
        Map<Integer, Node> map = new HashMap<>();
        Node head = new Node();
        Node tail = head;
        public LRUCache(int capacity) {
            this.capacity = capacity;
        }

        public int get(int key) {
            Node current = map.getOrDefault(key, null);
            if(current != null) {
                moveToHead(current);
            }
            return current == null ? -1 : current.val;
        }

        public void put(int key, int value) {
            if(map.containsKey(key)) {
                Node node = map.get(key);
                node.val = value;
                moveToHead(node);
            } else {
                if(map.size() == capacity) {
                    Node tail = removeTail();
                    map.remove(tail.key);
                }
                Node node = new Node();
                node.key = key;
                node.val = value;
                map.put(key, node);
                addToHead(node);
            }
        }


        private void moveToHead(Node node) {
            if(head.next == node) return;
            Node pre = node.prev;
            if(node == tail) tail = pre;
            Node next = node.next;
            head.next.prev = node;
            node.next = head.next;
            head.next = node;
            node.prev = head;
            pre.next = next;
            if(next != null) {
                next.prev = pre;
            }

        }

        private void addToHead(Node node) {
            if(head.next == null) {
                head.next = node;
                node.prev = head;
                tail = node;
            } else {
                Node next = head.next;
                head.next = node;
                node.prev = head;
                node.next = next;
                next.prev = node;
            }
        }
        private Node removeTail() {
            Node prev = this.tail.prev;
            if(prev != null) prev.next = null;
            Node currentTail = this.tail;
            this.tail = prev;
            return currentTail;
        }

        class Node {
            public int key;
            public int val;
            public Node next;
            public Node prev;
        }
    }

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
}
