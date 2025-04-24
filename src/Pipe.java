import java.awt.*;
import javax.swing.*;

public class Pipe {
    private int x;
    private int y;
    private int width;
    private int height;
    private Image img;
    private boolean passed = false;
    
    public Pipe(String imagePath, int x, int y, int width, int height) {
        this.img = new ImageIcon(getClass().getResource(imagePath)).getImage();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public Image getImage() {
        return img;
    }
    
    public boolean isPassed() {
        return passed;
    }
    
    public void setPassed(boolean passed) {
        this.passed = passed;
    }
    
    public Rectangle getBounds() {
        // Ajustar a hitbox do cano para ser um pouco menor que a imagem
        // Reduzir 5 pixels de cada lado para melhor corresponder à forma do cano
        int padding = 5;
        return new Rectangle(
            x + padding,
            y,
            width - (padding * 2),
            height
        );
    }
}
