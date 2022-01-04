
// Assignment 4
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.lang.Math;

public class Animal {
    // main class

    private JFrame mainFrame;
    private GridCanvas canvas;
    private JPanel gridPanel;
    private Color gridColor;
    private int gap;

    private ArrayList<Ellipse> ellipses = new ArrayList<Ellipse>();
    private ArrayList<Line> lines = new ArrayList<Line>();
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

    public class Ellipse {
        public Point center;
        int radiusX, radiusY;
        double angle;

        Ellipse(Point c, int rx, int ry) {
            this.center = c;
            this.radiusX = rx;
            this.radiusY = ry;
            this.angle = 90;
        }

        Ellipse(Point c, int rx, int ry, int angle) {
            this.center = c;
            this.radiusX = rx;
            this.radiusY = ry;
            this.angle = angle;
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

    public Animal() {
        gap = 5;
        prepareGUI();
    }

    public static void main(String[] args) {
        Animal coord = new Animal();
    }

    private void prepareGUI() {
        canvas = new GridCanvas();
        canvas.setSize(1000, 1000);
        canvas.addMouseWheelListener(new CustomMouseWheelListener());

        mainFrame = new JFrame("Assignment 3 - Q3. Shape 3 Arnab Sen (510519006)");
        mainFrame.setSize(1000, 1000);
        mainFrame.add(canvas);
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
                gap = gap + 1;
                gap = gap > 150 ? 150 : gap;
            } else {
                gap = gap - 1;
                gap = gap < 3 ? 3 : gap;
            }
            canvas.repaint();
        }
    }

    // class to detect frame resize
    class ResizeListener implements ComponentListener {
        public void componentResized(ComponentEvent e) {
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

            int[] parent1 = new int[9];
            parent1[0] = 0; // Beak Size: 0 -> big 1-> small
            parent1[1] = 1; // Beak Type: 0 -> toothy 1-> toothless
            parent1[2] = 1; // Ear Type: 0 -> Circular 1-> Triangular
            parent1[3] = 0; // Body Spots: 0 -> Spotted 1-> Spotless
            parent1[4] = 1; // Body Hair: 0 -> Hairy 1-> Hairless
            parent1[5] = 0; // Arm/Legs Spot: 0 -> Spotted 1-> Spotless
            parent1[6] = 0; // Arm/Legs Hair: 0 -> Hairy 1-> Hairless
            parent1[8] = 1; // Tails Shape: 0 -> Circular 1-> Triangular 2-> Haired ball
            // int refX = 40, refY = 0;
            drawAnimal(g, parent1, -100, 0);

            int[] parent2 = new int[9];
            parent2[0] = 1; // Beak Size: 0 -> big 1-> small
            parent2[1] = 0; // Beak Type: 0 -> toothy 1-> toothless
            parent2[2] = 1; // Ear Type: 0 -> Circular 1-> Triangular
            parent2[3] = 0; // Body Spots: 0 -> Spotted 1-> Spotless
            parent2[4] = 0; // Body Hair: 0 -> Hairy 1-> Hairless
            parent2[5] = 1; // Arm/Legs Spot: 0 -> Spotted 1-> Spotless
            parent2[6] = 0; // Arm/Legs Hair: 0 -> Hairy 1-> Hairless
            parent2[8] = 2; // Tails Shape: 0 -> Circular 1-> Triangular 2-> Haired ball
            drawAnimal(g, parent2, 40, 0);

            // mutate
            int[] child1 = new int[9];
            for (int i = 0; i < 9; i++) {
                child1[i] = Math.max(parent1[i], parent2[i]);
            }
            drawAnimal(g, child1, 200, 0);
        }

        public void drawOriginCircle(Graphics g) {
            g.setColor(Color.RED);
            g.fillOval(originX - 5, originY - 5, 10, 10);
        }

        public void drawXaxis(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(0, originY - 1, width, 2);
        }

        public void drawYaxis(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillRect(originX - 1, 0, 2, height);
        }

        public void drawVerticalLines(Graphics g) {
            g.setColor(Color.WHITE);
            for (int i = originX; i <= width; i += gap) {
                g.drawLine(i, 0, i, height);
            }
            for (int i = originX; i >= canvas.getX(); i -= gap) {
                g.drawLine(i, 0, i, height);
            }
        }

