
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class SwingListenerDemo {
  private JFrame mainFrame;
  private JLabel headerLabel;
  private JLabel statusLabel;
  private JPanel controlPanel;

  public SwingListenerDemo() {
    prepareGUI();
  }

  public static void main(String[] args) {
    SwingListenerDemo swingListenerDemo = new SwingListenerDemo();
    swingListenerDemo.showMouseListenerDemo();
  }

  private void prepareGUI() {
    mainFrame = new JFrame("Java SWING Examples");
    mainFrame.setSize(400, 400);
    mainFrame.setLayout(new GridLayout(3, 1));

    headerLabel = new JLabel("", JLabel.CENTER);
    statusLabel = new JLabel("", JLabel.CENTER);
    statusLabel.setSize(350, 100);

    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });
    controlPanel = new JPanel();
    controlPanel.setLayout(new FlowLayout());

    mainFrame.add(headerLabel);
    mainFrame.add(controlPanel);
    mainFrame.add(statusLabel);
    mainFrame.setVisible(true);
  }

  private void showMouseListenerDemo() {
    headerLabel.setText("Listener in action: MouseListener");

    JPanel panel = new JPanel();
    panel.setBackground(Color.magenta);
    panel.setLayout(new FlowLayout());
    panel.addMouseListener(new CustomMouseListener());

    JLabel msglabel = new JLabel("Welcome to TutorialsPoint SWING Tutorial.", JLabel.CENTER);
    panel.add(msglabel);

    msglabel.addMouseListener(new CustomMouseListener());
    panel.add(msglabel);

    controlPanel.add(panel);
    mainFrame.setVisible(true);
  }

  class CustomMouseListener implements MouseListener {
    public void mouseClicked(MouseEvent e) {
      statusLabel.setText("Mouse Clicked: (" + e.getX() + ", " + e.getY() + ")");
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
