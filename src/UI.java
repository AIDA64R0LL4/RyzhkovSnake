import java.awt.*;

public class UI {

    public void drawZone(Graphics g, int WIDTH, int GAME_HEIGHT, int TOP_PANEL, int TILE_SIZE)
    {
        boolean colorSwitch = false;

        for (int y = TOP_PANEL; y < GAME_HEIGHT + TOP_PANEL; y += TILE_SIZE)
        {
            colorSwitch = !colorSwitch;

            for (int x = 0; x < WIDTH; x += TILE_SIZE)
            {
                g.setColor(colorSwitch ? new Color(70, 110, 70) : new Color(60, 95, 60));
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                colorSwitch = !colorSwitch;
            }
        }
    }

    public void drawScore(Graphics g, int WIDTH, int TOP_PANEL, int score, int bestScore, Image appleImage, Image cupImage)
    {
        g.setColor(new Color(25, 70, 35));
        g.fillRect(0, 0, WIDTH, TOP_PANEL);
        g.drawImage(appleImage, 20, 5, 35, 35, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.drawString("x " + score, 65, 30);
        int cupSize = 30;
        int rightX = WIDTH - 140;
        g.drawImage(cupImage, rightX, 5, cupSize, cupSize, null);
        g.setColor(new Color(255, 215, 0));
        g.drawString("x " + bestScore, rightX + 40, 30);
    }

    public void drawGameOver(Graphics g, int WIDTH, int HEIGHT, boolean paused, boolean win, int score, int bestScore, Image appleImage, Image cupImage, Image restartImg, Image homeImg, Rectangle restartButton, Rectangle homeButton, float restartScale, float homeScale)
    {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setColor(new Color(40, 40, 40));
        g.fillRoundRect(WIDTH / 2 - 150, HEIGHT / 2 - 120, 300, 300, 30, 30);
        g.setFont(new Font("Arial", Font.BOLD, 34));
        String text = paused ? "Пауза" : (win ? "ПОБЕДА!" : "Ваш результат");
        g.setColor(paused ? Color.WHITE : (win ? new Color(255, 215, 0) : Color.WHITE));
        FontMetrics fm = g.getFontMetrics();
        int textX = (WIDTH - fm.stringWidth(text)) / 2;
        g.drawString(text, textX, HEIGHT / 2 - 75);
        g.drawImage(appleImage, WIDTH / 2 - 110, HEIGHT / 2 - 45, 35, 35, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("x " + score, WIDTH / 2 - 65, HEIGHT / 2 - 18);
        g.drawImage(cupImage, WIDTH / 2 + 20, HEIGHT / 2 - 45, 35, 35, null);
        g.setColor(new Color(255, 215, 0));
        g.drawString("x " + bestScore, WIDTH / 2 + 60, HEIGHT / 2 - 18);
        drawScaledImage(g, restartImg, restartButton, restartScale);
        drawScaledImage(g, homeImg, homeButton, homeScale);
    }

    public void drawScaledImage(Graphics g, Image img, Rectangle r, float extraScale)
    {
        int imgW = img.getWidth(null);
        int imgH = img.getHeight(null);

        float scale = Math.min((float) r.width / imgW, (float) r.height / imgH);
        scale *= 1.5f;
        scale *= extraScale;
        int w = (int)(imgW * scale * 2.0f);
        int h = (int)(imgH * scale * 1.2f);
        int x = r.x + (r.width - w) / 2;
        int y = r.y + (r.height - h) / 2;
        g.drawImage(img, x, y, w, h, null);
    }
}