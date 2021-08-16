package ColorTest;

import java.awt.*;
import javax.swing.*;

public class ColorTest extends Canvas {
  public void paint(Graphics g) {
    setBackground(Color.YELLOW);
    g.setColor(Color.BLUE);
    g.fillOval(0, 0, 100, 100);
    g.drawString("Arnab", 200, 200);
  }

  public static void main(String args[]) {
    ColorTest m = new ColorTest();
    JFrame f = new JFrame();
    f.add(m);
    f.setSize(400, 400);
    f.setVisible(true);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}
