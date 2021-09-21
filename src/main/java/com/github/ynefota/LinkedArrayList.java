package com.github.ynefota;

import java.io.Serializable;
import java.util.*;

public class LinkedArrayList<E> extends AbstractList<E> implements Iterable<E>, Serializable, List<E>, Cloneable {



    private int size;
    private Node head;
    private Node tail;
    private final int DEFAULT_SIZE;

    public LinkedArrayList() {
        this(10);
    }

    private LinkedArrayList(int default_size) {
        DEFAULT_SIZE = default_size;
        head = new Node();
        tail = head;
        size = 0;
    }

    private void tryAdd() {
        size++;

        if (size % DEFAULT_SIZE == 0) {
            appendNode();
        }
    }

    private void tryRemove() {
        --size;

        if (size % DEFAULT_SIZE == 9) {
            removeNode();
        }
    }

    private int nodes() {
        return size / DEFAULT_SIZE;
    }

    private Node getNode(int position) {
        Node tmp;
        if (position > nodes() / 2) {
            tmp = tail;
            for (int i = nodes(); i > position; i--)
                tmp = tmp.behind;
        } else {
            tmp = head;
            for (int i = 0; i < position; i++)
                tmp = tmp.next;
        }
        return tmp;
    }

    private void appendNode() {
        Node newNode = new Node();
        addNode(newNode);
    }

    private void addNode(Node node) {
        tail.next = node;
        node.behind = tail;
        tail = node;
    }

    private void removeNode() {
        Node newTail = tail.behind;
        newTail.next = null;
        tail = newTail;
    }

    private void moveUp(int index) {

        if (index > size || index < 0) throw new IndexOutOfBoundsException();
        int nodePosition = index / DEFAULT_SIZE;

        Node current = getNode(nodePosition);

        E valueNext = null;
        E valueNow;

        for (int i = index; i < size; i++) {
            int newNodeIndex = i / DEFAULT_SIZE;
            if (newNodeIndex > nodePosition) {
                nodePosition = newNodeIndex;
                current = current.next;
            }

            int nodeIndex = i % DEFAULT_SIZE;
            valueNow = current.values[nodeIndex];
            current.values[nodeIndex] = valueNext;
            valueNext = valueNow;
        }
        tail.values[size % DEFAULT_SIZE] = valueNext;

    }

    private void moveDown(int index) {

        if (index > size || index < 0) throw new IndexOutOfBoundsException();
        int nodePosition = size / DEFAULT_SIZE;

        Node current = tail;

        E valueBefore = tail.values[size % DEFAULT_SIZE];
        E valueNow;

        for (int i = size - 1; i >= index; i--) {

            int newNodeIndex = i / DEFAULT_SIZE;
            if (newNodeIndex < nodePosition) {
                nodePosition = newNodeIndex;
                current = current.behind;
            }

            int nodeIndex = i % DEFAULT_SIZE;
            valueNow = current.values[nodeIndex];
            current.values[nodeIndex] = valueBefore;
            valueBefore = valueNow;
        }
        if (index != 0) {
            current.values[index] = valueBefore;
        }
    }

    @Override
    public boolean add(E value) {
        int nodeIndex = size % DEFAULT_SIZE;

        tail.values[nodeIndex] = value;

        tryAdd();
        return true;
    }

    @Override
    public E get(int index) {
        Node node = getNode(index / DEFAULT_SIZE);
        return node.values[index % DEFAULT_SIZE];
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size()) throw new IndexOutOfBoundsException();

        int nodeIndex = index % DEFAULT_SIZE;
        Node node = getNode(index / DEFAULT_SIZE);

        E oldElement = node.values[nodeIndex];

        node.values[nodeIndex] = element;

