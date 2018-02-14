package q2.monitor.robots;

import q2.parts.Body;
import q2.parts.Leg;
import util.Util;

import java.util.LinkedList;

public class LegAttacher implements Runnable {

    private final LinkedList<Body> aBodies_incomplete; /* Bodies with tail */
    private final LinkedList<Body> aBodies_complete; /* Bodies with tail and legs */
    private final LinkedList<Leg> aFrontLegs;
    private final LinkedList<Leg> aHindLegs;
    private long idleTime = 0;

    public LegAttacher(LinkedList<Body> pBodies_incomplete, LinkedList<Body> pBodies_complete, LinkedList<Leg> pFrontLegs, LinkedList<Leg> pHindLegs) {
        aBodies_incomplete = pBodies_incomplete;
        aBodies_complete = pBodies_complete;
        aFrontLegs = pFrontLegs;
        aHindLegs = pHindLegs;
    }

    @Override
    public void run() {
        while(true) {
            Body body;
            Leg[] frontLegs = new Leg[2];
            Leg[] hindlegs = new Leg[2];
            long start = System.currentTimeMillis();
            synchronized (aBodies_incomplete) {
                long stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Wait for a body to get a tail */
                while(aBodies_incomplete.isEmpty()) {
                    try {
                        start = System.currentTimeMillis();
                        aBodies_incomplete.wait();
                        stop = System.currentTimeMillis();
                        idleTime += stop - start;
                    } catch (InterruptedException ignored) {
                        System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                        return;
                    }
                }
                /* Take the body and release the bin */
                body = aBodies_incomplete.pop();
            }
            start = System.currentTimeMillis();
            synchronized (aFrontLegs) {
                long stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Wait for a front leg */
                while(aFrontLegs.isEmpty()) {
                    try {
                        start = System.currentTimeMillis();
                        aFrontLegs.wait();
                        stop = System.currentTimeMillis();
                        idleTime += stop - start;
                    } catch (InterruptedException ignored) {
                        System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                        return;
                    }
                }
                frontLegs[0] = aFrontLegs.pop();
                /* Wait for another front leg */
                /* NOTE: Did not combine with the above to not query bin size */
                while(aFrontLegs.isEmpty()) {
                    try {
                        start = System.currentTimeMillis();
                        aFrontLegs.wait();
                        stop = System.currentTimeMillis();
                        idleTime += stop - start;
                    } catch (InterruptedException ignored) {
                        System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                        return;
                    }
                }
                frontLegs[1] = aFrontLegs.pop();
            }
            start = System.currentTimeMillis();
            synchronized (aHindLegs) {
                long stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Wait for a hind leg */
                while(aHindLegs.isEmpty()) {
                    try {
                        start = System.currentTimeMillis();
                        aHindLegs.wait();
                        stop = System.currentTimeMillis();
                        idleTime += stop - start;
                    } catch (InterruptedException ignored) {
                        System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                        return;
                    }
                }
                hindlegs[0] = aHindLegs.pop();
                /* Wait for another hind leg */
                /* NOTE: Did not combine with the above to not query bin size */
                while(aHindLegs.isEmpty()) {
                    try {
                        start = System.currentTimeMillis();
                        aHindLegs.wait();
                        stop = System.currentTimeMillis();
                        idleTime += stop - start;
                    } catch (InterruptedException ignored) {
                        System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                        return;
                    }
                }
                hindlegs[1] = aHindLegs.pop();
            }
            /* Attach legs to body */
            body.attachForeLegs(frontLegs);
            body.attachHindLegs(hindlegs);
            /* Place completed body in bin and wake some thread waiting */
            start = System.currentTimeMillis();
            synchronized (aBodies_complete) {
                long stop = System.currentTimeMillis();
                idleTime += stop - start;
                aBodies_complete.push(body);
                aBodies_complete.notify();
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
