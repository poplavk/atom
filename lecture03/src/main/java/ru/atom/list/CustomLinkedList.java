package ru.atom.list;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class CustomLinkedList<E> implements List<E> {
    private int size = 0;
    private ListNode header = new ListNode(null, null, null);
    private ListNode lastNode = header;
//    private ListNode[] listNodes = new ListNode[capacity];

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public boolean contains(Object o) {
        ListNode currentNode = header.getNextNode();
        if (currentNode.getValue() == o) {
            return true;
        }
        for (int i = 1; i < size - 1; i++) {
            currentNode = currentNode.getNextNode();
            if (currentNode.getValue() == o) {
                return true;
            }
        }
        return false;

    }

    @Override
    public Iterator<E> iterator() {
        throw new NotImplementedException();
    }

    @Override
    public boolean add(E e) {
        try {
            if (this.isEmpty()) {
                lastNode = new ListNode(e, null, header);
                header.setNextNode(lastNode);
                size++;
                return true;
            } else {
                ListNode newListNode = new ListNode(e, null, lastNode);
                lastNode.setNextNode(newListNode);
                lastNode = newListNode;
                size++;
                return true;
            }
        } catch (Exception exception) {
            return false;
        }

    }

    @Override
    public boolean remove(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new NotImplementedException();
    }

    @Override
    public void clear() {
        int size = 0;
        header = new ListNode(null, null, null);
        lastNode = header;
    }

    @Override
    public E get(int index) {
        if (index == 0) {
            return (E) header.getNextNode().getValue();
        }
        if (index == size - 1) {
            return (E) lastNode.getNextNode().getValue();
        }
        ListNode ptrNode = header.getNextNode();
        for (int i = 1; i < index + 1; i++) {
            ptrNode = ptrNode.getNextNode();
        }
        return (E) ptrNode.getValue();

    }

    @Override
    public int indexOf(Object o) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        try {
            for (E e : c) {
                this.add(e);
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }






    /*
      !!! Implement methods below Only if you know what you are doing !!!
     */

    /**
     * Do not implement
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    /**
     * Do not implement
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    /**
     * Do not implement
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    /**
     * Do not implement
     */
    @Override
    public void add(int index, E element) {
    }

    /**
     * Do not implement
     */
    @Override
    public E remove(int index) {
        return null;
    }

    /**
     * Do not implement
     */
    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    /**
     * Do not implement
     */
    @Override
    public ListIterator<E> listIterator() {
        return null;
    }

    /**
     * Do not implement
     */
    @Override
    public ListIterator<E> listIterator(int index) {
        return null;
    }

    /**
     * Do not implement
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    /**
     * Do not implement
     */
    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    /**
     * Do not implement
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    /**
     * Do not implement
     */
    @Override
    public E set(int index, E element) {
        return null;
    }
}
