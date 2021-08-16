
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

  private ArrayList<Rectangle> rectangles = new ArrayList<Rectangle>();

  public class Rectangle {
    public int x, y, width, height;

    Rectangle(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }
  }

  public Grid3() {
    gap = 20;
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
    JButton rectButton = new JButton("Rectangle");
    rectButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // show a dialog to enter rectangle coordinates
        JPanel coordPanel = new JPanel(new GridLayout(0, 1));
        JTextField x = new JTextField(2);
        JTextField y = new JTextField(2);
        JTextField w = new JTextField(2);
        JTextField h = new JTextField(2);

        coordPanel.add(new JLabel("X coordinate: "));
        coordPanel.add(x);
        coordPanel.add(new JLabel("Y coordinate: "));
        coordPanel.add(y);
        coordPanel.add(new JLabel("Width: "));
        coordPanel.add(w);
        coordPanel.add(new JLabel("Height: "));
        coordPanel.add(h);

        int result = JOptionPane.showConfirmDialog(mainFrame, coordPanel, "Enter coordinates",
            JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && x.getText().length() > 0 && y.getText().length() > 0) {
          int xCoord = Integer.parseInt(x.getText());
          int yCoord = Integer.parseInt(y.getText());
          int width = Integer.parseInt(w.getText());
          int height = Integer.parseInt(h.getText());
          Rectangle rect = new Rectangle(xCoord, yCoord, width, height);
          rectangles.add(rect);

          canvas.repaint();
        }
      }
    });

    buttonPanel.add(bgButton);
    buttonPanel.add(fgButton);
    buttonPanel.add(rectButton);

    gridPanel.add(canvas);
    gridPanel.add(buttonPanel);

    mainFrame = new JFrame("Assignment 1 - Arnab Sen (510519006)");
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

      // iterate through rectangles and draw them
      for (int i = 0; i < rectangles.size(); i++) {
        Rectangle rect = (Rectangle) rectangles.get(i);
        drawRectangle(g, rect);
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

    public void drawHorizontalLines(Graphics g) {
      g.setColor(gridColor);
      int yCord = 0;

      for (int i = originX; i <= width; i += gap, yCord--) {
        g.drawLine(i, 0, i, height);
        g.drawString(String.valueOf(yCord), originX - 20, i);

      }
      yCord = 0;
      for (int i = originX; i >= 0; i -= gap, yCord++) {
        g.drawLine(i, 0, i, height);
        g.drawString(String.valueOf(yCord), originX - 20, i);

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

    public void drawLine(Graphics g, int x, int y, int x2, int y2) {
      g.setColor(Color.BLUE);
      int appletX = originX + x * gap;
      int appletY = originY - y * gap;
      int appletX2 = originX + x2 * gap;
      int appletY2 = originY - y2 * gap;

      g.drawLine(appletX, appletY, appletX2, appletY2);
    }

    public void drawRectangle(Graphics g, Rectangle rect) {
      int appletX = originX + rect.x * gap;
      int appletY = originY - rect.y * gap;
      g.setColor(Color.BLUE);

      g.fillRect(appletX, appletY, rect.width * gap, rect.height * gap);
    }
  }
}
