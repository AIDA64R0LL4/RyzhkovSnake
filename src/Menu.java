import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Objects;

public class Menu extends JPanel {

    Image background;
    Image text;
    Image start;
    Image exit;
    Image stats;
    Image appleImg;
    Image cupImg;
    Image skullImg;
    Image starImg;
    Sound menuMusic = new Sound();

    boolean showStats = false;
    int bestScore = 0;
    int deaths = 0;
    int wins = 0;
    int totalApples = 0;
    float logoScale = 1.0f;
    float animationTime = 0;
    float startScale = 1.0f;
    float statsScale = 1.0f;
    float exitScale = 1.0f;
    boolean hoverStart = false;
    boolean hoverStats = false;
    boolean hoverExit = false;

    Timer animationTimer;

    Rectangle startBT = new Rectangle(180, 220, 200, 70);
    Rectangle statsBT = new Rectangle(180, 320, 200, 70);
    Rectangle exitBT  = new Rectangle(180, 420, 200, 70);
    Rectangle closeStatsBT = new Rectangle(420, 105, 30, 30);

    public Menu(JFrame frame)
    {
        setPreferredSize(new Dimension(560, 560));
        setBackground(Color.BLACK);

        background = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/background.png"))).getImage();
        text = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/snake.png"))).getImage();
        start = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/start.png"))).getImage();
        stats = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/stats.png"))).getImage();
        exit = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/exit.png"))).getImage();
        appleImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/apple.png"))).getImage();
        cupImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/cup.png"))).getImage();
        skullImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/skull.png"))).getImage();
        starImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/star.png"))).getImage();

        LoadStats();
        menuMusic.play("/sounds/gwyn.wav");

        //Таймер анимаций
        animationTimer = new Timer(16, e ->
        {
            animationTime += 0.05f;

            //Анимация для текста змейка
            logoScale = 1.0f + (float)Math.sin(animationTime) * 0.05f;

            //Увеличение кнопок когда на них курсор
            startScale += ((hoverStart ? 1.12f : 1.0f) - startScale) * 0.12f;
            statsScale += ((hoverStats ? 1.12f : 1.0f) - statsScale) * 0.12f;
            exitScale += ((hoverExit ? 1.12f : 1.0f) - exitScale) * 0.12f;

            repaint();
        });

        animationTimer.start();

        addMouseListener(new MouseAdapter()
        {

            public void mouseClicked(MouseEvent e)
            {
                if (startBT.contains(e.getPoint()))
                {
                    menuMusic.stop();
                    animationTimer.stop();
                    GameZone game = new GameZone();
                    frame.getContentPane().removeAll();
                    frame.add(game);
                    frame.revalidate();
                    frame.repaint();
                    game.requestFocusInWindow();
                }

                if (statsBT.contains(e.getPoint()))
                {
                    showStats = true;
                    repaint();
                }

                if (showStats && closeStatsBT.contains(e.getPoint()))
                {
                    showStats = false;
                    repaint();
                }

                if (exitBT.contains(e.getPoint()))
                {
                    menuMusic.stop();
                    animationTimer.stop();
                    System.exit(0);
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseMoved(MouseEvent e)
            {
                hoverStart = startBT.contains(e.getPoint());
                hoverStats = statsBT.contains(e.getPoint());
                hoverExit = exitBT.contains(e.getPoint());
            }
        });
    }

    private void LoadStats() {
        try
        {
            File file = new File("stats.txt");

            if (!file.exists())
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write("0\n0\n0\n0");
                writer.close();
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            bestScore = Integer.parseInt(reader.readLine());
            deaths = Integer.parseInt(reader.readLine());
            wins = Integer.parseInt(reader.readLine());
            totalApples = Integer.parseInt(reader.readLine());
            reader.close();
        }
        catch (Exception e)
        {
            bestScore = 0;
            deaths = 0;
            wins = 0;
            totalApples = 0;
            e.printStackTrace();
        }
    } //Загрузка статы
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);

        //Анимация лого змейки
        DrawAnimatedLogo(g, text, new Rectangle(80, 40, 400, 120));

        //Анимация кнопок
        DrawButton(g, start, startBT, startScale);
        DrawButton(g, stats, statsBT, statsScale);
        DrawButton(g, exit, exitBT, exitScale);if (showStats) {Static(g);}
    } //Вывод кнопок и задника
    public void Static(Graphics g) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(40, 40, 40));
        g.fillRoundRect(90, 70, 380, 420, 30, 30);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        String title = "Статистика";
        FontMetrics fm = g.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(title)) / 2;
        g.drawString(title, textX, 130);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.drawImage(appleImg, 120, 175, 36, 36, null);
        g.drawImage(cupImg, 120, 245, 36, 36, null);
        g.drawImage(skullImg, 120, 315, 36, 36, null);
        g.drawImage(starImg, 120, 385, 36, 36, null);
        g.drawString("Съедено яблок: " + totalApples, 175, 205);
        g.drawString("Рекорд: " + bestScore, 175, 275);
        g.drawString("Смертей: " + deaths, 175, 345);
        g.drawString("Побед: " + wins, 175, 415);
        g.setColor(new Color(180, 50, 50));
        g.fillRoundRect(closeStatsBT.x, closeStatsBT.y, closeStatsBT.width, closeStatsBT.height, 10, 10);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("X", closeStatsBT.x + 8, closeStatsBT.y + 22);
    } //Вывод статистики
    private void DrawAnimatedLogo(Graphics g, Image img, Rectangle r) {
        int imgW = img.getWidth(null);
        int imgH = img.getHeight(null);
        float scale = Math.min((float) r.width / imgW, (float) r.height / imgH);
        scale *= 1.5f;
        scale *= logoScale;
        int w = (int)(imgW * scale * 2.0f);
        int h = (int)(imgH * scale * 1.2f);
        int x = r.x + (r.width - w) / 2;
        int y = r.y + (r.height - h) / 2;
        g.drawImage(img, x, y, w, h, null);
    } //Анимация лого змейки
    private void DrawButton(Graphics g, Image img, Rectangle r, float extraScale) {
        int imgW = img.getWidth(null);
        int imgH = img.getHeight(null);
        float scale = Math.min((float) r.width / imgW, (float) r.height / imgH);
        scale *= 1.5f;scale *= extraScale;
        int w = (int)(imgW * scale * 2.0f);
        int h = (int)(imgH * scale * 1.2f);
        int x = r.x + (r.width - w) / 2;
        int y = r.y + (r.height - h) / 2;
        g.drawImage(img, x, y, w, h, null);
    } //Анимация кнопок
}