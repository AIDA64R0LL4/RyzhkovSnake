import javax.swing.*;

public class SnakeGame {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Змейка");
        Menu menu = new Menu(frame);
        frame.add(menu);
        frame.pack();
        frame.setSize(frame.getWidth(), frame.getHeight() + 35);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
    }
}
