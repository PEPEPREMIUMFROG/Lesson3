package Lesson13;

public class SimpleQueue {
    int size;
    Node head;
    Node tail;

    public SimpleQueue(int size) {
        this.size = 0;
        this.head = null;
        this.tail = null;
    }

    public boolean add(int item) {
        Node newNode = new Node(item);
        if (tail != null)
            tail.next = newNode;
        else
            head = newNode;
        tail = newNode;
        size++;
        return true;
    }

    public Integer poll(){
        if (isEmpty()) {
            return null;
        }
        int result = head.data;
        head = head.next;
        if (head == null)
            tail = null;
        size--;
        return result;
    }


    public Integer peek() {
        return (head != null) ? head.data : null;
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

        public Node(Integer data) {
            this.data = data;
            this.next = null;
        }
    }
}
