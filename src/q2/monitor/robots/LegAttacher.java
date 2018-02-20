package q2.monitor.robots;

import q2.monitor.Bins;
import q2.Robot;
import q2.parts.Body;
import q2.parts.Leg;
import util.Util;

public class LegAttacher extends Robot {

    private final Bins aBins;

    public LegAttacher(Bins pBins, String pName) {
        setName(pName);
        aBins = pBins;
    }

    @Override
    public void run() {
        long start;
        long stop;
        while (true) {
            Body body = null;
            boolean bodyHadTail = false;
            /* Check if there exists a body with a tail attached */
            start = System.currentTimeMillis();
            synchronized (aBins.getBodyWithTail()) {
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                if (!aBins.getBodyWithTail().isEmpty()) {
                    body = aBins.getBodyWithTail().pop();
                    bodyHadTail = true;
                }
            }
            /* Check if we got a body from the previous step
             * If we didn't, take a blank body */
            if (!bodyHadTail) {
                start = System.currentTimeMillis();
                synchronized (aBins.getBodies()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    body = aBins.getBodies().pop();
                }
            }
            /* Make space for taking 2 fore legs */
            Leg[] foreLegs = new Leg[2];
            /* Take 2 fore legs and attach them to the body */
            start = System.currentTimeMillis();
            synchronized (aBins.getForeLegs()) {
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Wait for 2 fore legs to get produced, if none available */
                while (aBins.getForeLegs().isEmpty()) {
                    try {
                        start = System.currentTimeMillis();
                        aBins.getForeLegs().wait();
                        stop = System.currentTimeMillis();
                        idleTime += stop - start;
                    } catch (InterruptedException ignored) {
                        return;
                    }
                }
                foreLegs[0] = aBins.getForeLegs().pop();
                while (aBins.getForeLegs().isEmpty()) {
                    try {
                        start = System.currentTimeMillis();
                        aBins.getForeLegs().wait();
                        stop = System.currentTimeMillis();
                        idleTime += stop - start;
                    } catch (InterruptedException ignored) {
                        return;
                    }
                }
                foreLegs[1] = aBins.getForeLegs().pop();
            }
            /* Make space for two hind legs */
            Leg[] hindLegs = new Leg[2];
            /* Take 2 hind legs and attach them to body */
            synchronized (aBins.getHindLegs()) {
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                /* Wait for 2 hind legs to get produced, if none available */
                while (aBins.getHindLegs().isEmpty()) {
                    try {
                        start = System.currentTimeMillis();
                        aBins.getHindLegs().wait();
                        stop = System.currentTimeMillis();
                        idleTime += stop - start;
                    } catch (InterruptedException ignored) {
                        return;
                    }
                }
                hindLegs[0] = aBins.getHindLegs().pop();
                while (aBins.getHindLegs().isEmpty()) {
                    try {
                        start = System.currentTimeMillis();
                        aBins.getHindLegs().wait();
                        stop = System.currentTimeMillis();
                        idleTime += stop - start;
                    } catch (InterruptedException ignored) {
                        return;
                    }
                }
                hindLegs[1] = aBins.getHindLegs().pop();
            }
            /* Attach the legs to the body */
            body.attachForeLegs(foreLegs);
            body.attachHindLegs(hindLegs);
            /* If body had a tail, place it in completed bodies bin
             * If it didn't have a tail, place it in bodies with legs bin */
            if (bodyHadTail) {
                start = System.currentTimeMillis();
                synchronized (aBins.getBodyCompleted()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Put the body on the completed bodies bin */
                    aBins.getBodyCompleted().push(body);
                    /* Notify about completed body production */
                    aBins.getBodyCompleted().notify();
                }
            } else {
                start = System.currentTimeMillis();
                synchronized (aBins.getBodyWithLegs()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Put the body on the bodies with tails bin */
                    aBins.getBodyWithTail().push(body);
                    /* Do not need to notify about production on this bin */
                }
            }

            try {
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(30, 50));
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }
}
