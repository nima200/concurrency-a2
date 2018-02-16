package q2.semaphore;

import java.util.concurrent.Semaphore;

public class ProductBin<T> extends BasicBin<T> {

    private Semaphore aProduced;

    ProductBin(int key) {
        super(key);
        aProduced = new Semaphore(0);
    }

    public void consume(int pCount) throws InterruptedException {
        aProduced.acquire(pCount);
    }

    public void consume() throws InterruptedException {
        aProduced.acquire();
    }

    public void produced(int pCount) {
        aProduced.release(pCount);
    }

    public void produced() {
        aProduced.release();
    }
}
