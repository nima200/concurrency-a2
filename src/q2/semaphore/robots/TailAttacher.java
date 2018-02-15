package q2.semaphore.robots;

import q2.parts.Body;
import q2.parts.Tail;
import util.Util;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class TailAttacher implements Runnable {

    private final LinkedList<Body> aBodies_incomplete;
    private final Semaphore aBodies_inc_binKey;
    private final Semaphore aBodies_inc_produced;
    private long idleTime = 0;

    public TailAttacher(LinkedList<Body> pBodies_incomplete,
                        Semaphore pBodies_inc_binKey,
                        Semaphore pBodies_inc_produced) {
        aBodies_incomplete = pBodies_incomplete;
        aBodies_inc_binKey = pBodies_inc_binKey;
        aBodies_inc_produced = pBodies_inc_produced;
    }

    @Override
    public void run() {
        long start;
        long stop;
        while(true) {
            /* Create a new body */
            Body body = new Body();
            /* Attach the tail to it */
            body.attachTail(new Tail());
            try {
                start = System.currentTimeMillis();
                /* Wait for access to the bin */
                aBodies_inc_binKey.acquire();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                aBodies_incomplete.push(body);
                /* Release access to bin */
                aBodies_inc_binKey.release();
                /* Inform about production */
                aBodies_inc_produced.release();
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(10, 20));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }

        }
    }
}
