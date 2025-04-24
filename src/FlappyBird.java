import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.sound.sampled.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    int LarguraBorda = 360;
    int AlturaBorda = 640;

    Image backgroundImage;
    Image birdImage;
    Image pipeImage;

    int birdX = LarguraBorda / 8;
    int birdY = AlturaBorda / 2;
    int birdWidht = 70;
    int birdHeight = 60;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidht;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    int PipeX = LarguraBorda;
    int PipeWidth = 80;
    int PipeHeight = 512;

    class Pipe {
        int x = PipeX;
        int y;
        int width = PipeWidth;
        int height = PipeHeight;
        Image img;
        boolean Passed = false;

        Pipe(Image img, int x, int y, int width, int height) {
            this.img = img;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    Bird bird;
    int VelocityX = -4;
    int VelocityY = 0;
    int Gravity = 1;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    Timer PlacePipesTimer;

    boolean gameStarted = false;
    boolean gameOver = false;

    int score = 0; // Contador de pontos

    Clip backgroundMusic; // Música de fundo
    Clip scoreSound; // Som do score

    FlappyBird() {
        setPreferredSize(new Dimension(LarguraBorda, AlturaBorda));
        setFocusable(true);
        addKeyListener(this);

        backgroundImage = new ImageIcon(getClass().getResource("./background.jpg")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./bird.png")).getImage();
        pipeImage = new ImageIcon(getClass().getResource("./obstaculo.png")).getImage();

        bird = new Bird(birdImage);
        pipes = new ArrayList<>();

        PlacePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameStarted && !gameOver) {
                    PlacePipes();
                }
            }
        });

        PlacePipesTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();

        // Tocar música de fundo
        playBackgroundMusic();

        // Carregar o som de pontuação
        loadScoreSound();
    }

    public void playBackgroundMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("./background_music.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Reproduzir a música em loop
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadScoreSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("./score_sound.wav"));
            scoreSound = AudioSystem.getClip();
            scoreSound.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playScoreSound() {
        if (scoreSound != null) {
            scoreSound.setFramePosition(0); // Reseta o som para começar do início
            scoreSound.start(); // Toca o som
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void PlacePipes() {
        int gap = 160;
        int pipeY = -random.nextInt(200);
        int tipo = random.nextInt(3);

        if (tipo == 0 || tipo == 2) {
            Pipe topPipe = new Pipe(pipeImage, LarguraBorda, pipeY, PipeWidth, PipeHeight);
            pipes.add(topPipe);
        }

        if (tipo == 1 || tipo == 2) {
            Pipe bottomPipe = new Pipe(pipeImage, LarguraBorda, pipeY + PipeHeight + gap, PipeWidth, PipeHeight);
            pipes.add(bottomPipe);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, LarguraBorda, AlturaBorda, null);
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        for (Pipe pipe : pipes) {
            if (pipe.y < 0) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(pipe.img, pipe.x + pipe.width, pipe.y + pipe.height, -pipe.width, -pipe.height, null);
            } else {
                g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
            }
        }

        if (!gameStarted && !gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            String[] linhas = { "PRESS SPACE", "FOR START" };
            FontMetrics metrics = g.getFontMetrics();
            int totalAltura = linhas.length * metrics.getHeight();
            int yInicial = (AlturaBorda - totalAltura) / 2;

            for (int i = 0; i < linhas.length; i++) {
                String linha = linhas[i];
                int x = (LarguraBorda - metrics.stringWidth(linha)) / 2;
                int y = yInicial + i * metrics.getHeight();
                g.drawString(linha, x, y);
            }
        }

        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.setColor(Color.RED);
            String msg = "GAME OVER";
            FontMetrics metrics = g.getFontMetrics();
            int x = (LarguraBorda - metrics.stringWidth(msg)) / 2;
            int y = AlturaBorda / 2 - 40;
            g.drawString(msg, x, y);

            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            String[] linhas = { "PRESS R", "FOR RESTART" };
            FontMetrics fm = g.getFontMetrics();
            int totalAltura = linhas.length * fm.getHeight();
            int yInicial = AlturaBorda / 2 + 10;

            for (int i = 0; i < linhas.length; i++) {
                String linha = linhas[i];
                int linhaX = (LarguraBorda - fm.stringWidth(linha)) / 2;
                int linhaY = yInicial + i * fm.getHeight();
                g.drawString(linha, linhaX, linhaY);
            }
        }

        if (gameStarted) {
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.setColor(Color.WHITE);
            String scoreText = "Score: " + score;
            g.drawString(scoreText, 10, 30); // Exibe o score no canto superior esquerdo
        }
    }

    public void move() {
        VelocityY += Gravity;
        bird.y += VelocityY;
        bird.y = Math.max(bird.y, 0);

        for (Pipe pipe : pipes) {
            pipe.x += VelocityX;

            Rectangle birdRect = new Rectangle(bird.x, bird.y, bird.width, bird.height);
            Rectangle pipeRect = new Rectangle(pipe.x, pipe.y, pipe.width, pipe.height);

            if (birdRect.intersects(pipeRect)) {
                gameOver = true;
                gameLoop.stop();
                PlacePipesTimer.stop();
            }

            if (pipe.x + pipe.width < bird.x && !pipe.Passed) {
                pipe.Passed = true;
                score++; // Incrementa o score quando o pássaro passa pelo cano
                playScoreSound(); // Toca o som de pontuação
            }
        }

        if (bird.y + bird.height >= AlturaBorda || bird.y <= 0) {
            gameOver = true;
            gameLoop.stop();
            PlacePipesTimer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gameOver) {
            move();
        }
        repaint();
    }

    public void reset() {
        bird.y = AlturaBorda / 2;
        VelocityY = 0;
        pipes.clear();
        score = 0; // Reseta o score
        gameStarted = false;
        gameOver = false;
        gameLoop.start();
        PlacePipesTimer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameStarted && !gameOver) {
                gameStarted = true;
            } else if (!gameOver) {
                VelocityY = -10;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            reset(); // Reinicia o jogo
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBird game = new FlappyBird();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.requestFocusInWindow();
    }
}
