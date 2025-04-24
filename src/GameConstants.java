public class GameConstants {
    // Dimensões da janela
    public static final int LARGURA_BORDA = 360;
    public static final int ALTURA_BORDA = 640;
    
    // Dimensões do pássaro
    public static final int BIRD_WIDTH = 70;
    public static final int BIRD_HEIGHT = 60;
    public static final int BIRD_X = LARGURA_BORDA / 8;
    public static final int BIRD_Y = ALTURA_BORDA / 2;
    
    // Dimensões do cano
    public static final int PIPE_WIDTH = 80;
    public static final int PIPE_HEIGHT = 512;
    public static final int PIPE_GAP = 160;

    // Ajustes de hitbox
    public static final int BIRD_HITBOX_PADDING = 10;
    public static final int PIPE_HITBOX_PADDING = 5;
    
    // Física do jogo
    public static final int GRAVITY = 1;
    public static final int JUMP_FORCE = -10;
    public static final int PIPE_VELOCITY = -4;
    
    // Caminhos para recursos
    public static final String BACKGROUND_IMAGE_PATH = "./background.jpg";
    public static final String BIRD_IMAGE_PATH = "./bird.png";
    public static final String PIPE_IMAGE_PATH = "./obstaculo.png";
    public static final String BACKGROUND_MUSIC_PATH = "./background_music.wav";
    public static final String SCORE_SOUND_PATH = "./score_sound.wav";
}
