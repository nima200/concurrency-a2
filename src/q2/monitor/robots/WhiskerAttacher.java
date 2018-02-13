package q2.monitor.robots;

import q2.monitor.parts.Head;
import q2.monitor.parts.Whisker;
import util.Util;

import java.util.LinkedList;

public class WhiskerAttacher implements Runnable {

    private final LinkedList<Head> aHeads_incomplete; /* Heads with eyes */
    private final LinkedList<Head> aHeads_complete; /* Heads with eyes and Whiskers */
    private long idleTime = 0;

    public WhiskerAttacher(LinkedList<Head> pHeads_incomplete, LinkedList<Head> pHeads_complete) {
        aHeads_incomplete = pHeads_incomplete;
        aHeads_complete = pHeads_complete;
    }

    @Override
    public void run() {
        while(true) {
            Head head;
            /* Only one robot can access a bin at a time */
            synchronized (aHeads_incomplete) {
                /* Wait until someone puts on the eyes for the head */
                while(aHeads_incomplete.isEmpty()) {
                    try {
                        long start = System.currentTimeMillis();
                        aHeads_incomplete.wait();
                        long stop = System.currentTimeMillis();
                        idleTime += stop - start;
                    } catch (InterruptedException ignored) {
                        System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                        return;
                    }
                }
                head = aHeads_incomplete.pop();
            }
            Whisker[] whiskers = {new Whisker(), new Whisker(), new Whisker(), new Whisker(), new Whisker(), new Whisker()};
            head.attachWhiskers(whiskers);
            /* Only one robot can access a bin at a time */
            synchronized (aHeads_complete) {
                /* Put head at the end of queue */
                aHeads_complete.push(head);
                /* If someone was waiting for a completed head, notify them that it's ready */
                aHeads_complete.notify();
            }
            /* Simulate the assembly process by sleeping for 20-60ms */
            try {
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(20, 60));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
