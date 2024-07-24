package com.planitary.atplatform.base.utils;

/**
 * 链表节点定义
 * @param <T>
 */
public class Node<T> {
    // 数据
    T data;
    // 序号
    int index;
    // 前指针
    Node<T> prev;
    // 后指针
    Node<T> next;
    public Node(T data,int index){
        this.data = data;
        this.index = index;
    }
}
