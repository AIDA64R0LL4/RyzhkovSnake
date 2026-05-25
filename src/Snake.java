import java.awt.event.KeyEvent;
import java.awt.*;
import java.util.ArrayList;

public class Snake {

    ArrayList<Integer> x = new ArrayList<>();
    ArrayList<Integer> y = new ArrayList<>();

    int tileSize;
    char direction = 'R';

    public Snake(int tileSize) {

        this.tileSize = tileSize;
        reset();
    }

    public void reset() {

        x.clear();
        y.clear();

        x.add(7 * tileSize);
        y.add(7 * tileSize);
        x.add(6 * tileSize);
        y.add(7 * tileSize);
        x.add(5 * tileSize);
        y.add(7 * tileSize);

        direction = 'R';
    } //Старт позиция для змеи
    public void move() {

        for (int i = x.size() - 1; i > 0; i--) {

            x.set(i, x.get(i - 1));
            y.set(i, y.get(i - 1));
        }

        switch (direction) {

            case 'U':
                y.set(0, y.get(0) - tileSize);
                break;

            case 'D':
                y.set(0, y.get(0) + tileSize);
                break;

            case 'L':
                x.set(0, x.get(0) - tileSize);
                break;

            case 'R':
                x.set(0, x.get(0) + tileSize);
                break;
        }
    } //Перемещение змеи по полю

    public void Binds(int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (direction != 'D') {direction = 'U';}break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {direction = 'D';}break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {direction = 'L';}break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {direction = 'R';}break;
        }
    }

    public boolean Collision(int width, int height) {

        int headX = x.get(0);
        int headY = y.get(0);

        //Столкновение змеи с границей карты
        if (headX < 0 || headY < 0 || headX >= width || headY >= height)
        {
            return true;
        }

        //Столкновение змеи об себя
        for (int i = 1; i < x.size(); i++)
        {
            if (headX == x.get(i) && headY == y.get(i)) {return true;}
        }

        return false;
    } //Логика змейки, удар об границу и об себя
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (int i = 0; i < x.size(); i++)
        {
            int drawX = x.get(i);
            int drawY = y.get(i) + GameZone.PANEL;

            if (i == 0)
            {
                // Голова змеи
                g2.setColor(new Color(0, 220, 70));
                g2.fillRoundRect(drawX, drawY, tileSize, tileSize, 12, 12);

                g2.setColor(new Color(0, 120, 40));
                g2.drawRoundRect(drawX, drawY, tileSize, tileSize, 12, 12);
            }
            else
            {
                // Тело змеи
                g2.setColor(new Color(0, 180, 0));
                g2.fillRoundRect(drawX, drawY, tileSize, tileSize, 10, 10);

                g2.setColor(new Color(0, 120, 40));
                g2.drawRoundRect(drawX, drawY, tileSize, tileSize, 10, 10);
            }
        }
    } //Сама змейка
    public void grow() {

        x.add(x.get(x.size() - 1));
        y.add(y.get(y.size() - 1));
    }
}