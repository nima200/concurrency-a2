package q1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Math.atan2;

public class Vertex {
    private double aX;
    private double aY;
    private double angle;
    private Lock aLock;
    private Vertex previous, next;

    public Vertex(double pX, double pY) {
        aLock = new ReentrantLock();
        aX = pX;
        aY = pY;
        angle = calculateAngle(aX, aY);
    }

    public void move(double pX, double pY) {
        aX = pX;
        aY = pY;
        angle = calculateAngle(aX, aY);
    }

    public void lock() {
        aLock.lock();
    }

    public void unlock() {
        aLock.unlock();
    }

    public double getAngle() {
        return angle;
    }

    public double getX() {
        return aX;
    }

    public double getY() {
        return aY;
    }

    public void setAngle(double pAngle) {
        angle = pAngle;
    }

    public Vertex getPrevious() {
        return previous;
    }

    public void setPrevious(Vertex pPrevious) {
        previous = pPrevious;
    }

    public Vertex getNext() {
        return next;
    }

    public void setNext(Vertex pNext) {
        next = pNext;
    }

    public static double calculateAngle(double pX, double pY) {
        return atan2(pY, pX) - atan2(0, 1);
    }
}
