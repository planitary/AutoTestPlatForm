package com.planitary.atplatform.base.utils;

/**
 * 临时双向链表(无头结点）
 */

public class TempDoubleLinkedList<T> {
    int currentIndex = 0;
    Node<T> head;
    Node<T> tail;
    int size;

    // 尾插法新建链表
    public void add(T data,int seq){
        Node<T> newNode = new Node<>(data,seq);
        // 数据为空
        if (head == null){
            head = newNode;
            tail = newNode;
        }else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size ++;
    }

}
