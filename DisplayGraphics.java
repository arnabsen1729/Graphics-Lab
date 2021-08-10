import java.awt.*;
import javax.swing.JFrame;

public class DisplayGraphics extends Canvas {
  public void paint(Graphics g) {
    g.drawOval(30, 130, 180, 180);
  }

  public static void main(String[] args) {
    DisplayGraphics m = new DisplayGraphics();
    JFrame f = new JFrame();
    f.add(m);
    f.setSize(400, 400);
    // f.setLayout(null);
    f.setVisible(true);
  }
}
