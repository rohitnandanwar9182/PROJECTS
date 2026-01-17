package REALPROJECTS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RocketSimulation3D extends JPanel implements ActionListener {

    Timer timer = new Timer(30, this);
    double rocketY = 0; // rocket vertical position
    boolean satelliteReleased = false;
    double satelliteAngle = 0; // satellite orbit angle
    int earthRadius = 80;

    // Satellite orbit radius
    int orbitRadius = 200;

    public RocketSimulation3D() {
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int cx = getWidth() / 2;
        int cy = getHeight() - 100; // position of Earth

        // Smooth rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Space background
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());

        // Draw Earth
        g2.setColor(Color.BLUE);
        g2.fillOval(cx - earthRadius, cy - earthRadius, earthRadius * 2, earthRadius * 2);

        // Draw rocket
        int rocketWidth = 20;
        int rocketHeight = 50;

        // Perspective effect: shrink rocket as it goes higher
        double scale = 1.0 - rocketY / (getHeight() * 1.2);
        int rWidth = (int) (rocketWidth * scale);
        int rHeight = (int) (rocketHeight * scale);

        int rocketX = cx - rWidth / 2;
        int rocketScreenY = (int) (cy - rocketY - rHeight);

        g2.setColor(Color.RED);
        g2.fillRect(rocketX, rocketScreenY, rWidth, rHeight);

        // Draw satellite in orbit after release
        if (satelliteReleased) {
            double satX = cx + orbitRadius * Math.cos(satelliteAngle);
            double satY = cy - orbitRadius * Math.sin(satelliteAngle) * 0.5; // elliptical orbit for 3D effect
            int satSize = 15;
            g2.setColor(Color.YELLOW);
            g2.fillOval((int)(satX - satSize/2), (int)(satY - satSize/2), satSize, satSize);

            // Draw orbit path
            g2.setColor(new Color(255, 255, 255, 50));
            g2.drawOval(cx - orbitRadius, cy - (int)(orbitRadius * 0.5), orbitRadius * 2, (int)(orbitRadius * 0.5) * 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Rocket moves up
        if (!satelliteReleased) {
            rocketY += 2;

            // Release satellite at a certain height
            if (rocketY > 250) {
                satelliteReleased = true;
            }
        } else {
            // Satellite orbits
            satelliteAngle += 0.02;
        }

        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Rocket and Satellite 3D Simulation");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new RocketSimulation3D());
        frame.setVisible(true);
    }
}
