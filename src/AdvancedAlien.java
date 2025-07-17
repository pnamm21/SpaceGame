import java.awt.*;

public class AdvancedAlien extends Alien {
    int health;
    int pointValue;

    public AdvancedAlien(int start_x, int start_y, int health, int pointValue, String imagePath) {
        super(start_x, start_y);
        this.health = health;
        this.pointValue = pointValue;
        this.image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(imagePath));
    }

    public void hit() {
        health--;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }
}
