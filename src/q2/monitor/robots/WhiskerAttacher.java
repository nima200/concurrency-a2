package q2.monitor.robots;

import q2.monitor.Bins;
import q2.monitor.Robot;
import q2.parts.Head;
import q2.parts.Whisker;
import util.Util;

public class WhiskerAttacher extends Robot {

    private final Bins aBins;

    public WhiskerAttacher(Bins pBins, String pName) {
        setName(pName);
        aBins = pBins;
    }

    @Override
    public void run() {
        long start;
        long stop;
        while (true) {
            Head head = null;
            boolean headHadEyes = false;
            /* Check if there exists a head with eyes on it
             * If so, take that head, attach whiskers, and put it in the completed heads bin
             * If not, attach whiskers on a blank head and put it in the heads with whiskers bin */
            start = System.currentTimeMillis();
            synchronized (aBins.getHeadWithEyes()) {
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                if (!aBins.getHeadWithEyes().isEmpty()) {
                    head = aBins.getHeadWithEyes().pop();
                    headHadEyes = true;
                }
            }

            if (!headHadEyes) {
                start = System.currentTimeMillis();
                synchronized (aBins.getHeads()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    head = aBins.getHeads().pop();
                }
            }

            /* Make space for 6 whiskers */
            Whisker[] whiskers = new Whisker[6];
            /* Take 6 whiskers from the whisker bin */
            start = System.currentTimeMillis();
            synchronized (aBins.getWhiskers()) {
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                for (int i = 0; i < whiskers.length; i++) {
                    whiskers[i] = aBins.getWhiskers().pop();
                }
            }

            /* Attach the whiskers to the head */
            head.attachWhiskers(whiskers);

            /* If the head had a eyes on it already, put it in the completed heads bin
             * If not, put it in the heads with whiskers bin */
            if (headHadEyes) {
                start = System.currentTimeMillis();
                synchronized (aBins.getHeadCompleted()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Put the head in the completed heads bin */
                    aBins.getHeadCompleted().push(head);
                    /* Notify about production of completed head */
                    aBins.getHeadCompleted().notify();
                }
            } else {
                start = System.currentTimeMillis();
                synchronized (aBins.getHeadWithWhiskers()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Put the head in the heads with whiskers bin */
                    aBins.getHeadWithWhiskers().push(head);
                    /* Do not need to notify about production */
                }
            }

            try {
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(20, 60));
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }
}
