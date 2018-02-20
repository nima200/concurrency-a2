package q2.semaphore.robots;

import q2.Robot;
import q2.parts.Leg;
import q2.parts.Toe;
import q2.semaphore.Bins;
import util.Util;

import java.util.Random;

public class LegMaker extends Robot {

    private final Bins aBins;

    public LegMaker(Bins pBins, String pName) {
        setName(pName);
        aBins = pBins;
    }

    @Override
    public void run() {
        long start;
        long stop;
        Random random = new Random();
        while (true) {
            try {
                /* Randomly decide between making a foreleg and a hind leg*/
                if (random.nextFloat() >= 0.5) { /* Make a foreleg */
                    /* Make space for 5 toes */
                    Toe[] toes = new Toe[5];
                    start = System.currentTimeMillis();
                    /* Take 5 toes */
                    aBins.getToeBin().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    for (int i = 0; i < toes.length; i++) {
                        toes[i] = aBins.getToeBin().pop();
                    }
                    aBins.getToeBin().releaseAccess();

                    Leg leg;
                    /* Take a leg from the legs bin */
                    start = System.currentTimeMillis();
                    aBins.getLegBin().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    leg = aBins.getLegBin().pop();
                    aBins.getLegBin().releaseAccess();

                    /* Attach toes to leg */
                    leg.addToes(toes);

                    /* Put leg in fore legs bin and notify about production */
                    start = System.currentTimeMillis();
                    aBins.getForeLegs().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    aBins.getForeLegs().push(leg);
                    aBins.getForeLegs().releaseAccess();
                    aBins.getForeLegs().produced();
                } else { /* Make a hind leg*/
                    /* Make space for 4 toes */
                    Toe[] toes = new Toe[4];
                    /* Take 4 toes */
                    start = System.currentTimeMillis();
                    aBins.getToeBin().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    for (int i = 0; i < toes.length; i++) {
                        toes[i] = aBins.getToeBin().pop();
                    }
                    aBins.getToeBin().releaseAccess();

                    Leg leg;
                    /* Take a leg from the legs bin */
                    start = System.currentTimeMillis();
                    aBins.getLegBin().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    leg = aBins.getLegBin().pop();
                    aBins.getLegBin().releaseAccess();

                    /* Attach toes to leg */
                    leg.addToes(toes);

                    /* Put leg in hind legs bin and notify about production */
                    start = System.currentTimeMillis();
                    aBins.getHindLegs().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    aBins.getHindLegs().push(leg);
                    aBins.getHindLegs().releaseAccess();
                    aBins.getHindLegs().produced();
                }
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(10, 20));
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }
}
