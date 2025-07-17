import java.awt.*;

public class PowerUp extends Space {
    public static final int AMMO = 1;
    public static final int HEALTH = 2;
    public static final int INVINCIBLE = 3;

    int type;
    Image image;

    public PowerUp(int start_x, int start_y, int type) {
        super(start_x, start_y);
        this.type = type;
        width = 30;
        height = 30;
        speed = 2;
        direction = DOWN;

        switch (type) {
            case AMMO -> image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("bullet.png"));
            case HEALTH -> image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("health.png"));
            case INVINCIBLE -> image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("shield.png"));
        }
    }

    public void move() {
        super.move();
    }
}
