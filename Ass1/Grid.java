
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Grid {
    // main class

    private JFrame mainFrame;
    private GridCanvas canvas;
    private JPanel gridPanel;
    private Color gridColor;
    private int gap;

    public Grid() {
        gap = 20;
        prepareGUI();
    }

    public static void main(String[] args) {
        Grid coord = new Grid();
    }

    private void prepareGUI() {
        gridPanel = new JPanel();
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        canvas = new GridCanvas();
        canvas.setSize(800, 800);
        // canvas.addMouseListener(new CustomMouseListener());
        canvas.addMouseWheelListener(new CustomMouseWheelListener());

        JButton bgButton = new JButton("Background");
        bgButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(mainFrame, "Choose a color", Color.BLACK);
                canvas.setBackground(color);
            }
        });

        JButton fgButton = new JButton("Grid");
        fgButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(mainFrame, "Choose a color", Color.BLACK);
                gridColor = color;
                canvas.repaint();
            }
        });
        buttonPanel.add(bgButton);
        buttonPanel.add(fgButton);

        gridPanel.add(canvas);
        gridPanel.add(buttonPanel);

        mainFrame = new JFrame("Assignment 1");
        mainFrame.add(gridPanel);
        mainFrame.setSize(1000, 800);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        mainFrame.setVisible(true);
    }

    class CustomMouseWheelListener implements MouseWheelListener {
        public void mouseWheelMoved(MouseWheelEvent e) {
            int notches = e.getWheelRotation();
            if (notches < 0) {
                gap = gap + 10;
                gap = gap > 100 ? 100 : gap;
            } else {
                gap = gap - 10;
                gap = gap < 30 ? 30 : gap;
            }
            canvas.repaint();
        }
    }

    class GridCanvas extends Canvas {
        // Canvas class where we override the paint function.
        int originX, originY;
        int height, width;

        public void paint(Graphics g) {
            g.setColor(Color.BLACK);
            height = canvas.getHeight();
            width = canvas.getWidth();
            originX = (canvas.getX() + width) / 2;
            originY = (canvas.getY() + height) / 2;
            drawXaxis(g);
            drawYaxis(g);
            drawOriginCircle(g);
            drawHorizontalLines(g);
            drawVeritcalLines(g);
        }

        public void drawOriginCircle(Graphics g) {
            g.setColor(Color.RED);
            g.fillOval(originX - 5, originY - 5, 10, 10);
        }

        public void drawXaxis(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillRect(0, originY - 2, width, 4);
        }

        public void drawYaxis(Graphics g) {
            g.setColor(Color.BLUE);
            g.fillRect(originX - 2, 0, 4, height);
        }

        public void drawHorizontalLines(Graphics g) {
            g.setColor(gridColor);
            int yCord = 0;

            for (int i = originX; i <= width; i += gap, yCord--) {
                g.drawLine(i, 0, i, height);
                g.drawString(String.valueOf(yCord), originX - 20, i + 30);

            }
            yCord = 0;
            for (int i = originX; i >= 0; i -= gap, yCord++) {
                g.drawLine(i, 0, i, height);
                g.drawString(String.valueOf(yCord), originX - 20, i + 30);

            }
        }

        public void drawVeritcalLines(Graphics g) {
            g.setColor(gridColor);
            int xCord = 0;
            for (int i = originY; i <= height; i += gap, xCord++) {
                g.drawLine(0, i, width, i);
                // add coordinate text
                g.drawString(String.valueOf(xCord), i + 30, originY + 20);
            }
            xCord = 0;
            for (int i = originY; i >= 0; i -= gap, xCord--) {
                g.drawLine(0, i, width, i);
                g.drawString(String.valueOf(xCord), i + 30, originY + 20);

            }
        }
    }
}
