
// Assignment 3
// Part 1, Circle Midpoint circle drawing algorithm
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class DrawCircle {
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

  public DrawCircle() {
    gap = 40;
    prepareGUI();
  }

  public static void main(String[] args) {
    DrawCircle coord = new DrawCircle();
  }

  private void prepareGUI() {
    gridPanel = new JPanel();
    JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
    canvas = new GridCanvas();
    canvas.setSize(800, 800);
    // canvas.addMouseListener(new CustomMouseListener());
    canvas.addMouseWheelListener(new CustomMouseWheelListener());

    // button to draw point
    JButton circleBtn = new JButton("Add Circle");
    circleBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // show a dialog to enter point coordinates
        JPanel coordPanel = new JPanel(new GridLayout(0, 1));
        JTextField x = new JTextField(2);
        JTextField y = new JTextField(2);
        JTextField r = new JTextField(2);

        coordPanel.add(new JLabel("Center X Coord: "));
        coordPanel.add(x);
        coordPanel.add(new JLabel("Center Y Coord "));
        coordPanel.add(y);
        coordPanel.add(new JLabel("Radius: "));
        coordPanel.add(r);

        int result = JOptionPane.showConfirmDialog(mainFrame, coordPanel, "Enter coordinates for point",
            JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && x.getText().length() > 0 && y.getText().length() > 0
            && r.getText().length() > 0) {
          int xCoord = Integer.parseInt(x.getText());
          int yCoord = Integer.parseInt(y.getText());
          int radius = Integer.parseInt(r.getText());

          Point pt = new Point(xCoord, yCoord);
          circles.add(new Circle(pt, radius));
          canvas.repaint();
        }
      }
    });

    buttonPanel.add(circleBtn);

    gridPanel.add(canvas);
    gridPanel.add(buttonPanel);

    mainFrame = new JFrame("Assignment 3 - Q1. Circle using Mid Point Arnab Sen (510519006)");
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
        gap = gap + 10;
        gap = gap > 150 ? 150 : gap;
      } else {
        gap = gap - 10;
        gap = gap < 20 ? 20 : gap;
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

      // loop and draw all circles
      for (Circle c : circles) {
        drawCircle(g, c);
      }
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
        g.drawString(String.valueOf(-1 * yCord), i - 10, originY);
      }
      yCord = 0;
      for (int i = originX; i >= canvas.getX(); i -= gap, yCord++) {
        g.drawLine(i, 0, i, height);
        // drawstring with font
        g.drawString(String.valueOf(-1 * yCord), i - 10, originY);
      }
    }

    public void drawHorizontalLines(Graphics g) {
      g.setColor(Color.BLACK);
      int xCord = 0;
      for (int i = originY; i <= height; i += gap, xCord++) {
        g.drawLine(0, i, width, i);
        g.drawString(String.valueOf(-1 * xCord), originX, i + 5);
      }
      xCord = 0;
      for (int i = originY; i >= canvas.getY(); i -= gap, xCord--) {
        g.drawLine(0, i, width, i);
        g.drawString(String.valueOf(-1 * xCord), originX, i + 5);
      }
    }

    public void drawPoint(Graphics g, Point pt) {
      g.setColor(Color.BLACK);
      int appletX = originX + pt.x * gap;
      int appletY = originY - pt.y * gap;
      g.fillRect(appletX - (gap / 2), appletY - (gap / 2), gap, gap);
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
  }
}
