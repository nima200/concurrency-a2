package q2.semaphore.robots;

import q2.parts.Head;
import q2.parts.Whisker;
import q2.semaphore.Bins;
import util.Util;

public class WhiskerAttacher implements Runnable {

    private final Bins aBins;
    private long idleTime = 0;

    public WhiskerAttacher(Bins pBins) {
        aBins = pBins;
    }

    @Override
    public void run() {
        long start;
        long stop;
        while (true) {
            try {
                Head head = null;
                boolean headHadEyes = false;
                /* Check to see if we had a head with eyes, if so, take it */
                start = System.currentTimeMillis();
                aBins.getHeadWithEyes().getAccess();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                if (!aBins.getHeadWithEyes().isEmpty()) {
                    head = aBins.getHeadWithEyes().pop();
                    headHadEyes = true;
                }
                aBins.getHeadWithEyes().releaseAccess();

                /* If there was no head with eyes, take a blank one */
                if (!headHadEyes) {
                    start = System.currentTimeMillis();
                    aBins.getHeadBin().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    head = aBins.getHeadBin().pop();
                    aBins.getHeadBin().releaseAccess();
                }

                /* Make space to take 6 whiskers */
                Whisker[] whiskers = new Whisker[6];
                aBins.getWhiskerBin().getAccess();
                for (int i = 0; i < whiskers.length; i++) {
                    whiskers[i] = aBins.getWhiskerBin().pop();
                }
                aBins.getWhiskerBin().releaseAccess();

                /* Attach whiskers to head */
                head.attachWhiskers(whiskers);

                /* If head had eyes, put it in the completed heads bin
                 * If not, place it in the heads with whiskers bin */
                if (headHadEyes) {
                    start = System.currentTimeMillis();
                    aBins.getHeadCompleted().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    aBins.getHeadCompleted().push(head);
                    aBins.getHeadCompleted().releaseAccess();
                    /* Notify about production */
                    aBins.getHeadCompleted().produced();
                } else {
                    start = System.currentTimeMillis();
                    aBins.getHeadWithEyes().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    aBins.getHeadWithEyes().push(head);
                    aBins.getHeadWithEyes().releaseAccess();
                    /* Do not need to inform about production */
                }
                /* Simulate assembly time */
                Thread.sleep(Util.randInt(20, 60));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
