package q1;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class star {
    private static final List<Thread> aThreads = new ArrayList<>();
    public static void main(String[] args) throws IOException {
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

        for (int i = 0; i < m; i++) {
            Thread t = new Thread(new Mover(vertices, i, c));
            t.start();
        }

        BufferedImage img = new BufferedImage(1920,1080,BufferedImage.TYPE_INT_ARGB);
        Color red = new Color(255, 0, 0);
        Color black = new Color(0, 0, 0);
        Vertex current = v_1;

        for (int i = 0; i < 1920; i++) {
            img.setRGB(i, 540, black.getRGB());
        }

        for (int i = 0; i < 1080; i++) {
            img.setRGB(960, i, black.getRGB());
        }

        do {
            int x = 960 + (int) ((current.getX()) * 300);
            int y = 540 + (int) ((current.getY()) * 300);
            Graphics2D g = (Graphics2D) img.getGraphics();
            g.setColor(Color.BLACK);
            g.fillOval(x, y, 10, 10);
            System.out.println("x: " + ((current.getX()) * 400)  + " y: " + ((current.getY()) * 400));
            current = current.getNext();
        } while(current != v_1);



        File outputfile = new File("outputimage.png");
        ImageIO.write(img, "png", outputfile);
    }
}
