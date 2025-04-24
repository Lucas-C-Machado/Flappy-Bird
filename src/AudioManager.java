import javax.sound.sampled.*;

public class AudioManager {
    private Clip backgroundMusic;
    private Clip scoreSound;
    
    public AudioManager() {
        loadBackgroundMusic();
        loadScoreSound();
    }
    
    private void loadBackgroundMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                getClass().getResource("./background_music.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void loadScoreSound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                getClass().getResource("./score_sound.wav"));
            scoreSound = AudioSystem.getClip();
            scoreSound.open(audioInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }
    
    public void playScoreSound() {
        if (scoreSound != null) {
            scoreSound.setFramePosition(0);
            scoreSound.start();
        }
    }
    
    public void cleanup() {
        if (backgroundMusic != null) {
            backgroundMusic.close();
        }
        if (scoreSound != null) {
            scoreSound.close();
        }
    }
}
