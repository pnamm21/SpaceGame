import java.awt.*;

public class BossAlien extends Alien {
    int health;

    public BossAlien(int x, int y, int waveLevel) {
        super(x, y);
        this.image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/boss.png"));
        this.width = 150;
        this.height = 150;
        this.speed = 1 + (waveLevel / 10); // Wird schneller
        this.health = 5 + (waveLevel / 2); // Wird härter
    }

    public void hit() {
        health--;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

    @Override
    public void move() {
        y += speed;
    }
}
