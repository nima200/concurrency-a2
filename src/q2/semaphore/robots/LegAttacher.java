package q2.semaphore.robots;

import q2.parts.Body;
import q2.parts.Leg;
import util.Util;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class LegAttacher implements Runnable {
    private final LinkedList<Body> aBodies_incomplete; /* Bodies with tail */
    private final LinkedList<Body> aBodies_complete; /* Bodies with tail and legs */
    private final LinkedList<Leg> aFrontLegs;
    private final LinkedList<Leg> aHindLegs;
    private final Semaphore aBodies_inc_binKey; /* Access key for bin */
    private final Semaphore aBodies_inc_produced; /* Access key for bin */
    private final Semaphore aBodies_com_binKey; /* Access key for bin */
    private final Semaphore aBodies_com_produced; /* Access key for bin */
    private final Semaphore aFrontLegs_produced; /* To notify about production of items */
    private final Semaphore aFrontLegs_binKey; /* Access key for bin */
    private final Semaphore aHindLegs_produced; /* To notify about production of items */
    private final Semaphore aHindLegs_binKey; /* Access key for bin */

    private long idleTime = 0;

    public LegAttacher(LinkedList<Body> pBodies_incomplete,
                       LinkedList<Body> pBodies_complete,
                       LinkedList<Leg> pFrontLegs,
                       LinkedList<Leg> pHindLegs,
                       Semaphore pBodies_inc_binKey,
                       Semaphore pBodies_com_binKey,
                       Semaphore pFrontLegs_binKey,
                       Semaphore pHindLegs_binKey,
                       Semaphore pFrontLegs_produced,
                       Semaphore pHindLegs_produced,
                       Semaphore pBodies_inc_produced,
                       Semaphore pBodies_com_produced) {
        aBodies_incomplete = pBodies_incomplete;
        aBodies_complete = pBodies_complete;
        aFrontLegs = pFrontLegs;
        aHindLegs = pHindLegs;
        aBodies_inc_binKey = pBodies_inc_binKey;
        aBodies_com_binKey = pBodies_com_binKey;
        aFrontLegs_produced = pFrontLegs_produced;
        aHindLegs_produced = pHindLegs_produced;
        aFrontLegs_binKey = pFrontLegs_binKey;
        aHindLegs_binKey = pHindLegs_binKey;
        aBodies_inc_produced = pBodies_inc_produced;
        aBodies_com_produced = pBodies_com_produced;
    }


    @Override
    public void run() {
        while (true) {
            Body body;
            Leg[] frontLegs = new Leg[2];
            Leg[] hindLegs = new Leg[2];

            long start;
            long stop;
            /* Wait for a body to get created */
            try {
                start = System.currentTimeMillis();
                aBodies_inc_produced.acquire();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Wait for access to bin */
                start = System.currentTimeMillis();
                aBodies_inc_binKey.acquire();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Take the body */
                body = aBodies_incomplete.pop();
                /* Release access to bin */
                aBodies_inc_binKey.release();
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
            /* Wait for two front legs to get created */
            try {
                start = System.currentTimeMillis();
                aFrontLegs_produced.acquire(2);
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Wait for access to bin */
                start = System.currentTimeMillis();
                aBodies_inc_binKey.acquire();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Take the two front legs */
                frontLegs[0] = aFrontLegs.pop();
                frontLegs[1] = aFrontLegs.pop();
                /* Release access to the bin */
                aFrontLegs_binKey.release();
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
            /* Wait for two hindlegs to get created */
            try {
                start = System.currentTimeMillis();
                aHindLegs_produced.acquire(2);
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Wait for access to bin */
                start = System.currentTimeMillis();
                aFrontLegs_binKey.acquire();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Take the two hind legs */
                hindLegs[0] = aHindLegs.pop();
                hindLegs[1] = aHindLegs.pop();
                /* Release access to the bin */
                aHindLegs_binKey.release();
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
            /* Attach legs to body */
            body.attachForeLegs(frontLegs);
            body.attachHindLegs(hindLegs);
            /* Wait for access to bin */
            try {
                start = System.currentTimeMillis();
                aBodies_com_binKey.acquire();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                aBodies_complete.push(body);
                /* Inform body produced */
                aBodies_com_produced.release();
                /* Release access to bin */
                aBodies_com_binKey.release();
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
            try {
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(30, 50));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
