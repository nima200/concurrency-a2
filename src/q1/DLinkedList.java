package q1;

import java.util.Random;
import static java.lang.Math.sqrt;

public class DLinkedList {
    private Vertex aHead;

    public DLinkedList(Vertex pHead) {
        aHead = pHead;
    }

    public Vertex vertexAtIndex(int index) {
        Vertex current = aHead;

        while (index > 0) {
            current.lock();
            current = current.getNext();
            current.getPrevious().unlock();
            index--;
        }
        return current;
    }

    public Vertex getHead() {
        return aHead;
    }

    public static double[] createNewPoint(double a_x, double a_y, double b_x, double b_y, double c_x, double c_y) {
        double[] result = {0, 0};
        Random rand = new Random();
        double r1 = rand.nextDouble();
        double r2 = rand.nextDouble();
        result[0] = (1 - sqrt(r1)) * a_x + (sqrt(r1) * (1 - r2)) * b_x + (sqrt(r1) * r2) * c_x;
        result[1] = (1 - sqrt(r1)) * a_y + (sqrt(r1) * (1 - r2)) * b_y + (sqrt(r1) * r2) * c_y;
        return result;
    }

}
