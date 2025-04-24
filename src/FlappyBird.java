import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    private Bird bird;
    private ArrayList<Pipe> pipes;
    private AudioManager audioManager;
    private Random random;
    
    private Timer gameLoop;
    private Timer placePipesTimer;
    
    private int velocityY;
    private int score;
    
    private boolean gameStarted;
    private boolean gameOver;
    
    private Image backgroundImage;

    public FlappyBird() {
        setPreferredSize(new Dimension(GameConstants.LARGURA_BORDA, GameConstants.ALTURA_BORDA));
        setFocusable(true);
        addKeyListener(this);
        
        // Inicializar recursos
        backgroundImage = new ImageIcon(getClass().getResource(GameConstants.BACKGROUND_IMAGE_PATH)).getImage();
        
        // Inicializar objetos do jogo
        bird = new Bird(
            GameConstants.BIRD_X, 
            GameConstants.BIRD_Y, 
            GameConstants.BIRD_WIDTH, 
            GameConstants.BIRD_HEIGHT, 
            GameConstants.BIRD_IMAGE_PATH
        );
        
        pipes = new ArrayList<>();
        random = new Random();
        audioManager = new AudioManager();
        
        // Inicializar variáveis do jogo
        velocityY = 0;
        score = 0;
        gameStarted = false;
        gameOver = false;
        
        // Configurar timers
        placePipesTimer = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (gameStarted && !gameOver) {
                    placePipes();
                }
            }
        });
        
        gameLoop = new Timer(1000 / 60, this);
        
        // Iniciar timers
        placePipesTimer.start();
        gameLoop.start();
        
        // Iniciar música de fundo
        audioManager.playBackgroundMusic();
    }

    private void placePipes() {
        int pipeY = -random.nextInt(200);
        int tipo = random.nextInt(3);

        if (tipo == 0 || tipo == 2) {
            Pipe topPipe = new Pipe(
                GameConstants.PIPE_IMAGE_PATH,
                GameConstants.LARGURA_BORDA,
                pipeY,
                GameConstants.PIPE_WIDTH,
                GameConstants.PIPE_HEIGHT
            );
            pipes.add(topPipe);
        }

        if (tipo == 1 || tipo == 2) {
            Pipe bottomPipe = new Pipe(
                GameConstants.PIPE_IMAGE_PATH,
                GameConstants.LARGURA_BORDA,
                pipeY + GameConstants.PIPE_HEIGHT + GameConstants.PIPE_GAP,
                GameConstants.PIPE_WIDTH,
                GameConstants.PIPE_HEIGHT
            );
            pipes.add(bottomPipe);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        // Desenhar fundo
        g.drawImage(backgroundImage, 0, 0, GameConstants.LARGURA_BORDA, GameConstants.ALTURA_BORDA, null);
        
        // Desenhar pássaro
        g.drawImage(bird.getImage(), bird.getX(), bird.getY(), bird.getWidth(), bird.getHeight(), null);

        // Desenhar canos
        for (Pipe pipe : pipes) {
            if (pipe.getY() < 0) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(
                    pipe.getImage(), 
                    pipe.getX() + pipe.getWidth(), 
                    pipe.getY() + pipe.getHeight(), 
                    -pipe.getWidth(), 
                    -pipe.getHeight(), 
                    null
                );
            } else {
                g.drawImage(
                    pipe.getImage(), 
                    pipe.getX(), 
                    pipe.getY(), 
                    pipe.getWidth(), 
                    pipe.getHeight(), 
                    null
                );
            }
        }

        // Desenhar mensagens de início
        if (!gameStarted && !gameOver) {
            drawStartMessage(g);
        }

        // Desenhar mensagem de game over
        if (gameOver) {
            drawGameOverMessage(g);
        }

        // Desenhar pontuação
        if (gameStarted) {
            drawScore(g);
        }
        
        // Descomente a linha abaixo para visualizar as hitboxes durante o desenvolvimento
        // drawHitboxes(g);
    }
    
    private void drawHitboxes(Graphics g) {
        // Desenhar a hitbox do pássaro
        Rectangle birdBounds = bird.getBounds();
        g.setColor(Color.RED);
        g.drawRect(birdBounds.x, birdBounds.y, birdBounds.width, birdBounds.height);
        
        // Desenhar as hitboxes dos canos
        g.setColor(Color.GREEN);
        for (Pipe pipe : pipes) {
            Rectangle pipeBounds = pipe.getBounds();
            g.drawRect(pipeBounds.x, pipeBounds.y, pipeBounds.width, pipeBounds.height);
        }
    }

    private void drawStartMessage(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.WHITE);
        String[] linhas = { "PRESS SPACE", "FOR START" };
        FontMetrics metrics = g.getFontMetrics();
        int totalAltura = linhas.length * metrics.getHeight();
        int yInicial = (GameConstants.ALTURA_BORDA - totalAltura) / 2;

        for (int i = 0; i < linhas.length; i++) {
            String linha = linhas[i];
            int x = (GameConstants.LARGURA_BORDA - metrics.stringWidth(linha)) / 2;
            int y = yInicial + i * metrics.getHeight();
            g.drawString(linha, x, y);
        }
    }

    private void drawGameOverMessage(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.setColor(Color.RED);
        String msg = "GAME OVER";
        FontMetrics metrics = g.getFontMetrics();
        int x = (GameConstants.LARGURA_BORDA - metrics.stringWidth(msg)) / 2;
        int y = GameConstants.ALTURA_BORDA / 2 - 40;
        g.drawString(msg, x, y);

        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.WHITE);
        String[] linhas = { "PRESS R", "FOR RESTART" };
        FontMetrics fm = g.getFontMetrics();
        int totalAltura = linhas.length * fm.getHeight();
        int yInicial = GameConstants.ALTURA_BORDA / 2 + 10;

        for (int i = 0; i < linhas.length; i++) {
            String linha = linhas[i];
            int linhaX = (GameConstants.LARGURA_BORDA - fm.stringWidth(linha)) / 2;
            int linhaY = yInicial + i * fm.getHeight();
            g.drawString(linha, linhaX, linhaY);
        }
    }

    private void drawScore(Graphics g) {
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.setColor(Color.WHITE);
        String scoreText = "Score: " + score;
        g.drawString(scoreText, 10, 30);
    }

    private void move() {
        // Atualizar posição do pássaro
        velocityY += GameConstants.GRAVITY;
        bird.setY(bird.getY() + velocityY);
        bird.setY(Math.max(bird.getY(), 0));

        // Verificar colisões e atualizar canos
        Rectangle birdBounds = bird.getBounds();
        
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setX(pipe.getX() + GameConstants.PIPE_VELOCITY);

            // Verificar colisão com hitboxes ajustadas
            Rectangle pipeBounds = pipe.getBounds();
            if (birdBounds.intersects(pipeBounds)) {
                gameOver = true;
                gameLoop.stop();
                placePipesTimer.stop();
            }

            // Verificar se passou pelo cano
            if (pipe.getX() + pipe.getWidth() < bird.getX() && !pipe.isPassed()) {
                pipe.setPassed(true);
                score++;
                audioManager.playScoreSound();
            }
            
            // Remover canos que saíram da tela
            if (pipe.getX() + pipe.getWidth() < 0) {
                pipes.remove(i);
                i--;
            }
        }

        // Verificar colisão com o chão ou teto
        if (bird.getY() + bird.getHeight() >= GameConstants.ALTURA_BORDA || bird.getY() <= 0) {
            gameOver = true;
            gameLoop.stop();
            placePipesTimer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gameOver) {
            move();
        }
        repaint();
    }

    private void reset() {
        bird.setY(GameConstants.ALTURA_BORDA / 2);
        velocityY = 0;
        pipes.clear();
        score = 0;
        gameStarted = false;
        gameOver = false;
        gameLoop.start();
        placePipesTimer.start();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!gameStarted && !gameOver) {
                gameStarted = true;
            } else if (!gameOver) {
                velocityY = GameConstants.JUMP_FORCE;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            reset();
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
