import java.awt.*;

public class Shield {
    private int x, y;
    private boolean active;

    public Shield(int x, int y) {
        this.x = x;
        this.y = y;
        this.active = true;
    }

    public void draw(Graphics g) {
        if (active) {
            g.setColor(Color.BLUE);
            g.fillRect(x, y, 20, 20);
        }
    }

    public void collect(Player player) {
        if (active && player.getBounds().intersects(getBounds())) {
            player.activateShield();
            active = false;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20); // Power-up hitbox
    }

    public boolean isActive() {
        return active;
    }
}
