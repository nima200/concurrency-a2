package q2.monitor.robots;

import q2.parts.Eye;
import q2.parts.Head;
import util.Util;

import java.util.LinkedList;

public class EyeAttacher implements Runnable {

    private final LinkedList<Head> aHeads_incomplete; /* Bin to put heads with eyes */
    private long idleTime = 0;

    public EyeAttacher(LinkedList<Head> pHeads_incomplete) {
        aHeads_incomplete = pHeads_incomplete;
    }

    @Override
    public void run() {
        while(true) {
            Head head = new Head();
            Eye[] eyes = {new Eye(), new Eye()};
            head.attachEyes(eyes);
            /* Only one robot may access a bin at a time */
            long start = System.currentTimeMillis();
            synchronized (aHeads_incomplete) {
                long stop = System.currentTimeMillis();
                /* Put head at the end of list */
                aHeads_incomplete.push(head);
                idleTime += stop - start;
                /* Notify threads waiting on the list, if any */
                aHeads_incomplete.notify();
            }
            try {
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(10, 30));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
