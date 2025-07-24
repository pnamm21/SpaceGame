import java.awt.*;

public class PowerUp extends Space {

    public static final int AMMO = 1;
    public static final int HEALTH = 2;
    public static final int INVINCIBLE = 3;
    public static final int SUPERBOX = 4;  // Neue Box mit kombiniertem Effekt

    public int type;
    Image image;

    public PowerUp(int start_x, int start_y, int type) {
        super(start_x, start_y);
        this.type = type;
        width = 30;
        height = 30;
        speed = 2;
        direction = DOWN;

        switch (type) {
            case AMMO -> image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/bullet.png"));
            case HEALTH -> image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/health.png"));
            case INVINCIBLE -> image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/shield.png"));
            case SUPERBOX -> image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/superBox.png"));
        }
    }

    public void move() {
        super.move();
    }

    public void draw(Graphics g, Component c) {
        g.drawImage(image, x, y, width, height, c);
    }
}
