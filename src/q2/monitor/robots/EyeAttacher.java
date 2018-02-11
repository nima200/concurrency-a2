package q2.monitor.robots;

import q2.monitor.parts.Eye;
import q2.monitor.parts.Head;
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
            synchronized (aHeads_incomplete) {
                /* Put head at the end of list */
                aHeads_incomplete.push(head);
                /* Notify threads waiting on the list, if any */
                aHeads_incomplete.notify();
            }
            try {
                Thread.sleep(Util.randInt(10, 30));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
