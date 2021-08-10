package ListenerTest;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class ListenerTest {
  private JFrame mainFrame;

  public ListenerTest() {
    prepareGUI();
  }

  public static void main(String[] args) {
    ListenerTest ListenerTest = new ListenerTest();
  }

  private void prepareGUI() {
    ColorsTest m = new ColorsTest();
    m.addMouseListener(new CustomMouseListener());
    mainFrame = new JFrame("Testing Listener");
    mainFrame.add(m);
    mainFrame.setSize(400, 400);

    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });
    mainFrame.setVisible(true);
  }

  class ColorsTest extends Canvas {
    public void paint(Graphics g) {
      setBackground(Color.YELLOW);
      g.setColor(Color.BLUE);
      g.fillOval(0, 0, 100, 100);
      g.drawString("Arnab", 200, 200);
    }
  }

  class CustomMouseListener implements MouseListener {
    public void mouseClicked(MouseEvent e) {
      System.out.println("Mouse Clicked: (" + e.getX() + ", " + e.getY() + ")");
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
  }
}
