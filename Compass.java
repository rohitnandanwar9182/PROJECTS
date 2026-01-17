package REALPROJECTS;

// private double angle = 45;
// Examples:
// 0 → North
// 90 → East
// 180 → South
// 270 → West

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Compass extends JPanel {

    // Angle of needle in degrees (0 = North)
    Scanner sc = new Scanner(System.in);
    private double angle = sc.nextInt(); // try changing this

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;
        int cy = h / 2;
        int radius = Math.min(w, h) / 3;

        // Background
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, w, h);

        // Compass circle
        g2.setColor(Color.BLACK);
        g2.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);

        // Directions
        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.drawString("N", cx - 6, cy - radius + 18);
        g2.drawString("S", cx - 6, cy + radius - 8);
        g2.drawString("E", cx + radius - 16, cy + 6);
        g2.drawString("W", cx - radius + 6, cy + 6);

        // Needle
        double rad = Math.toRadians(angle - 90); // rotate so 0 = North
        int x = (int) (cx + radius * Math.cos(rad));
        int y = (int) (cy + radius * Math.sin(rad));

        g2.setStroke(new BasicStroke(3));
        g2.setColor(Color.RED);
        g2.drawLine(cx, cy, x, y);

        // Center point
        g2.fillOval(cx - 4, cy - 4, 8, 8);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Compass");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Compass());
        frame.setVisible(true);
    }
}