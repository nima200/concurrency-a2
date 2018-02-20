package q2;

public class Robot extends Thread {
    private long startTime = 0;
    private long stopTime = 0;
    protected long idleTime = 0;

    public void start() {
        startTime = System.currentTimeMillis();
        super.start();
    }

    public void interrupt() {
        stopTime = System.currentTimeMillis();
        super.interrupt();
    }

    public float getIdlePercentage() {
        long total = stopTime - startTime;
        return ((float) idleTime / (float) total) * 100;
    }
}
