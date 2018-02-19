package q2.monitor.robots;

import q2.monitor.Bins;
import q2.monitor.Robot;
import q2.parts.Leg;
import q2.parts.Toe;
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
        Random random = new Random();
        long start;
        long stop;
        while (true) {
            if (random.nextFloat() >= 0.5) { /* Make a fore leg */
                /* Make space for storing 5 toes */
                Toe[] toes = new Toe[5];
                start = System.currentTimeMillis();
                synchronized (aBins.getToes()) {
                    /* Track time asleep */
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Take 5 toes from toe bin */
                    for (int i = 0; i < toes.length; i++) {
                        toes[i] = aBins.getToes().pop();
                    }
                }

                Leg foreLeg;
                start = System.currentTimeMillis();
                synchronized (aBins.getLegs()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    foreLeg = aBins.getLegs().pop();
                }

                /* Attach toes to leg to complete foreleg */
                foreLeg.addToes(toes);

                start = System.currentTimeMillis();
                synchronized (aBins.getForeLegs()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Add foreleg to bin of forelegs */
                    aBins.getForeLegs().push(foreLeg);
                    /* Notify others about creation */
                    aBins.getForeLegs().notify();
                }
            } else { /* Make a hind leg */
                /* Make space for storing 4 toes */
                Toe[] toes = new Toe[4];
                start = System.currentTimeMillis();
                synchronized (aBins.getToes()) {
                    /* Track time asleep */
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Take 4 toes from toe bin */
                    for (int i = 0; i < toes.length; i++) {
                        toes[i] = aBins.getToes().pop();
                    }
                }

                Leg hindLeg;
                start = System.currentTimeMillis();
                synchronized (aBins.getLegs()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    hindLeg = aBins.getLegs().pop();
                }

                /* Attach toes to leg to complete hind leg */
                hindLeg.addToes(toes);

                start = System.currentTimeMillis();
                synchronized (aBins.getHindLegs()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Add foreleg to bin of hind legs */
                    aBins.getHindLegs().push(hindLeg);
                    /* Notify others about creation */
                    aBins.getHindLegs().notify();
                }
            }
            /* Simulate assembly time */
            try {
                Thread.sleep(Util.randInt(10, 20));
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }
}