        public void drawHorizontalLines(Graphics g) {
            g.setColor(Color.WHITE);
            for (int i = originY; i <= height; i += gap) {
                g.drawLine(0, i, width, i);
            }
            for (int i = originY; i >= canvas.getY(); i -= gap) {
                g.drawLine(0, i, width, i);
            }
        }

        public void drawEllipse(Graphics g, Ellipse c) {
            int x_c = c.center.x;
            int y_c = c.center.y;
            int rx = c.radiusX;
            int ry = c.radiusY;
            double alpha = c.angle * Math.PI / 180.0;
            float dx, dy, d1, d2, x, y;
            x = 0;
            y = ry;
            // Decision parameter for region-1
            d1 = (ry * ry) - (rx * rx * ry) + (0.25f * rx * rx);
            dx = 2 * ry * ry * x;
            dy = 2 * rx * rx * y;
            while (dx < dy) {
                int x_pivot = (int) x_c;
                int y_pivot = (int) y_c;
                int x_shifted = (int) (x + x_c) - x_pivot;
                int y_shifted = (int) (y + y_c) - y_pivot;
                int f_x = x_pivot + (int) (x_shifted * Math.cos(alpha) - y_shifted * Math.sin(alpha));
                int f_y = y_pivot + (int) (x_shifted * Math.sin(alpha) + y_shifted * Math.cos(alpha));
                // g.fillRect(originX + f_x * gap - gap / 2, originY - f_y * gap - gap / 2, gap,
                // gap);
                drawPoint(g, new Point(f_x, f_y));

                x_shifted = (int) (-x + x_c) - x_pivot;
                y_shifted = (int) (y + y_c) - y_pivot;
                f_x = x_pivot + (int) (x_shifted * Math.cos(alpha) - y_shifted * Math.sin(alpha));
                f_y = y_pivot + (int) (x_shifted * Math.sin(alpha) + y_shifted * Math.cos(alpha));
                drawPoint(g, new Point(f_x, f_y));

                x_shifted = (int) (x + x_c) - x_pivot;
                y_shifted = (int) (-y + y_c) - y_pivot;
                f_x = x_pivot + (int) (x_shifted * Math.cos(alpha) - y_shifted * Math.sin(alpha));
                f_y = y_pivot + (int) (x_shifted * Math.sin(alpha) + y_shifted * Math.cos(alpha));
                drawPoint(g, new Point(f_x, f_y));

                x_shifted = (int) (-x + x_c) - x_pivot;
                y_shifted = (int) (-y + y_c) - y_pivot;
                f_x = x_pivot + (int) (x_shifted * Math.cos(alpha) - y_shifted * Math.sin(alpha));
                f_y = y_pivot + (int) (x_shifted * Math.sin(alpha) + y_shifted * Math.cos(alpha));
                drawPoint(g, new Point(f_x, f_y));

                if (d1 < 0) {
                    x++;
                    dx = dx + (2 * ry * ry);
                    d1 = d1 + dx + (ry * ry);
                } else {
                    x++;
                    y--;
                    dx = dx + (2 * ry * ry);
                    dy = dy - (2 * rx * rx);
                    d1 = d1 + dx - dy + (ry * ry);
                }
            }
            // Decision parameter for region-2
            d2 = ((ry * ry) * ((x + 0.5f) * (x + 0.5f))) + ((rx * rx) * ((y - 1) * (y - 1))) - (rx * rx * ry * ry);
            while (y >= 0) {

                int x_pivot = (int) x_c;
                int y_pivot = (int) y_c;
                int x_shifted = (int) (x + x_c) - x_pivot;
                int y_shifted = (int) (y + y_c) - y_pivot;
                int f_x = x_pivot + (int) (x_shifted * Math.cos(alpha) - y_shifted * Math.sin(alpha));
                int f_y = y_pivot + (int) (x_shifted * Math.sin(alpha) + y_shifted * Math.cos(alpha));
                drawPoint(g, new Point(f_x, f_y));

                x_shifted = (int) (-x + x_c) - x_pivot;
                y_shifted = (int) (y + y_c) - y_pivot;
                f_x = x_pivot + (int) (x_shifted * Math.cos(alpha) - y_shifted * Math.sin(alpha));
                f_y = y_pivot + (int) (x_shifted * Math.sin(alpha) + y_shifted * Math.cos(alpha));
                drawPoint(g, new Point(f_x, f_y));

                x_shifted = (int) (x + x_c) - x_pivot;
                y_shifted = (int) (-y + y_c) - y_pivot;
                f_x = x_pivot + (int) (x_shifted * Math.cos(alpha) - y_shifted * Math.sin(alpha));
                f_y = y_pivot + (int) (x_shifted * Math.sin(alpha) + y_shifted * Math.cos(alpha));
                drawPoint(g, new Point(f_x, f_y));

                x_shifted = (int) (-x + x_c) - x_pivot;
                y_shifted = (int) (-y + y_c) - y_pivot;
                f_x = x_pivot + (int) (x_shifted * Math.cos(alpha) - y_shifted * Math.sin(alpha));
                f_y = y_pivot + (int) (x_shifted * Math.sin(alpha) + y_shifted * Math.cos(alpha));
                drawPoint(g, new Point(f_x, f_y));

                if (d2 > 0) {
                    y--;
                    dy = dy - (2 * rx * rx);
                    d2 = d2 + (rx * rx) - dy;
                } else {
                    y--;
                    x++;
                    dx = dx + (2 * ry * ry);
                    dy = dy - (2 * rx * rx);
                    d2 = d2 + dx - dy + (rx * rx);
                }
            }

        }

