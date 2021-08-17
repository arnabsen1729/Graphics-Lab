
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

  public class Point {
    public int x, y;

    Point(int x, int y) {
      this.x = x;
      this.y = y;
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
    JPanel buttonPanel = new JPanel(new GridLayout(3, 1));
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

    // button to draw rectangle
    JButton pointButton = new JButton("Add Point");
    pointButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // show a dialog to enter rectangle coordinates
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

    buttonPanel.add(bgButton);
    buttonPanel.add(fgButton);
    buttonPanel.add(pointButton);

    gridPanel.add(canvas);
    gridPanel.add(buttonPanel);

    mainFrame = new JFrame("Assignment 1 - Arnab Sen (510519006)");
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
  }
}
