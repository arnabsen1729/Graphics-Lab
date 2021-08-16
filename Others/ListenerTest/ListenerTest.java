package ListenerTest;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ListenerTest {
  private JFrame mainFrame;
  ColorsTest m;
  Color a;

  public ListenerTest() {
    a = Color.RED;
    prepareGUI();
  }

  public static void main(String[] args) {
    ListenerTest ListenerTest = new ListenerTest();
  }

  public class ColorsTest extends Canvas {
    public void paint(Graphics g) {
      setBackground(Color.YELLOW);
      g.setColor(a);
      g.fillOval(0, 0, 100, 100);
      g.drawString("Arnab", 200, 200);
    }
  }

  private void prepareGUI() {
    m = new ColorsTest();
    m.addMouseListener(new CustomMouseListener());
    m.addKeyListener(new CustomKeyboardListener());
    mainFrame = new JFrame("Testing Listener");
    mainFrame.add(m);
    JTextField userText = new JTextField(6);
    mainFrame.add(userText);
    mainFrame.setSize(400, 400);

    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });
    mainFrame.setVisible(true);
  }

  class CustomMouseListener implements MouseListener {
    public void mouseClicked(MouseEvent e) {
      a = Color.BLUE;
      m.repaint();
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

  class CustomKeyboardListener implements KeyListener {
    public void keyPressed(KeyEvent e) {
      System.out.println("Key Pressed: " + e.getKeyCode());
    }

    public void keyReleased(KeyEvent e) {
      System.out.println("Key Released: " + e.getKeyCode());
    }

    public void keyTyped(KeyEvent e) {
      System.out.println("Key Typed: " + e.getKeyChar());
    }
  }
}