        public void drawCircle(Graphics g, Circle c) {
            // draw Circle using mid point algorithm
            int x_centre = c.center.x;
            int y_centre = c.center.y;
            int radius = c.radius;
            int x = radius, y = 0;
            drawPoint(g, new Point(x + x_centre, y + y_centre));

            if (radius > 0) {
                drawPoint(g, new Point(-x + x_centre, y + y_centre));
                drawPoint(g, new Point(y + x_centre, x + y_centre));
                drawPoint(g, new Point(y + x_centre, -x + y_centre));
            }

            int P = 1 - radius;
            while (x > y) {
                y++;
                if (P <= 0)
                    P = P + 2 * y + 1;
                else {
                    x--;
                    P = P + 2 * y - 2 * x + 1;
                }

                if (x < y)
                    break;

                drawPoint(g, new Point(x + x_centre, y + y_centre));
                drawPoint(g, new Point(-x + x_centre, y + y_centre));
                drawPoint(g, new Point(x + x_centre, -y + y_centre));
                drawPoint(g, new Point(-x + x_centre, -y + y_centre));

                if (x != y) {
                    drawPoint(g, new Point(y + x_centre, x + y_centre));
                    drawPoint(g, new Point(-y + x_centre, x + y_centre));
                    drawPoint(g, new Point(y + x_centre, -x + y_centre));
                    drawPoint(g, new Point(-y + x_centre, -x + y_centre));
                }
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

        class beak {
            int base;
            int height;
            int distance;
            Point start;
            int separation;
            Boolean showTooth;

            beak(int base, int height, int distance, Point start, int separation, Boolean showTooth) {
                this.base = base;
                this.height = height;
                this.distance = distance;
                this.start = start;
                this.separation = separation;
                this.showTooth = showTooth;
            }

            public void paint(Graphics g) {
                drawUpRAT(g);
                drawDownRAT(g);
            }

            public void drawUpRAT(Graphics g) {
                // draw a right angles triangle

                Point A = start;
                Point B = new Point(A.x + base, A.y);
                Point C = new Point(A.x + base, A.y + height);

                Line AB = new Line(A, B);
                Line BC = new Line(B, C);
                Line CA = new Line(C, A);

                drawLine(g, AB);
                drawLine(g, BC);
                drawLine(g, CA);

                // draw veritical lines of height 4 at a gap of distance
                if (showTooth) {
                    for (int i = 2; i <= base; i += distance) {
                        drawLine(g, new Line(new Point(A.x + i, A.y), new Point(A.x + i, A.y - distance)));
                    }
                }
            }

            public void drawDownRAT(Graphics g) {
                // draw a right angles triangle
                Point A = new Point(start.x, start.y - separation);
                Point B = new Point(A.x + base, A.y);
                Point C = new Point(A.x + base, A.y - height);

                Line AB = new Line(A, B);
                Line BC = new Line(B, C);
                Line CA = new Line(C, A);

                drawLine(g, AB);
                drawLine(g, BC);
                drawLine(g, CA);

                if (showTooth) {
                    for (int i = 2; i <= base; i += distance) {
                        drawLine(g, new Line(new Point(A.x + i, A.y), new Point(A.x + i, A.y + distance)));
                    }
                }
            }

        }

        class Triangle {
            Point A;
            Point B;
            Point C;

            Triangle(Point A, Point B, Point C) {
                this.A = A;
                this.B = B;
                this.C = C;
            }
        }

        public void drawTriangle(Graphics g, Triangle t) {
            drawLine(g, new Line(t.A, t.B));
            drawLine(g, new Line(t.B, t.C));
            drawLine(g, new Line(t.C, t.A));
        }

        public Point getRotatedPoints(int x, int y, int cx, int cy, double angle) {
            Point p = new Point(x, y);
            Point center = new Point(cx, cy);
            p.x = (int) ((x - center.x) * Math.cos(angle) - (y - center.y) * Math.sin(angle)) + center.x;
            p.y = (int) ((x - center.x) * Math.sin(angle) + (y - center.y) * Math.cos(angle)) + center.y;
            return p;
        }

        public void hairOnEllipse(Graphics g, int refX, int refY, int minor, int major, int angle) {
            for (int i = 0; i < 360; i += 20) {
                // double degree = (Math.random() * 361) * Math.PI / 180;
                double degree = i * Math.PI / 180;
                int xInitial = refX + (int) ((minor - 2) * Math.cos(degree));
                int yInitial = refY + (int) ((major - 2) * Math.sin(degree));
                int xFinal = xInitial + (int) (7 * Math.cos(degree));
                int yFinal = yInitial + (int) (7 * Math.sin(degree));

                drawLine(g, new Line(getRotatedPoints(xInitial, yInitial, refX, refY, angle * Math.PI / 180),
                        getRotatedPoints(xFinal, yFinal, refX, refY, angle * Math.PI / 180)));
            }
        }

        public void drawAnimal(Graphics g, int[] featureVector, int refX, int refY) {

            // drawing Head
            drawCircle(g, new Circle(new Point(refX - 55, refY + 70), 20));

            // drawing Eye
            drawCircle(g, new Circle(new Point(refX - 58, refY + 75), 8));

            if (featureVector[2] == 0) {
                // draw two circular ears
                drawCircle(g, new Circle(new Point(refX - 42, refY + 89), 6));
                drawCircle(g, new Circle(new Point(refX - 40, refY + 87), 6));
            } else {
                // draw two triangular ears
                drawTriangle(g, new Triangle(new Point(refX - 55, refY + 87), new Point(refX + 10 - 55, refY + 87),
                        new Point(refX + 5 - 55, refY + 10 + 87)));
                drawTriangle(g, new Triangle(new Point(refX - 53, refY + 87), new Point(refX + 10 - 53, refY + 87),
                        new Point(refX + 5 - 53, refY + 10 + 87)));

            }
            // drawing body
            drawEllipse(g, new Ellipse(new Point(refX - 25, refY + 20), 30, 45, 40));

            if (featureVector[3] == 0) {
                // drawing spots in body
                drawCircle(g, new Circle(new Point(refX - 25, refY + 20), 5));
                drawCircle(g, new Circle(new Point(refX - 15, refY + 35), 5));
                drawCircle(g, new Circle(new Point(refX - 35, refY + 35), 5));
                drawCircle(g, new Circle(new Point(refX - 25, refY + 45), 5));
                drawCircle(g, new Circle(new Point(refX - 5, refY + 15), 5));
                drawCircle(g, new Circle(new Point(refX - 10, refY), 5));
                drawCircle(g, new Circle(new Point(refX - 30, refY), 5));
                drawCircle(g, new Circle(new Point(refX - 45, refY + 20), 5));
            }

            if (featureVector[4] == 0) {
                for (int i = 0; i < 360; i += 10) {
                    double degree = (Math.random() * 361) * Math.PI / 180;
                    // double degree = i * Math.PI / 180;
                    int xInitial = refX - 25 + (int) ((30 - 2) * Math.cos(degree));
                    int yInitial = refY + 20 + (int) ((45 - 2) * Math.sin(degree));
                    int xFinal = xInitial + (int) (10 * Math.cos(degree));
                    int yFinal = yInitial + (int) (10 * Math.sin(degree));

                    drawLine(g, new Line(getRotatedPoints(xInitial, yInitial, refX - 25, refY + 20, 40 * Math.PI / 180),
                            getRotatedPoints(xFinal, yFinal, refX - 25, refY + 20, 40 * Math.PI / 180)));
                }
            }

            // drawing tail
            if (featureVector[8] == 0) {
                drawEllipse(g, new Ellipse(new Point(refX + 18, refY + 10), 15, 8, 45));
            } else if (featureVector[8] == 1) {
                drawTriangle(g, new Triangle(new Point(refX + 10, refY - 5), new Point(refX + 10, refY - 5 + 15),
                        new Point(refX + 10 + 25, refY - 5 + 15)));
            } else {
                drawCircle(g, new Circle(new Point(refX + 18, refY + 10), 5));
                drawCircle(g, new Circle(new Point(refX + 18, refY + 10), 8));
                hairOnEllipse(g, refX + 18, refY + 10, 8, 8, 0);
            }
            // drawing beak

            Boolean showTooth = (featureVector[1] == 0);

            if (featureVector[0] == 0) {
                beak b = new beak(15, 5, 2, new Point(refX - 85, refY + 70), 5, showTooth);
                b.paint(g);
            } else {
                beak b = new beak(10, 3, 2, new Point(refX - 85, refY + 70), 3, showTooth);
                b.paint(g);
            }

            // drawing thighs
            drawEllipse(g, new Ellipse(new Point(refX + 3, refY - 30), 12, 25, 10));
            drawEllipse(g, new Ellipse(new Point(refX - 13, refY - 35), 10, 20, 170));
            // spots on thighs
            if (featureVector[5] == 0) {
                drawCircle(g, new Circle(new Point(refX + 3, refY - 30), 5));
                drawCircle(g, new Circle(new Point(refX - 13, refY - 35), 5));
            }
            // hair on thighs
            if (featureVector[6] == 0) {
                hairOnEllipse(g, refX + 3, refY - 30, 12, 25, 10);
                hairOnEllipse(g, refX - 13, refY - 35, 10, 20, 170);
            }

            // drawing legs
            drawEllipse(g, new Ellipse(new Point(refX + 2, refY - 65), 8, 20, 180));
            drawEllipse(g, new Ellipse(new Point(refX - 20, refY - 65), 8, 20, 160));
            // spots on legs
            if (featureVector[5] == 0) {
                drawCircle(g, new Circle(new Point(refX + 2, refY - 65), 5));
                drawCircle(g, new Circle(new Point(refX - 20, refY - 65), 5));
            }
            // hair on legs
            if (featureVector[6] == 0) {
                hairOnEllipse(g, refX + 2, refY - 65, 8, 20, 180);
                hairOnEllipse(g, refX - 20, refY - 65, 8, 20, 160);
            }

            // drawing feet
            drawEllipse(g, new Ellipse(new Point(refX, refY - 87), 4, 8, 90));
            drawEllipse(g, new Ellipse(new Point(refX - 27, refY - 85), 4, 8, 90));

            // drawing biceps
            drawEllipse(g, new Ellipse(new Point(refX - 76, refY + 20), 8, 20, 110));
            drawEllipse(g, new Ellipse(new Point(refX - 75, refY + 35), 6, 17, 95));
            // spots on biceps
            if (featureVector[5] == 0) {
                drawCircle(g, new Circle(new Point(refX - 76, refY + 20), 3));
                drawCircle(g, new Circle(new Point(refX - 75, refY + 35), 3));
            }
            // hair on biceps
            if (featureVector[6] == 0) {
                hairOnEllipse(g, refX - 76, refY + 20, 8, 20, 110);
                hairOnEllipse(g, refX - 75, refY + 35, 6, 17, 95);
            }

            // drawing forearms
            drawEllipse(g, new Ellipse(new Point(refX - 100, refY + 20), 6, 17, 60));
            drawEllipse(g, new Ellipse(new Point(refX - 96, refY + 40), 5, 15, 60));
            // spots on forearms
            if (featureVector[5] == 0) {
                drawCircle(g, new Circle(new Point(refX - 100, refY + 20), 3));
                drawCircle(g, new Circle(new Point(refX - 96, refY + 40), 3));
            }
            // hair on forearms
            if (featureVector[6] == 0) {
                hairOnEllipse(g, refX - 100, refY + 20, 6, 17, 60);
                hairOnEllipse(g, refX - 96, refY + 40, 5, 15, 60);
            }

            // drawing palms
            drawCircle(g, new Circle(new Point(refX - 115, refY + 30), 5));
            drawCircle(g, new Circle(new Point(refX - 110, refY + 50), 5));
        }
    }

}
