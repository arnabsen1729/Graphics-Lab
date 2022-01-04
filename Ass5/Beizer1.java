
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class Beizer1 {
    // main class

    private JFrame mainFrame;
    private GridCanvas canvas;
    private JPanel gridPanel;
    private Color gridColor;
    private int gap;

    private ArrayList<Circle> circles = new ArrayList<Circle>();

    public class Point {
        public int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public class Circle {
        public Point center;
        int radius;

        Circle(Point c, int r) {
            this.center = c;
            this.radius = r;
        }
    }

    public class Line {
        public Point p1, p2;

        Line() {
            p1 = new Point(0, 0);
            p2 = new Point(0, 0);
        }

        Line(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
        }
    }

    public Beizer1() {
        gap = 40;
        prepareGUI();
    }

    public static void main(String[] args) {
        Beizer1 coord = new Beizer1();
    }

    private void prepareGUI() {
        gridPanel = new JPanel();
        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
        canvas = new GridCanvas();
        canvas.setSize(800, 800);
        // canvas.addMouseListener(new CustomMouseListener());
        canvas.addMouseWheelListener(new CustomMouseWheelListener());

        // button to draw point

        gridPanel.add(canvas);
        gridPanel.add(buttonPanel);

        mainFrame = new JFrame("Assignment 5");
        mainFrame.add(gridPanel);
        mainFrame.setSize(1300, 900);
        mainFrame.addComponentListener(new ResizeListener());
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
                gap = gap + 2;
                gap = gap > 150 ? 150 : gap;
            } else {
                gap = gap - 2;
                gap = gap < 2 ? 2 : gap;
            }
            canvas.repaint();
        }
    }

    // class to detect frame resize
    class ResizeListener implements ComponentListener {
        public void componentResized(ComponentEvent e) {
            int height = mainFrame.getHeight();
            int width = mainFrame.getWidth();
            canvas.setSize(width - 200, height);
            canvas.repaint();
        }

        public void componentMoved(ComponentEvent e) {

        }

        public void componentShown(ComponentEvent e) {

        }

        public void componentHidden(ComponentEvent e) {
        }
    }

    class GridCanvas extends Canvas {
        // Canvas class where we override the paint function.
        int originX, originY;
        int height, width;
        // font

        public void paint(Graphics g) {
            g.setColor(Color.BLACK);
            Font font = new Font("Arial", Font.PLAIN, 20);
            g.setFont(font);
            height = canvas.getHeight();
            width = canvas.getWidth();
            originX = (canvas.getX() + width) / 2;
            originY = (canvas.getY() + height) / 2;
            drawXaxis(g);
            drawYaxis(g);
            drawOriginCircle(g);
            drawHorizontalLines(g);
            drawVerticalLines(g);

            Point pts[] = new Point[4];
            pts[0] = (new Point(0, 5));
            pts[1] = (new Point(10, 15));
            pts[2] = (new Point(15, 5));
            pts[3] = (new Point(20, 30));
            drawCurve(g, pts);
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

        public void drawVerticalLines(Graphics g) {
            g.setColor(Color.BLACK);
            int yCord = 0;

            for (int i = originX; i <= width; i += gap, yCord--) {
                g.drawLine(i, 0, i, height);
                // g.drawString(String.valueOf(-1 * yCord), i - 10, originY);
            }
            yCord = 0;
            for (int i = originX; i >= canvas.getX(); i -= gap, yCord++) {
                g.drawLine(i, 0, i, height);
                // drawstring with font
                // g.drawString(String.valueOf(-1 * yCord), i - 10, originY);
            }
        }

        public void drawHorizontalLines(Graphics g) {
            g.setColor(Color.BLACK);
            int xCord = 0;
            for (int i = originY; i <= height; i += gap, xCord++) {
                g.drawLine(0, i, width, i);
                // g.drawString(String.valueOf(-1 * xCord), originX, i + 5);
            }
            xCord = 0;
            for (int i = originY; i >= canvas.getY(); i -= gap, xCord--) {
                g.drawLine(0, i, width, i);
                // g.drawString(String.valueOf(-1 * xCord), originX, i + 5);
            }
        }

        public void drawPoint(Graphics g, Point pt) {
            g.setColor(Color.BLACK);
            int appletX = originX + pt.x * gap;
            int appletY = originY - pt.y * gap;
            g.fillRect(appletX - (gap / 2), appletY - (gap / 2), gap, gap);
        }

        public void drawLine(Graphics g, Line line) {
            int x1 = line.p1.x;
            int y1 = line.p1.y;
            int x2 = line.p2.x;
            int y2 = line.p2.y;
            int dx = x2 - x1;
            int dy = y2 - y1;
            int steps = Math.abs(dx) > Math.abs(dy) ? Math.abs(dx) : Math.abs(dy);
            float xInc = dx / (float) steps;
            float yInc = dy / (float) steps;
            float x = x1;
            float y = y1;
            for (int i = 0; i <= steps; i++) {
                drawPoint(g, new Point(Math.round(x), Math.round(y)));
                x += xInc;
                y += yInc;
            }
        }

        public long ncr(int n, int r) {
            long result = 1;

            for (int i = 1; i <= r; i++) {
                result *= n - r + i;
                result /= i;
            }

            return result;
        }

        public List<Long> getCombinations(int n) {
            List<Long> combinations = new ArrayList<>();
            for (int i = 0; i <= n; i++) {
                combinations.add(ncr(n, i));
            }
            return combinations;
        }

        public void drawCurve(Graphics g, Point[] controlPoints) {
            int n = controlPoints.length;
            var combinations = getCombinations(n - 1);

            for (double t = 0.0; t <= 1; t += 0.00001) {
                double x_new = 0.0, y_new = 0.0;
                for (int i = 0; i < n; i++) {
                    Double bPoly = combinations.get(i) * Math.pow(1 - t, n - i - 1) * Math.pow(t, i);
                    x_new += controlPoints[i].x * bPoly;
                    y_new += controlPoints[i].y * bPoly;
                }
                drawPoint(g, new Point((int) Math.round(x_new), (int) Math.round(y_new)));
            }
        }
    }
}
