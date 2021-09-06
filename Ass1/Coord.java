
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Coord {
  private ColorsTest canvas;
  private JFrame mainFrame;

  private int originX = 0;
  private int originY = 0;

  public Coord() {
    prepareGUI();
  }

  public static void main(String[] args) {
    Coord coord = new Coord();
  }

  private void setOrigin() {
    this.originX = canvas.getWidth() / 2;
    this.originY = canvas.getHeight() / 2;
  }

  private void prepareGUI() {
    canvas = new ColorsTest();

    // add the MouseListener to the
    mainFrame = new JFrame("Assignment 1.1");
    mainFrame.add(canvas);
    mainFrame.setSize(500, 500);
    this.setOrigin();
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });
    mainFrame.setVisible(true);
  }

  class ColorsTest extends Canvas {
    // Canvas class where we override the paint function.
    public void paint(Graphics g) {
      setBackground(Color.YELLOW);
      g.setColor(Color.BLUE);
      g.fillOval(0, 0, 100, 100);
      g.drawString("Arnab", 200, 200);
    }
  }
}
