import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class GameZone extends JPanel implements ActionListener, KeyListener {

    static final int ROWS = 16;
    static final int COLS = 16;
    final int SIZE = 35;
    static final int PANEL = 40;
    final int GHEIGHT = ROWS * SIZE;
    final int WIDTH = COLS * SIZE;
    final int HEIGHT = GHEIGHT + PANEL;
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
    UI UI = new UI();
    SaveLoadStats Stats = new SaveLoadStats();
    AnimationButtons Animation = new AnimationButtons();


    public GameZone() {

        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        snake = new Snake(SIZE);
        apple = new Apple(SIZE);
        apple.spawn(snake);
        cupImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/cup.png"))).getImage();
        restartImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/res.png"))).getImage();
        homeImg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/exit.png"))).getImage();
        helpingImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/image/helpingimage.png"))).getImage();
        timer = new Timer(16, this);
        timer.start();
        music.playTrack();
        Stats.load();
        bestScore = Stats.bestScore;
        deaths = Stats.deaths;
        wins = Stats.wins;
        totalApples = Stats.totalApples;

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
        addMouseMotionListener(new MouseMotionAdapter() {public void mouseMoved(MouseEvent e) {restartHover = restartButton.contains(e.getPoint());homeHover = homeButton.contains(e.getPoint());}});
    }


    private void HelpImage() {

        helpImage = true;
        waitingForStart = false;
        gameStarted = false;
        splashStartTime = System.currentTimeMillis();
    }


    public void actionPerformed(ActionEvent e) {

        Animation.animate(restartHover, homeHover);
        restartScale = Animation.restartScale;
        homeScale = Animation.homeScale;

        if (helpImage) {

            if (System.currentTimeMillis() - splashStartTime >= splashDuration) {helpImage = false;waitingForStart = true;}
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
                Stats.bestScore = bestScore;
                Stats.deaths = deaths;
                Stats.wins = wins;
                Stats.totalApples = totalApples;
                Stats.save();
            } else {apple.spawn(snake);}
        }

        if (snake.Collision(WIDTH, GHEIGHT))
        {
            gameOver = true;
            deaths++;
            if (score > bestScore) {bestScore = score;}
            Stats.bestScore = bestScore;
            Stats.deaths = deaths;
            Stats.wins = wins;
            Stats.totalApples = totalApples;
            Stats.save();
        }
        repaint();
    }

    public void keyPressed(KeyEvent e)
    {
        if (helpImage) return;
        if (waitingForStart) {waitingForStart = false;gameStarted = true;}
        if (!gameStarted || gameOver) return;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {paused = !paused;return;}
        if (e.getKeyCode() == KeyEvent.VK_P) {music.nextTrack();}
        if (e.getKeyCode() == KeyEvent.VK_O) {music.previousTrack();}
        snake.Binds(e.getKeyCode());
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        UI.drawScore(g2, WIDTH, PANEL, score, bestScore, apple.appleImage, cupImage);
        UI.drawZone(g2, WIDTH, GHEIGHT, PANEL, SIZE);
        apple.draw(g2);
        snake.draw(g2);

        if (gameOver || paused)
        {
            UI.drawGameOver(g2, WIDTH, HEIGHT, paused, win, score, bestScore, apple.appleImage, cupImage, restartImg, homeImg, restartButton, homeButton, restartScale, homeScale);
        }

        if (helpImage)
        {

            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, WIDTH, HEIGHT);

            int w = 500;
            int h = 380;

            g2.drawImage(helpingImage, WIDTH / 2 - w / 2, HEIGHT / 2 - h / 2, w, h, null);
        }
    }


    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
}