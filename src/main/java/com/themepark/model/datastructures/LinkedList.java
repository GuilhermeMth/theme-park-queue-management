package com.themepark.model.datastructures;

import java.util.NoSuchElementException;

public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);

        if (this.head == null) {
            this.head = newNode;
        } else {
            this.tail.setNext(newNode);
        }

        this.tail = newNode;
        this.size++;
    }

    public void add(int index, T element) {
        if (index < 0 || index > this.size) { // A verificação deve usar this.size
            throw new IndexOutOfBoundsException("Index out of bounds. Must be between 0 and " + this.size);
        }

        Node<T> newNode = new Node<>(element);

        if (index == 0) {
            newNode.setNext(this.head);
            this.head = newNode;

            if (this.tail == null) {
                this.tail = newNode;
            }
        } else {
            Node<T> current = this.head;
            // Percorre até o nó ANTERIOR ao índice (index - 1)
            for (int i = 0; i < index - 1; i++) {
                current = current.getNext();
            }

            newNode.setNext(current.getNext());
            current.setNext(newNode);

            if (index == this.size) { // Verifica se é o novo final
                this.tail = newNode;
            }
        }
        this.size++;
    }

    public T removeFirst() {
        if (this.head == null) {
            throw new NoSuchElementException("The list is empty.");
        }

        T removedElement = this.head.getElement();
        this.head = this.head.getNext();

        if (this.head == null) {
            this.tail = null;
        }

        this.size--;
        return removedElement;
    }

    public boolean remove(T element) {
        Node<T> current = this.head;
        Node<T> previous = null;

        while (current != null) {
            if (current.getElement().equals(element)) {
                if (previous == null) {
                    this.removeFirst();
                } else {
                    previous.setNext(current.getNext());

                    if (current == this.tail) {
                        this.tail = previous;
                    }

                    this.size--;
                }
                return true;
            }

            previous = current;
            current = current.getNext();
        }
        return false;
    }

    public int getIndexOf(T element) {
        Node<T> current = this.head;
        int index = 0;
        while (current != null) {
            if (current.getElement().equals(element)) {
                return index;
            }
            current = current.getNext();
            index++;
        }
        return -1;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "LinkedList{" +
                "head=" + head +
                ", tail=" + tail +
                ", size=" + size +
                '}';
    }
}