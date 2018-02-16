package q2.semaphore.robots;

import q2.parts.Body;
import q2.parts.Leg;
import q2.semaphore.Bins;
import util.Util;

public class LegAttacher implements Runnable {

    private final Bins aBins;
    private long idleTime = 0;

    public LegAttacher(Bins pBins) {
        aBins = pBins;
    }

    @Override
    public void run() {
        long start;
        long stop;
        while (true) {
            try {
                Body body = null;
                boolean bodyHadTail = false;
                /* Check if there exists a body with a tail attached */
                start = System.currentTimeMillis();
                aBins.getBodyWithTail().getAccess();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                if (!aBins.getBodyWithTail().isEmpty()) {
                    body = aBins.getBodyWithTail().pop();
                    bodyHadTail = true;
                }
                aBins.getBodyWithTail().releaseAccess();

                /* Check if we got a body from the previous step,
                 * If we didn't, take a blank body */
                if (!bodyHadTail) {
                    start = System.currentTimeMillis();
                    aBins.getBodyBin().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    body = aBins.getBodyBin().pop();
                    aBins.getBodyBin().releaseAccess();
                }

                /* Make space for taking 2 fore legs */
                Leg[] foreLegs = new Leg[2];
                /* Wait for 2 fore legs to get created */
                start = System.currentTimeMillis();
                aBins.getForeLegs().consume(2);
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Take 2 fore legs */
                start = System.currentTimeMillis();
                aBins.getForeLegs().getAccess();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                foreLegs[0] = aBins.getForeLegs().pop();
                foreLegs[1] = aBins.getForeLegs().pop();
                aBins.getForeLegs().releaseAccess();

                /* Make space for taking 2 hind legs */
                Leg[] hindLegs = new Leg[2];
                /* Wait for 2 hind legs to get produced */
                start = System.currentTimeMillis();
                aBins.getHindLegs().consume(2);
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Take 2 hind legs */
                start = System.currentTimeMillis();
                aBins.getHindLegs().getAccess();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                hindLegs[0] = aBins.getHindLegs().pop();
                hindLegs[1] = aBins.getHindLegs().pop();
                aBins.getHindLegs().releaseAccess();

                /* Attach the legs to the body */
                body.attachForeLegs(foreLegs);
                body.attachHindLegs(hindLegs);

                /* If body had a tail, place it in the completed bodies bin
                 * If not, place it it in the bodies with legs bin */
                if (bodyHadTail) {
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
                    aBins.getBodyWithLegs().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    aBins.getBodyWithLegs().push(body);
                    aBins.getBodyWithLegs().releaseAccess();
                    /* Do not need to inform about production */
                }
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(30, 50));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
