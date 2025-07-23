package Lesson13;

public class SimpleDoubleQueue {
    int size;
    Node head;
    Node tail;

    public SimpleDoubleQueue(int size) {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public boolean addLast(int item) {
        Node newNode = new Node(item);
        if (tail != null) tail.next = newNode;
        else head = newNode;
        tail = newNode;
        size++;
        return true;
    }

    public boolean addFirst(int item) {
        Node newNode = new Node(item);
        if (head != null) {
            head.prev = newNode;
            newNode.next = head;
        } else {
            tail = newNode;
        }
        head = newNode;
        size++;
        return true;
    }


    public Integer pollFirst() {
        if (isEmpty()) {
            return null;
        }
        int result = head.data;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }
        size--;
        return result;
    }


    public Integer pollLast() {
        if (isEmpty()) {
            return null;
        }
        int result = tail.data;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return result;
    }


    public Integer peekFirst() {
        return (head != null) ? head.data : null;
    }

    public Integer peekLast() {
        return (tail != null) ? tail.data : null;
    }


    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }


    private static class Node {
        private Integer data;
        private Node next;
        private Node prev;

        public Node(Integer data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
}
