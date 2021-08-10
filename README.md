# Graphics

## `JFrame`

`JFrame` is responsible for creating the native window.

### `setVisible`

But by default it doesn't show any window on run. For that we need to use the `setVisible` method.

### `setSize`

Sets the size of the window in `px`.


We will always have this function
```java
 public static void main(String[] args) {
  Test m = new Test();
  JFrame f = new JFrame();
  f.add(m);
  f.setSize(400, 400);
  f.setVisible(true);
  f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
```

https://www.tutorialspoint.com/swing/swing_mouse_listener.htm

# Standard Strucuture of Program

```java

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

public class ListenerTest {
  // main class

  private JFrame mainFrame;

  public ListenerTest() {
    // constructor of the main class will call the prepareGUI;
    prepareGUI();
  }

  public static void main(String[] args) {
    // we create a new object of this class and when we do that, the constructor
    // is called. And that paints the window.
    ListenerTest ListenerTest = new ListenerTest();
  }

  private void prepareGUI() {
    ColorsTest m = new ColorsTest();

    // add the MouseListener to the
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
    // Canvas class where we override the paint function.
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
```
