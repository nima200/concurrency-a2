package q1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class star {
    public static void main(String[] args) throws IOException, InterruptedException {
        int m = 0, c = 0;
        try {
            m = Integer.parseInt(args[0]);
            if (m > 6) {
                System.out.println("Invalid value for m");
            }
            c = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Both arguments must be integers!");
        }


        Vertex v_1 = new Vertex(-1.0, 5.0);
        Vertex v_2 = new Vertex(1.0, 2.0);
        Vertex v_3 = new Vertex(5.0, 0.0);
        Vertex v_4 = new Vertex(1.0, -2.0);
        Vertex v_5 = new Vertex(-4.0, -4.0);
        Vertex v_6 = new Vertex(-3.0, -1.0);

        v_1.setNext(v_2); v_1.setPrevious(v_6);
        v_2.setNext(v_3); v_2.setPrevious(v_1);
        v_3.setNext(v_4); v_3.setPrevious(v_2);
        v_4.setNext(v_5); v_4.setPrevious(v_3);
        v_5.setNext(v_6); v_5.setPrevious(v_4);
        v_6.setNext(v_1); v_6.setPrevious(v_5);

        DLinkedList vertices = new DLinkedList(v_1);
        List<Thread> movers = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            Thread t = new Thread(new Mover(vertices, i, c));
            movers.add(t);
            t.start();
        }

        for (Thread mover: movers) {
            mover.join();
        }

        BufferedImage img = new BufferedImage(1920,1080,BufferedImage.TYPE_INT_ARGB);
        Color black = new Color(0, 0, 0);
        Vertex current = v_1;
        Graphics graphics = img.getGraphics();
        graphics.setColor(Color.BLACK);

        double[] minXY = findMin(vertices);
        double[] maxXY = findMax(vertices);
        double minX = minXY[0], minY = minXY[1], maxX = maxXY[0], maxY = maxXY[1];
        double currentWidth = maxX - minX;
        double currentHeight = maxY - minY;
        double scaleX = (1920 * 0.9) / currentWidth;
        double scaleY = (1080 * 0.9) / currentHeight;
        double scaleFactor = Math.min(scaleX, scaleY);
        translate(vertices, -minX, -minY);
        scale(vertices, scaleFactor, scaleFactor);
        translate(vertices, -((1920 * 0.9) / 2.0), -((1080 * 0.9) / 2.0));
        translate(vertices, 1920 / 2.0, 1920 / 2.0);

        Polygon p = new Polygon();
        do {
//            int x = 960 + (int) ((current.getX()) * (1920 / 8));
//            int y = 540 + (int) ((current.getY()) * (1080 / 8));
            int x = (int) current.getX();
            int y = (int) current.getY();
            p.addPoint(x, y);
            graphics.fillOval(x, y, 5, 5);
            System.out.println("x: " + ((current.getX()))  + " y: " + current.getY());
            current = current.getNext();
        } while(current != v_1);
        graphics.drawPolygon(p);
        File outputfile = new File("outputimage.png");
        ImageIO.write(img, "png", outputfile);
    }

    public static double[] findMin(DLinkedList vertices) {
        Vertex current = vertices.getHead();
        double minX = current.getX();
        double minY = current.getY();
        do {
            if (current.getX() < minX) minX = current.getX();
            if (current.getY() < minY) minY = current.getY();
            current = current.getNext();
        } while (current != vertices.getHead());
        return new double[] {minX, minY};
    }

    public static double[] findMax(DLinkedList vertices) {
        Vertex current = vertices.getHead();
        double maxX = current.getX();
        double maxY = current.getY();
        do {
            if (current.getX() > maxX) maxX = current.getX();
            if (current.getY() > maxY) maxY = current.getY();
            current = current.getNext();
        } while (current != vertices.getHead());
        return new double[] {maxX, maxY};
    }

    public static void translate(DLinkedList vertices, double translateX, double translateY) {
        Vertex current = vertices.getHead();
        do {
            current.setX(current.getX() + translateX);
            current.setY(current.getY() + translateY);
            current = current.getNext();
        } while (current != vertices.getHead());
    }

    public static void scale(DLinkedList vertices, double scaleX, double scaleY) {
        Vertex current = vertices.getHead();
        do {
            current.setX(current.getX() * scaleX);
            current.setY(current.getY() * scaleY);
            current = current.getNext();
        } while (current != vertices.getHead());
    }

}
