import java.awt.*;
import javax.swing.*;

public class Bird {
    private int x;
    private int y;
    private int width;
    private int height;
    private Image img;
    
    public Bird(int x, int y, int width, int height, String imagePath) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = new ImageIcon(getClass().getResource(imagePath)).getImage();
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
    
    public void setY(int y) {
        this.y = y;
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
    
    public Rectangle getBounds() {
        // Criar uma hitbox menor que a imagem visível
        // Reduzir 25% de cada lado para melhor corresponder à forma do pássaro
        int padding = 10;
        return new Rectangle(
            x + padding,
            y + padding,
            width - (padding * 2),
            height - (padding * 2)
        );
    }
}
