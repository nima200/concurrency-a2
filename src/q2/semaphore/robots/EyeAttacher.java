package q2.semaphore.robots;

import q2.parts.Eye;
import q2.parts.Head;
import q2.semaphore.Bins;
import util.Util;

public class EyeAttacher implements Runnable {

    private final Bins aBins;
    private long idleTime = 0;

    public EyeAttacher(Bins pBins) {
        aBins = pBins;
    }

    @Override
    public void run() {
        long start;
        long stop;
        while (true) {
            try {
                Head head = null;
                boolean headHadWhiskers = false;
                /* Check if there exists a head with whiskers already */
                start = System.currentTimeMillis();
                aBins.getHeadWithWhiskers().getAccess();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                if (!aBins.getHeadWithWhiskers().isEmpty()) {
                    head = aBins.getHeadWithWhiskers().pop();
                    headHadWhiskers = true;
                }
                aBins.getHeadWithWhiskers().releaseAccess();

                /* If we didn't get a head with whiskers, take a blank head */
                if (!headHadWhiskers) {
                    start = System.currentTimeMillis();
                    aBins.getHeadBin().getAccess();
                    stop = System.currentTimeMillis();
                    idleTime += stop - start;
                    head = aBins.getHeadBin().pop();
                    aBins.getHeadBin().releaseAccess();
                }

                /* Make space for storing 2 eyes */
                Eye[] eyes = new Eye[2];
                /* Take two eyes */
                start = System.currentTimeMillis();
                aBins.getEyeBin().getAccess();
                stop = System.currentTimeMillis();
                idleTime += stop - start;
                for (int i = 0; i < eyes.length; i++) {
                    eyes[i] = aBins.getEyeBin().pop();
                }
                aBins.getEyeBin().releaseAccess();

                /* Attach eyes to head */
                head.attachEyes(eyes);

                /* If head had whiskers, place it in completed heads bin
                 * If not, place it in the heads with eyes bin */
                if (headHadWhiskers) {
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
                Thread.sleep(Util.randInt(10, 30));
            } catch (InterruptedException ignored) {
                System.out.println(Thread.currentThread().getName() + " idle time: " + idleTime);
                return;
            }
        }
    }
}
