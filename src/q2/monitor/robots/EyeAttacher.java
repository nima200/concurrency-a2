package q2.monitor.robots;

import q2.monitor.Bins;
import q2.Robot;
import q2.parts.Eye;
import q2.parts.Head;
import util.Util;

public class EyeAttacher extends Robot {

    private final Bins aBins;

    public EyeAttacher(Bins pBins, String pName) {
        setName(pName);
        aBins = pBins;
    }

    @Override
    public void run() {
        long start;
        long stop;
        while (true) {
            /* Make reference to store the head */
            Head head = null;
            boolean headHadWhiskers = false;
            /* Check if there exists a head with whiskers
             * If so, take that head and attach the eyes on it, put it in the completed heads bin
             * If not, take a blank head and attach eyes on it, put it in the heads with eyes bin */
            start = System.currentTimeMillis();
            synchronized (aBins.getHeadWithWhiskers()) {
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                if (!aBins.getHeadWithWhiskers().isEmpty()) {
                    head = aBins.getHeadWithWhiskers().pop();
                    headHadWhiskers = true;
                }
            }

            if (!headHadWhiskers) {
                start = System.currentTimeMillis();
                synchronized (aBins.getHeads()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    head = aBins.getHeads().pop();
                }
            }

            /* Make space for 2 eyes */
            Eye[] eyes = new Eye[2];
            start = System.currentTimeMillis();
            synchronized (aBins.getEyes()) {
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                for (int i = 0; i < eyes.length; i++) {
                    eyes[i] = aBins.getEyes().pop();
                }
            }

            /* Attach the eyes to the head */
            head.attachEyes(eyes);

            /* If the head had whiskers, place it in the completed heads bin,
             * If not, place it in the heads with eyes bin */
            if (headHadWhiskers) {
                start = System.currentTimeMillis();
                synchronized (aBins.getHeadCompleted()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Put the head in the completed heads bin */
                    aBins.getHeadCompleted().push(head);
                    /* Notify about the production of completed head */
                    aBins.getHeadCompleted().notify();
                }
            } else {
                start = System.currentTimeMillis();
                synchronized (aBins.getHeadWithEyes()) {
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    /* Put the head in the heads with eyes bin */
                    aBins.getHeadWithEyes().push(head);
                    /* Do not need to notify about production of this part */
                }
            }

            try {
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(10, 30));
            } catch (InterruptedException ignored) {
                return;
            }
        }
    }
}
