
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

public class Grid3 {
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

  public Grid3() {
    gap = 40;
    prepareGUI();
  }

  public static void main(String[] args) {
    Grid3 coord = new Grid3();
  }

  private void prepareGUI() {
    gridPanel = new JPanel();
    JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
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

    JButton fgButton = new JButton("Grid3");
    fgButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Color color = JColorChooser.showDialog(mainFrame, "Choose a color", Color.BLACK);
        gridColor = color;
        canvas.repaint();
      }
    });

    // button to draw point
    JButton pointButton = new JButton("Add Point");
    pointButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // show a dialog to enter point coordinates
        JPanel coordPanel = new JPanel(new GridLayout(0, 1));
        JTextField x = new JTextField(2);
        JTextField y = new JTextField(2);

        coordPanel.add(new JLabel("X coordinate: "));
        coordPanel.add(x);
        coordPanel.add(new JLabel("Y coordinate: "));
        coordPanel.add(y);

        int result = JOptionPane.showConfirmDialog(mainFrame, coordPanel, "Enter coordinates for point",
            JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && x.getText().length() > 0 && y.getText().length() > 0) {
          int xCoord = Integer.parseInt(x.getText());
          int yCoord = Integer.parseInt(y.getText());

          Point pt = new Point(xCoord, yCoord);
          points.add(pt);
          canvas.repaint();
        }
      }
    });

    // button to draw line
    JButton lineButton = new JButton("Add Line");
    lineButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // show a dialog to enter point coordinates
        JPanel coordPanel = new JPanel(new GridLayout(0, 1));
        JTextField x1 = new JTextField(2);
        JTextField y1 = new JTextField(2);
        JTextField x2 = new JTextField(2);
        JTextField y2 = new JTextField(2);

        coordPanel.add(new JLabel("X1 coordinate: "));
        coordPanel.add(x1);
        coordPanel.add(new JLabel("Y1 coordinate: "));
        coordPanel.add(y1);
        coordPanel.add(new JLabel("X2 coordinate: "));
        coordPanel.add(x2);
        coordPanel.add(new JLabel("Y2 coordinate: "));
        coordPanel.add(y2);

        int result = JOptionPane.showConfirmDialog(mainFrame, coordPanel, "Enter coordinates for line",
            JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && x1.getText().length() > 0 && y1.getText().length() > 0
            && x2.getText().length() > 0 && y2.getText().length() > 0) {
          int x1Coord = Integer.parseInt(x1.getText());
          int y1Coord = Integer.parseInt(y1.getText());
          int x2Coord = Integer.parseInt(x2.getText());
          int y2Coord = Integer.parseInt(y2.getText());

          Point pt1 = new Point(x1Coord, y1Coord);
          Point pt2 = new Point(x2Coord, y2Coord);

          Line line = new Line(pt1, pt2);
          lines.add(line);
          canvas.repaint();
        }
      }
    });

    buttonPanel.add(bgButton);
    buttonPanel.add(fgButton);
    buttonPanel.add(pointButton);
    buttonPanel.add(lineButton);

    gridPanel.add(canvas);
    gridPanel.add(buttonPanel);

    mainFrame = new JFrame("Assignment 2 - Q1. Mid Point Arnab Sen (510519006)");
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

      // iterate through rectangles and draw them
      for (Point pt : points) {
        drawRectangle(g, pt);
      }

      // iterate through lines and draw them
      for (Line line : lines) {
        drawLine(g, line);
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
      // midpoint algorithm
      int x1 = line.p1.x;
      int y1 = line.p1.y;
      int x2 = line.p2.x;
      int y2 = line.p2.y;

      if (x2 < x1 && y2 < y1) {
        int temp = x1;
        x1 = x2;
        x2 = temp;
        temp = y1;
        y1 = y2;
        y2 = temp;
      }

      int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1);
      int m = dx - dy, p;
      while (x1 != x2 && y1 != y2) {
        drawRectangle(g, new Point(x1, y1));

        p = 2 * m;

        if (p > -dy) {
          m = m - dy;
          if (x1 < x2)
            x1++;
          else
            x1--;
        }

        if (p < dx) {
          m = m + dx;
          if (y1 < y2)
            y1++;
          else
            y1--;
        }
      }
      drawRectangle(g, new Point(x2, y2));

    }
  }
}
