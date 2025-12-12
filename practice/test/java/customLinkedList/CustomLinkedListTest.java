package customLinkedList;

import javaCore.customLinkedList.CustomLinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CustomLinkedListTest {

    private CustomLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new CustomLinkedList<>();
    }

    @Test
    void testAddFirst() {
        list.addFirst(100);
        assertEquals(1, list.size());
        assertEquals(100, list.getFirst());
        assertEquals(100, list.getLast());

        list.addFirst(200);
        assertEquals(2, list.size());
        assertEquals(200, list.getFirst());
        assertEquals(100, list.getLast());
    }

    @Test
    void testAddLast() {
        list.addLast(10);
        assertEquals(1, list.size());
        assertEquals(10, list.getLast());
        assertEquals(10, list.getFirst());

        list.addLast(20);
        assertEquals(20, list.getLast());
        assertEquals(2, list.size());
        assertEquals(10, list.getFirst());
    }

    @Test
    void testGetFirstEmpty() {
        assertThrows(NullPointerException.class, () -> {
            list.getFirst();
        });
    }

    @Test
    void testGetLastEmpty() {
        assertThrows(NullPointerException.class, () -> {
            list.getLast();
        });
    }

    @Test
    void testGetFirstAndLast() {
        list.addFirst(50);
        list.addLast(60);
        assertEquals(50, list.getFirst());
        assertEquals(60, list.getLast());
    }

    @Test
    void testSize() {
        assertEquals(0, list.size());

        list.addFirst(70);
        assertEquals(1, list.size());

        list.addLast(80);
        assertEquals(2, list.size());

        list.removeFirst();
        assertEquals(1, list.size());

        list.removeLast();
        assertEquals(0, list.size());
    }

    @Test
    void testRemoveFirstFromEmptyList() {
        list.removeFirst();
        assertEquals(0, list.size());
    }

    @Test
    void testRemoveFirst() {
        list.add(0, 1);
        list.add(1, 2);
        list.removeFirst();
        assertEquals(1, list.size());
        assertEquals(2, list.get(0));
    }

    @Test
    void testRemoveLastFromEmptyList() {
        list.removeLast();
        assertEquals(0, list.size());
    }

    @Test
    void testRemoveLast() {
        list.add(0, 1);
        list.add(1, 2);
        list.removeLast();
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    void testRemoveAtIndex() {
        list.add(0, 1);
        list.add(1, 2);
        list.add(2, 3);
        list.remove(1);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }

    @Test
    void testRemoveAtIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.remove(-1);
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.remove(0);
        });

        list.add(0, 1);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.remove(1);
        });
    }

    @Test
    void testAddAtIndex() {
        list.add(0, 10);
        list.add(1, 20);
        list.add(1, 30);
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
        assertEquals(20, list.get(2));
    }

    @Test
    void testAddAtIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add(-1, 1);
        });

        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add(1, 2);
        });

        list.add(0, 1);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            list.add(2, 3);
        });
    }
}




