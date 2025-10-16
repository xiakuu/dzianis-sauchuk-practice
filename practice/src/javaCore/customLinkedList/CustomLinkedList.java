package javaCore.customLinkedList;

public class CustomLinkedList<T> {
    private Node<T> first;
    private Node<T> last;
    private int size;

    public CustomLinkedList(){
        first = null;
        last = null;
        size = 0;
    }

    public int size(){
        return size;
    }

    public void addFirst(T data){
        Node<T> node = new Node<>(data);
        node.nextlink = first;
        first = node;

        if (size == 0){
            last = node;
        }
        size++;
    }

    public void addLast(T data){
        if(size == 0){
            first = last = new Node<>(data);
        } else{
            last.nextlink = new Node<>(data);
            last = last.nextlink;
        }
        size++;
    }

    public T getFirst(){
        return first.data;
    }

    public T getLast(){
        return last.data;
    }

    public T get(int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException("out of bounds");
        }

        Node<T> node = first;
        for (int i = 0; i < index; i++) {
            node = node.nextlink;
        }
        return node.data;
    }

    public void removeFirst(){
        if(first == null){
            return;
        }
        first = first.nextlink;
        if (first == null){
            last = null;
        }
        size--;
    }

    public void removeLast(){
        if (first == null){
            return;
        }
        Node<T> node = first;
        for (int i = 0; i < size - 1; i++) {
            node = node.nextlink;
        }
        node.nextlink = null;
        last = node;
        size--;
    }

    public void remove(int index){
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException("out of bounds");
        }

        if(index == 0){
            removeFirst();
            return;
        }

        Node<T> node = first;
        for (int i = 0; i < index - 1; i++) {
            node = node.nextlink;
        }

        if(node.nextlink == last){
            last = node;
        }

        node.nextlink = node.nextlink.nextlink;
        size--;
    }



    public void add(int index, T data){
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException("out of bounds");
        }

        if(index == 0){
            addFirst(data);
            return;
        }

        Node<T> datanode = new Node<>(data);

        Node<T> node = first;
        for (int i = 0; i < index - 1; i++) {
            node = node.nextlink;
        }
        datanode.nextlink = node.nextlink;
        node.nextlink = datanode;
        size++;

    }







}
