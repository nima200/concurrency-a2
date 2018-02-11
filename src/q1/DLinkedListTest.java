package q1;

import org.junit.Test;

import static org.junit.Assert.*;

public class DLinkedListTest {

    @Test
    public void vertexAtIndex() {
        Vertex v1 = new Vertex(1, 1);
        Vertex v2 = new Vertex(1, 1);
        Vertex v3 = new Vertex(1, 1);
        v1.setNext(v2);
        v2.setNext(v3);
        v3.setNext(v1);
        DLinkedList list = new DLinkedList(v1);
        assertEquals(v3, list.vertexAtIndex(2));
    }
}