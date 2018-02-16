package q2.semaphore;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class BasicBin<T> {
    private final LinkedList<T> aList;
    private final Semaphore aKey;

    BasicBin(int key) {
        aList = new LinkedList<>();
        aKey = new Semaphore(key);
    }

    public LinkedList<T> getList() {
        return aList;
    }

    public void push(T item) {
        aList.push(item);
    }

    public T pop() {
        return aList.pop();
    }

    public boolean isEmpty() {
        return aList.isEmpty();
    }

    public void getAccess() throws InterruptedException {
        aKey.acquire();
    }

    public void releaseAccess() {
        aKey.release();
    }
}
