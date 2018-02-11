package q1;

import java.util.Arrays;
import java.util.Random;

public class Mover implements Runnable {

    private DLinkedList aVertices;
    private int aID;
    int aCount;

    public Mover(DLinkedList pVertices, int pID, int pCount) {
        aVertices = pVertices;
        aID = pID;
        aCount = pCount;
    }

    @Override
    public void run() {

        for (int i = 0; i < aCount; i++) {
            int length = 6;
            Random rand = new Random();
            int index = rand.nextInt(length);
            Vertex middle = aVertices.vertexAtIndex(index);
            int previousIndex = (index + length - 1) % length;
            int nextIndex = (index + 1) % length;
            int[] indices = {previousIndex, index, nextIndex};
            Arrays.sort(indices);
            Vertex first = aVertices.vertexAtIndex(indices[0]);
            Vertex second = aVertices.vertexAtIndex(indices[1]);
            Vertex third = aVertices.vertexAtIndex(indices[2]);

            first.lock();
            second.lock();
            third.lock();

            double[] newCords =
                    DLinkedList.createNewPoint(first.getX(), first.getY(),
                            second.getX(), second.getY(),
                            third.getX(), third.getY());

            middle.move(newCords[0], newCords[1]);

            third.unlock();
            second.unlock();
            first.unlock();
        }
    }
}