        return oldElement;
    }

    /**
     * Es können Duplikate und null-Werte der Liste hinzugefügt werden
     */
    @Override
    public void add(int index, E value) {
        moveUp(index);
        getNode(index / DEFAULT_SIZE).values[index % DEFAULT_SIZE] = value;
        tryAdd();
    }

    @Override
    public E remove(int index) {
        Node node = getNode(index / DEFAULT_SIZE);

        int nodeIndex = index % DEFAULT_SIZE;
        E element = node.values[nodeIndex];
        moveDown(index);

        tryRemove();

        return element;
    }

    @Override
    public int indexOf(Object o) {
        int nodeIndex = 0;
        Node current = head;

        for (int i = 0; i < size; i++) {
            int newNodeIndex = i / DEFAULT_SIZE;
            if (newNodeIndex > nodeIndex) {
                nodeIndex = newNodeIndex;
                current = current.next;
            }
            if (current.values[i % DEFAULT_SIZE].equals(o)) {
                return i;
            }
        }
        throw new NullPointerException();
    }

    @Override
    public int lastIndexOf(Object o) {
        int nodePosition = size / DEFAULT_SIZE;
        Node current = tail;

        for (int i = size - 1; i >= 0; i--) {
            if (i / DEFAULT_SIZE != nodePosition) {
                current = current.behind;
                nodePosition--;
            }
            if (current.values[i % DEFAULT_SIZE].equals(o)) {
                return i;
            }
        }
        throw new NullPointerException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {

        if (index < 0 || index > size()) throw new IndexOutOfBoundsException();

        E[] cArray = (E[]) c.toArray();

        int nodeIndex = index / DEFAULT_SIZE;
        Node current = getNode(nodeIndex);

        for (int i = index; index < cArray.length + index; i++) {

            int newNodeIndex = i / DEFAULT_SIZE;
            if (nodeIndex < newNodeIndex) {
                nodeIndex = newNodeIndex;
                current = current.next;
            }

            moveUp(i);
            current.values[i % DEFAULT_SIZE] = cArray[i];

            tryAdd();
        }
        return true;
    }

    @Override
    public Iterator<E> iterator() {
        return new ThisIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new ThisIterator(index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {

        if (fromIndex < 0 || toIndex > size) throw new IndexOutOfBoundsException();
        if (fromIndex > toIndex) throw new IllegalArgumentException();

        LinkedArrayList<E> subList = new LinkedArrayList<>();

        int nodeIndex = fromIndex / DEFAULT_SIZE;
        Node current = getNode(nodeIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            int newNodeIndex = i / DEFAULT_SIZE;
            if (newNodeIndex < nodeIndex) {
                nodeIndex = newNodeIndex;
                current = current.behind;
            }

            subList.add(current.values[i % DEFAULT_SIZE]);
        }
        return subList;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(Object o) {
        int nodeIndex = 0;
        Node current = head;

        for (int i = 0; i < size; i++) {
            int newNodeIndex = i / DEFAULT_SIZE;
            if (newNodeIndex > nodeIndex) {
                nodeIndex = newNodeIndex;
                current = current.next;
            }
            if (current.values[i % DEFAULT_SIZE].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int i = 0;
        for (Node x = head; x != null; x = x.next) {
            for (int j = 0; j < DEFAULT_SIZE && i < size; j++) {
                array[i++] = x.get(j);
            }
        }
        return array;
    }

    @Override
    public boolean remove(Object o) {
        try {
            remove(this.indexOf(o));
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        LinkedArrayList<E> clone = (LinkedArrayList<E>) super.clone();

        clone.head = head;
        clone.tail = head;


        if (head.next != null) {
            for (Node node = head.next; node.next != null; node = node.next) {
                clone.addNode(node);
            }
        }

        clone.tryAdd();
        clone.size = size;

        return clone;
    }

    class Node implements RandomAccess {
        public Node next;
        public Node behind;
        private E[] values;

        public Node() {
            values = (E[]) new Object[DEFAULT_SIZE];
        }

        public E get(int index) {
            return values[index];
        }

        public void set(int index, E e) {
            this.values[index] = e;
        }

    }

    class ThisIterator implements ListIterator<E> {

        private int currentIndex;
        private int absolutIndex;
        private int lastIndex;
        private Node currentNode;

        public ThisIterator(int index) {
            super();
            currentIndex = index % DEFAULT_SIZE;
            absolutIndex = index;
            currentNode = getNode(index / DEFAULT_SIZE);
        }

        public ThisIterator() {
            this(0);
        }

        @Override
        public boolean hasNext() {
            return absolutIndex < size && currentNode != null;
        }

        @Override
        public E next() {
            E next = currentNode.values[currentIndex++];
            lastIndex = absolutIndex;
            absolutIndex++;
            if (currentIndex >= DEFAULT_SIZE) {
                currentIndex = 0;
                currentNode = currentNode.next;
            }
            return next;
        }

        @Override
        public boolean hasPrevious() {
            return absolutIndex != 0 && currentNode != null;
        }

        @Override
        public E previous() {
            E previous = currentNode.values[currentIndex--];
            lastIndex = absolutIndex;
            absolutIndex--;
            if (currentIndex < 0) {
                currentIndex = 9;
                currentNode = currentNode.behind;
            }
            return previous;
        }

        @Override
        public int nextIndex() {
            return absolutIndex + 1;
        }

        @Override
        public int previousIndex() {
            return absolutIndex - 1;
        }

        @Override
        public void remove() {
            LinkedArrayList.this.remove(lastIndex);
        }

        @Override
        public void set(E e) {
            LinkedArrayList.this.set(lastIndex, e);
        }

        @Override
        public void add(E e) {
            LinkedArrayList.this.add(lastIndex, e);
        }
    }
}