
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class DrawShape {
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

  public DrawShape() {
    gap = 40;
    prepareGUI();
  }

  public static void main(String[] args) {
    DrawShape coord = new DrawShape();
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

      Shape shapeOb = new Shape();
      shapeOb.paint(g);
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
      g.setColor(gridColor);
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
      g.setColor(gridColor);
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

    class Shape {
      int base;
      int height;
      int distance;
      Point start;
      int separation;

      Shape() {
        base = 23;
        height = 12;
        distance = 4;
        start = new Point(-7, 8);
        separation = 11;
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
        for (int i = 2; i <= base; i += distance) {
          drawLine(g, new Line(new Point(A.x + i, A.y), new Point(A.x + i, A.y - distance)));
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

        for (int i = 2; i <= base; i += distance) {
          drawLine(g, new Line(new Point(A.x + i, A.y), new Point(A.x + i, A.y + distance)));
        }
      }
    }
  }
}
