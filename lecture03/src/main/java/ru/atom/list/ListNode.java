package ru.atom.list;

import java.util.List;

/**
 * Contains ref to next node, prev node and value
 */
public class ListNode<E> {
    private E value;
    private ListNode nextNode;
    private ListNode prevNode;
//    private int index;

    public ListNode(E val, ListNode next, ListNode prev) {
        this.value = val;
        this.nextNode = next;
        this.prevNode = prev;
//        this.index = index;
    }

    public E getValue() {
        return value;
    }

    public void setNextNode(ListNode nextNode) {
        this.nextNode = nextNode;
    }

    public void setPrevNode(ListNode prevNode) {
        this.prevNode = prevNode;
    }

    public ListNode getNextNode() {
        return nextNode;
    }

    public ListNode getPrevNode() {
        return prevNode;
    }

//    public int getIndex() {
//        return index;
//    }
}
