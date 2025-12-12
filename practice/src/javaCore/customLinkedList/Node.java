package javaCore.customLinkedList;

class Node<T> {
    T data;
    Node<T> nextlink;

    public Node(T data){
        this.data = data;
        this.nextlink = null;

    }
}
