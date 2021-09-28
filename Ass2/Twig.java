
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Twig {
  // main class

  private JFrame mainFrame;
  private GridCanvas canvas;
  private JPanel gridPanel;
  private Color gridColor;
  private int gap;

  private ArrayList<Point> points = new ArrayList<Point>();
  private ArrayList<Line> lines = new ArrayList<Line>();

  public class Point {
    public int x, y;

    Point(int x, int y) {
      this.x = x;
      this.y = y;
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

  public Twig() {
    gap = 40;
    prepareGUI();
  }

  public static void main(String[] args) {
    Twig coord = new Twig();
  }

  private void prepareGUI() {
    gridPanel = new JPanel();
    canvas = new GridCanvas();
    canvas.setSize(800, 800);
    canvas.addMouseWheelListener(new CustomMouseWheelListener());

    gridPanel.add(canvas);

    mainFrame = new JFrame("Assignment 2 - Q1. Bresenham’s Arnab Sen (510519006)");
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
        gap = gap < 10 ? 10 : gap;
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
      Font font = new Font("Arial", Font.PLAIN, 10);
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
      g.setColor(Color.RED);
      drawTwig(g, new Point(0, -10), 90, 10);
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

    public void drawRectangle(Graphics g, Point pt) {
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
        drawRectangle(g, new Point(Math.round(x), Math.round(y)));
        x += xInc;
        y += yInc;
      }
    }

    public void drawTwig(Graphics g, Point pt, int angle, int length) {
      int x = pt.x;
      int y = pt.y;
      int x1 = x + (int) (length * Math.cos(Math.toRadians(angle)));
      int y1 = y + (int) (length * Math.sin(Math.toRadians(angle)));
      drawLine(g, new Line(pt, new Point(x1, y1)));
      if (length > 0) {
        drawTwig(g, new Point(x1, y1), angle - 20, length - 1);
        drawTwig(g, new Point(x1, y1), angle + 20, length - 1);
      }
    }
  }
}
