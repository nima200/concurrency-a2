package q2.monitor.robots;

import q2.monitor.Bins;
import q2.parts.Body;
import q2.parts.Tail;
import util.Util;

public class TailAttacher implements Runnable {

    private final Bins aBins;
    private long idleTime = 0;

    public TailAttacher(Bins pBins) {
        aBins = pBins;
    }

    @Override
    public void run() {
        long start;
        long stop;
        while (true) {
            Body body = null;
            boolean bodyHadLegs = false;
            /* Check if there exists a body with legs, if so take it */
            start = System.currentTimeMillis();
            synchronized (aBins.getBodyWithLegs()) {
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                if (!aBins.getBodyWithLegs().isEmpty()) {
                    body = aBins.getBodyWithLegs().pop();
                    bodyHadLegs = true;
                }
            }
            /* If the body didn't have legs, take a blank body */
            if (!bodyHadLegs) {
                start = System.currentTimeMillis();
                synchronized (aBins.getBodies()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    body = aBins.getBodies().pop();
                }
            }
            /* Make space for reference to tail */
            Tail tail;
            /* Take a tail from the tails bin */
            start = System.currentTimeMillis();
            synchronized (aBins.getTails()) {
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                tail = aBins.getTails().pop();
            }
            /* Attach the tail to the body */
            body.attachTail(tail);
            /* If the body had legs, add it to the list of completed bodies
             * If not, then add it to the list of bodies with tails */
            if (bodyHadLegs) {
                start = System.currentTimeMillis();
                synchronized (aBins.getBodyCompleted()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Add body to bin of completed bodies */
                    aBins.getBodyCompleted().push(body);
                    /* Inform about production */
                    aBins.getBodyCompleted().notify();
                }
            } else {
                start = System.currentTimeMillis();
                synchronized (aBins.getBodyWithTail()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Add body to bin of bodies with tails */
                    aBins.getBodyWithTail().push(body);
                    /* Do not need to inform about production */
                }
            }

            try {
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(10, 20));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
