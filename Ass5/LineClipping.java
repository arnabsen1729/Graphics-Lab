
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

public class LineClipping {
    // main class

    private JFrame mainFrame;
    private GridCanvas canvas;
    private JPanel gridPanel;
    private Color gridColor;
    private int gap;

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

    public LineClipping() {
        gap = 40;
        prepareGUI();
    }

    public static void main(String[] args) {
        LineClipping coord = new LineClipping();
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

            cohenSutherlandClip(g, 0, 0, 8, 8);
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

        public int INSIDE = 0; // 0000
        public int LEFT = 1; // 0001
        public int RIGHT = 2; // 0010
        public int BOTTOM = 4; // 0100
        public int TOP = 8; // 1000

        public int x_max = 10;
        public int y_max = 8;
        public int x_min = 4;
        public int y_min = 4;

        public int computeCode(double x, double y) {
            // initialized as being inside
            int code = INSIDE;

            if (x < x_min) // to the left of rectangle
                code |= LEFT;
            else if (x > x_max) // to the right of rectangle
                code |= RIGHT;
            if (y < y_min) // below the rectangle
                code |= BOTTOM;
            else if (y > y_max) // above the rectangle
                code |= TOP;

            return code;
        }

        public void cohenSutherlandClip(Graphics g, double x1, double y1,
                double x2, double y2) {

            double lx = 0, ly = 0;
            // Compute region codes for P1, P2
            int code1 = computeCode(x1, y1);
            int code2 = computeCode(x2, y2);

            // Initialize line as outside the rectangular window
            boolean accept = false;

            while (true) {
                if ((code1 == 0) && (code2 == 0)) {
                    // If both endpoints lie within rectangle
                    accept = true;
                    break;
                } else if ((code1 & code2) > 0) {
                    // If both endpoints are outside rectangle,
                    // in same region
                    break;
                } else {
                    // Some segment of line lies within the
                    // rectangle
                    int code_out;

                    // At least one endpoint is outside the
                    // rectangle, pick it.
                    if (code1 != 0)
                        code_out = code1;
                    else
                        code_out = code2;

                    // Find intersection point;
                    // using formulas y = y1 + slope * (x - x1),
                    // x = x1 + (1 / slope) * (y - y1)
                    if ((code_out & TOP) > 0) {
                        // point is above the clip rectangle
                        lx = x1 + (x2 - x1) * (y_max - y1) / (y2 - y1);
                        ly = y_max;
                    } else if ((code_out & BOTTOM) > 0) {
                        // point is below the rectangle
                        lx = x1 + (x2 - x1) * (y_min - y1) / (y2 - y1);
                        ly = y_min;
                    } else if ((code_out & RIGHT) > 0) {
                        // point is to the right of rectangle
                        ly = y1 + (y2 - y1) * (x_max - x1) / (x2 - x1);
                        lx = x_max;
                    } else if ((code_out & LEFT) > 0) {
                        // point is to the left of rectangle
                        ly = y1 + (y2 - y1) * (x_min - x1) / (x2 - x1);
                        lx = x_min;
                    }

                    // Now intersection point x, y is found
                    // We replace point outside rectangle
                    // by intersection point
                    if (code_out == code1) {
                        x1 = lx;
                        y1 = ly;
                        code1 = computeCode(x1, y1);
                    } else {
                        x2 = lx;
                        y2 = ly;
                        code2 = computeCode(x2, y2);
                    }
                }
            }
            if (accept) {

                drawLine(g, new Line(new Point((int) x1, (int) y1), new Point((int) x2, (int) y2)));
                // Here the user can add code to display the rectangle
                // along with the accepted (portion of) lines
            } else {
                System.out.println("Rejected!");
            }
        }
    }
}
