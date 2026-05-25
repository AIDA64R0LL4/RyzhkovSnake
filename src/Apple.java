import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class Apple {

    int x;
    int y;
    int tileSize;

    Random random = new Random();
    public Image appleImage;

    public Apple(int tileSize)
    {
        this.tileSize = tileSize;
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/apple.png")));
        appleImage = icon.getImage();
    }

    public void spawn(Snake snake)
    {
        boolean onSnake;

        do
        {
            onSnake = false;

            x = random.nextInt(GameZone.COLS) * tileSize;
            y = random.nextInt(GameZone.ROWS) * tileSize;

            for (int i = 0; i < snake.x.size(); i++)
            {
                if (snake.x.get(i) == x && snake.y.get(i) == y) {onSnake = true;break;}
            }

        } while (onSnake);
    }

    public void draw(Graphics g)
    {
        g.drawImage(appleImage, x, y + GameZone.PANEL, tileSize, tileSize, null);
    }
}