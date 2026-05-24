import javax.sound.sampled.LineEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Objects;

public class GameZone extends JPanel implements ActionListener, KeyListener {

    static final int ROWS = 16;
    static final int COLS = 16;
    final int TILE_SIZE = 35;
    static final int TOP_PANEL = 40;
    final int GAME_HEIGHT = ROWS * TILE_SIZE;
    final int WIDTH = COLS * TILE_SIZE;
    final int HEIGHT = GAME_HEIGHT + TOP_PANEL;
    int score = 0;
    int bestScore = 0;
    int deaths = 0;
    int wins = 0;
    int totalApples = 0;
    int speedsnake = 180;
    long lastMoveTime = 0;
    boolean gameOver = false;
    boolean gameStarted = false;
    boolean paused = false;
    boolean win = false;
    boolean helpImage = true;
    boolean waitingForStart = false;
    long splashStartTime = System.currentTimeMillis();
    int splashDuration = 3000;
    float restartScale = 1.0f;
    float homeScale = 1.0f;
    boolean restartHover = false;
    boolean homeHover = false;

    Rectangle restartButton = new Rectangle(WIDTH / 2 - 100, HEIGHT / 2 + 25, 200, 50);
    Rectangle homeButton = new Rectangle(WIDTH / 2 - 100, HEIGHT / 2 + 110, 200, 50);
    Timer timer;
    Snake snake;
    Apple apple;
    Image helpingImage;
    Image cupImage;
    Image restartImg;
    Image homeImg;
    Sound sound = new Sound();
    Sound music = new Sound();

    String[] tracks = {"/sounds/song1.wav", "/sounds/song2.wav", "/sounds/song3.wav", "/sounds/song4.wav", "/sounds/song5.wav", "/sounds/song6.wav", "/sounds/song7.wav", "/sounds/song8.wav", "/sounds/song9.wav"};

    int currentTrack = (int)(Math.random() * tracks.length);

    public GameZone() {

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        snake = new Snake(TILE_SIZE);
        apple = new Apple(TILE_SIZE);
        apple.spawn(snake);

        cupImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/cup.png"))).getImage();
        restartImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/res.png"))).getImage();
        homeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/exit.png"))).getImage();
        helpingImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/helpingimage.png"))).getImage();
        timer = new Timer(16, this);
        timer.start();

        PlayTrack();
        ScoreLoad();

        addMouseListener(new MouseAdapter()
        {

            public void mouseClicked(MouseEvent e)
            {

                if (gameOver || paused)
                {

                    if (restartButton.contains(e.getPoint()))
                    {
                        snake.reset();
                        score = 0;
                        apple.spawn(snake);
                        gameOver = false;
                        paused = false;
                        win = false;
                        HelpImage();
                    }

                    if (homeButton.contains(e.getPoint()))
                    {
                        music.stop();
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(GameZone.this);
                        frame.getContentPane().removeAll();
                        frame.add(new Menu(frame));
                        frame.revalidate();
                        frame.repaint();
                    }
                }
            }
        });

        addMouseMotionListener(
                new MouseMotionAdapter() {
                    public void mouseMoved(MouseEvent e) {
                        restartHover = restartButton.contains(e.getPoint());
                        homeHover = homeButton.contains(e.getPoint());
                    }
                });
    }

    private void PlayTrack() {

        music.stop();
        music.play(tracks[currentTrack]);

        music.clip.addLineListener(event ->
        {
            if (event.getType() == LineEvent.Type.STOP) {if (music.clip.getFramePosition() >= music.clip.getFrameLength()) {NextTrack();}}
        });
    }

    private void NextTrack() {

        currentTrack++;
        if (currentTrack >= tracks.length) {currentTrack = 0;}

        PlayTrack();
    }

    private void PreviousTrack() {

        currentTrack--;
        if (currentTrack < 0) {currentTrack = tracks.length - 1;}

        PlayTrack();
    }

    private void HelpImage() {

        helpImage = true;
        waitingForStart = false;
        gameStarted = false;
        splashStartTime = System.currentTimeMillis();
    }

    private void AnimateButtons()
    {
        float speed = 0.12f;
        restartScale += ((restartHover ? 1.12f : 1.0f) - restartScale) * speed;
        homeScale += ((homeHover ? 1.12f : 1.0f) - homeScale) * speed;
    }

