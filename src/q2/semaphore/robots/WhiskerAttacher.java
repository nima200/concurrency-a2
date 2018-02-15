package q2.semaphore.robots;

import q2.parts.Head;
import q2.parts.Whisker;
import util.Util;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class WhiskerAttacher implements Runnable {

    private final LinkedList<Head> aHeads_incomplete; /* Heads with eyes */
    private final LinkedList<Head> aHeads_complete; /* Heads with eyes and whiskers */
    private final Semaphore head_inc_binKey;
    private final Semaphore head_com_binKey;
    private final Semaphore head_inc_produced;
    private final Semaphore head_com_produced;
    private long idleTime = 0;

    public WhiskerAttacher(LinkedList<Head> pHeads_incomplete,
                           LinkedList<Head> pHeads_complete,
                           Semaphore pHead_inc_binKey,
                           Semaphore pHead_com_binKey,
                           Semaphore pHead_inc_produced,
                           Semaphore pHead_com_produced) {
        aHeads_incomplete = pHeads_incomplete;
        aHeads_complete = pHeads_complete;
        head_inc_binKey = pHead_inc_binKey;
        head_com_binKey = pHead_com_binKey;
        head_inc_produced = pHead_inc_produced;
        head_com_produced = pHead_com_produced;
    }

    @Override
    public void run() {
        long start;
        long stop;
        while(true) {
            Head head;
            try {
                /* Wait for a head to get produced */
                start = System.currentTimeMillis();
//                System.out.println("Acquiring head completed produced");
                head_inc_produced.acquire();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Wait for access to the incomplete bin */
                start = System.currentTimeMillis();
//                System.out.println("Acquiring head completed bin key");
                head_inc_binKey.acquire();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                head = aHeads_incomplete.pop();
                /* Release access to bin */
                head_inc_binKey.release();
                /* Create and attach the whiskers */
                Whisker[] whiskers = {new Whisker(),
                        new Whisker(), new Whisker(), new Whisker(), new Whisker(), new Whisker()};
                head.attachWhiskers(whiskers);
                /* Wait for access to completed bin */
                start = System.currentTimeMillis();
//                System.out.println("Acquiring head completed produced");
                head_com_binKey.acquire();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                aHeads_complete.push(head);
                /* Let go of the bin */
                head_com_binKey.release();
                /* Inform about completion of the head */
                head_com_produced.release();
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(20, 60));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
