package q2.semaphore.robots;

import q2.parts.Eye;
import q2.parts.Head;
import util.Util;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class EyeAttacher implements Runnable {

    private final LinkedList<Head> aHeads_incomplete; /* Bin to put heads with eyes */
    private final Semaphore aHeads_inc_binKey; /* Semaphore key to the bin */
    private final Semaphore aHeads_inc_produced; /* To notify about production of items */
    private long idleTime = 0;

    public EyeAttacher(LinkedList<Head> pHeads_incomplete, Semaphore pHeads_inc_binKey, Semaphore pHeads_inc_produced) {
        aHeads_incomplete = pHeads_incomplete;
        aHeads_inc_binKey = pHeads_inc_binKey;
        aHeads_inc_produced = pHeads_inc_produced;
    }

    @Override
    public void run() {
        while(true) {
            Head head = new Head();
            Eye[] eyes = {new Eye(), new Eye()};
            head.attachEyes(eyes);
            /* Only one robot may access a bin at a time */
            try {
                long start = System.currentTimeMillis();
                aHeads_inc_binKey.acquire();
                long stop = System.currentTimeMillis();
                idleTime += (stop - start);
                /* Put created head in bin */
                aHeads_incomplete.push(head);
                /* Let go of the bin */
                aHeads_inc_binKey.release();
                /* Notify about production */
                aHeads_inc_produced.release();
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
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
