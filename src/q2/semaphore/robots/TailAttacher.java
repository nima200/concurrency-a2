package q2.semaphore.robots;

import q2.parts.Body;
import q2.parts.Tail;
import q2.semaphore.Bins;
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
            try {
                Body body = null;
                boolean bodyHadLegs = false;
                /* Check if there's a body with legs */
                start = System.currentTimeMillis();
                aBins.getBodyWithLegs().getAccess();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                if (!aBins.getBodyWithLegs().isEmpty()) {
                    body = aBins.getBodyWithLegs().pop();
                    bodyHadLegs = true;
                }
                aBins.getBodyWithLegs().releaseAccess();

                /* If there wasn't a body with legs, take a blank body */
                if (!bodyHadLegs) {
                    start = System.currentTimeMillis();
                    aBins.getBodyBin().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    body = aBins.getBodyBin().pop();
                    aBins.getBodyBin().releaseAccess();
                }

                /* Make reference to tail that we are taking */
                Tail tail;
                start = System.currentTimeMillis();
                aBins.getTailBin().getAccess();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                tail = aBins.getTailBin().pop();
                aBins.getTailBin().releaseAccess();

                /* Attach tail to body */
                body.attachTail(tail);

                /* If it was a body with legs, put it in the completed body bin
                 * If not, put it in the body with tail bin */
                if (bodyHadLegs) {
                    start = System.currentTimeMillis();
                    aBins.getBodyCompleted().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    aBins.getBodyCompleted().push(body);
                    aBins.getBodyCompleted().releaseAccess();
                    /* Notify about production */
                    aBins.getBodyCompleted().produced();
                } else {
                    start = System.currentTimeMillis();
                    aBins.getBodyWithTail().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    aBins.getBodyWithTail().push(body);
                    aBins.getBodyWithTail().releaseAccess();
                    /* Do not need to notify production */
                    Thread.sleep(Util.randInt(10, 20));
                }
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
