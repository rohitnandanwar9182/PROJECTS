package REALPROJECTS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class BlackHole3D extends JPanel implements ActionListener {

    Timer timer = new Timer(30, this);
    double angle = 0;
    Random rand = new Random();

    static class Particle {
        double radius;
        double angle;
        double depth;

        Particle() {
            radius = 50 + Math.random() * 120;
            angle = Math.random() * Math.PI * 2;
            depth = Math.random();
        }
    }

    Particle[] particles = new Particle[400];

    public BlackHole3D() {
        for (int i = 0; i < particles.length; i++)
            particles[i] = new Particle();
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

       

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        // Space background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Black hole center
        g2.setColor(Color.BLACK);
        g2.fillOval(cx - 35, cy - 35, 70, 70);

        // Accretion disk particles
        for (Particle p : particles) {
            double a = p.angle + angle;
            double x = Math.cos(a) * p.radius;
            double y = Math.sin(a) * p.radius * 0.5;

            double scale = 0.5 + p.depth;
            int size = (int) (4 * scale);

            int px = (int) (cx + x);
            int py = (int) (cy + y);

            g2.setColor(new Color(255, 140, 0, 120));
            g2.fillOval(px, py, size, size);

            p.angle += 0.002 + p.depth * 0.01;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        angle += 0.01;
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("3D Black Hole Visualization");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new BlackHole3D());
        frame.setVisible(true);
    }
}



//more realistic code //

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;
// import java.util.Random;

// public class RealisticBlackHole extends JPanel implements ActionListener {

//     Timer timer = new Timer(30, this);
//     double angle = 0;
//     Random rand = new Random();

//     static class Particle {
//         double radius;
//         double angle;
//         double depth;

//         Particle() {
//             radius = 30 + Math.random() * 170;
//             angle = Math.random() * Math.PI * 2;
//             depth = Math.random(); // 0 (far) → 1 (near)
//         }
//     }

//     Particle[] particles = new Particle[600];

//     public RealisticBlackHole() {
//         for (int i = 0; i < particles.length; i++)
//             particles[i] = new Particle();
//         timer.start();
//     }

//     @Override
//     protected void paintComponent(Graphics g) {
//         super.paintComponent(g);
//         Graphics2D g2 = (Graphics2D) g;

//         int cx = getWidth() / 2;
//         int cy = getHeight() / 2;

//         // Smooth rendering
//         g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                             RenderingHints.VALUE_ANTIALIAS_ON);

//         // Space background
//         g2.setColor(Color.BLACK);
//         g2.fillRect(0, 0, getWidth(), getHeight());

//         // Black hole center (event horizon)
//         int bhSize = 50;
//         g2.setColor(Color.BLACK);
//         g2.fillOval(cx - bhSize / 2, cy - bhSize / 2, bhSize, bhSize);

//         // Accretion disk particles
//         for (Particle p : particles) {
//             double a = p.angle + angle;

//             // Elliptical disk effect for 3D illusion
//             double x = Math.cos(a) * p.radius;
//             double y = Math.sin(a) * p.radius * (0.2 + 0.3 * (1 - p.depth)); 

//             // Size & brightness based on depth
//             double scale = 0.5 + p.depth;
//             int size = (int) (3 + 6 * scale);

//             int px = (int) (cx + x);
//             int py = (int) (cy + y);

//             // Color gradient: orange → yellow → white
//             int r = 255;
//             int gCol = (int) (140 + 115 * p.depth); // 140 → 255
//             int b = (int) (50 * (1 - p.depth)); // darker at far
//             g2.setColor(new Color(r, gCol, b, 180));

//             g2.fillOval(px, py, size, size);

//             // Faster rotation for inner particles
//             p.angle += 0.002 + 0.01 * (1 / (p.radius / 50));
//         }

//         // Optional: Add faint lensing/glow
//         int glowRadius = 100;
//         GradientPaint gp = new GradientPaint(cx, cy, new Color(255, 200, 0, 50),
//                                              cx, cy + glowRadius, new Color(0, 0, 0, 0));
//         g2.setPaint(gp);
//         g2.fillOval(cx - glowRadius, cy - glowRadius, glowRadius * 2, glowRadius * 2);
//     }

//     @Override
//     public void actionPerformed(ActionEvent e) {
//         angle += 0.02; // Overall rotation
//         repaint();
//     }

//     public static void main(String[] args) {
//         JFrame frame = new JFrame("Realistic Black Hole");
//         frame.setSize(700, 700);
//         frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         frame.add(new RealisticBlackHole());
//         frame.setVisible(true);
//     }
// }