    public void actionPerformed(ActionEvent e) {

        AnimateButtons();

        if (helpImage) {

            if (System.currentTimeMillis() - splashStartTime >= splashDuration)
            {
                helpImage = false;
                waitingForStart = true;
            }

            repaint();
            return;
        }

        if (!gameStarted || gameOver || paused)
        {

            repaint();
            return;
        }

        if (System.currentTimeMillis() - lastMoveTime >= speedsnake) {

            snake.move();
            lastMoveTime = System.currentTimeMillis();
        }

        if (snake.x.getFirst() == apple.x && snake.y.getFirst() == apple.y)
        {

            snake.grow();
            score++;
            totalApples++;
            sound.play("/sounds/eat.wav");

            if (snake.x.size() >= ROWS * COLS)
            {
                win = true;
                gameOver = true;
                wins++;
                ScoreSave();

            }
            else
            {
                apple.spawn(snake);
            }
        }

        if (snake.Collision(WIDTH, GAME_HEIGHT))
        {

            gameOver = true;
            deaths++;

            if (score > bestScore) {bestScore = score;}

            ScoreSave();
        }

        repaint();
    }

    public void keyPressed(KeyEvent e)
    {

        if (helpImage) return;

        if (waitingForStart)
        {
            waitingForStart = false;
            gameStarted = true;
        }

        if (!gameStarted || gameOver)
            return;

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            paused = !paused;
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_P)
        {
            NextTrack();
        }

        if (e.getKeyCode() == KeyEvent.VK_O)
        {
            PreviousTrack();
        }

        switch (e.getKeyCode())
        {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                if (snake.direction != 'D') {snake.direction = 'U';}break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                if (snake.direction != 'U') {snake.direction = 'D';}break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                if (snake.direction != 'R') {snake.direction = 'L';}break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                if (snake.direction != 'L') {snake.direction = 'R';}break;
        }
    }

    public void Zone(Graphics g) {

        boolean colorSwitch = false;

        for (int y = TOP_PANEL;
             y < GAME_HEIGHT + TOP_PANEL;
             y += TILE_SIZE) {

            colorSwitch = !colorSwitch;

            for (int x = 0;
                 x < WIDTH;
                 x += TILE_SIZE)
            {
                g.setColor(colorSwitch ? new Color(70, 110, 70) : new Color(60, 95, 60));
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                colorSwitch = !colorSwitch;
            }
        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        Score(g2);
        Zone(g2);
        apple.draw(g2);
        snake.draw(g2);

        if (gameOver || paused) {GameOverAndStats(g2);}

        if (helpImage)
        {

            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, WIDTH, HEIGHT);

            int w = 500;
            int h = 380;

            g2.drawImage(helpingImage, WIDTH / 2 - w / 2, HEIGHT / 2 - h / 2, w, h, null);
        }
    }

    public void Score(Graphics g) {

        g.setColor(new Color(25, 70, 35));
        g.fillRect(0, 0, WIDTH, TOP_PANEL);
        g.drawImage(apple.appleImage, 20, 5, 35, 35, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 26));
        g.drawString("x " + score, 65, 30);
        int cupSize = 30;
        int rightX = WIDTH - 140;
        g.drawImage(cupImage, rightX, 5, cupSize, cupSize, null);
        g.setColor(new Color(255, 215, 0));
        g.drawString("x " + bestScore, rightX + 40, 30);
    }

    public void GameOverAndStats(Graphics g) {

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
        g.drawImage(apple.appleImage, WIDTH / 2 - 110, HEIGHT / 2 - 45, 35, 35, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 28));
        g.drawString("x " + score, WIDTH / 2 - 65, HEIGHT / 2 - 18);
        g.drawImage(cupImage, WIDTH / 2 + 20, HEIGHT / 2 - 45, 35, 35, null);
        g.setColor(new Color(255, 215, 0));
        g.drawString("x " + bestScore, WIDTH / 2 + 60, HEIGHT / 2 - 18);
        WidthHeightImage(g, restartImg, restartButton, restartScale);
        WidthHeightImage(g, homeImg, homeButton, homeScale);
    }

    private void WidthHeightImage(Graphics g, Image img, Rectangle r, float extraScale)
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

    private void ScoreLoad() {

        try {

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
    }

    private void ScoreSave() {

        try
        {

            BufferedWriter writer = new BufferedWriter(new FileWriter("stats.txt"));
            writer.write(bestScore + "\n");
            writer.write(deaths + "\n");
            writer.write(wins + "\n");
            writer.write(totalApples + "\n");
            writer.close();

        } catch (Exception e) {e.printStackTrace();}
    }

    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}