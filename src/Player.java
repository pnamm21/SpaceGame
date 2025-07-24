import java.awt.*;

public class Player extends Space {

    int ammo;
    int score;
    int health;

    private boolean isShieldActive;
    private long shieldActivatedTime;
    private static final long SHIELD_DURATION = 5000;
    public static final int MAX_HEALTH = 10;


    public Player(int start_x, int start_y) {
        super(start_x, start_y);
        width = 50;
        height = 50;
        speed = 7;
        direction = 0;
        ammo = 30;
        score = 0;
        health = 10;
        isShieldActive = false;

        image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/spaceShip.png"));
    }

    public void activateShield() {
        isShieldActive = true;
        shieldActivatedTime = System.currentTimeMillis();
    }

    public void update() {
        if (isShieldActive && (System.currentTimeMillis() - shieldActivatedTime > SHIELD_DURATION)) {
            isShieldActive = false;
        }
    }

    public boolean isShieldActive() {
        return isShieldActive;
    }

    public void draw(Graphics g, Component c) {
        super.draw(g, c);

        if (isShieldActive) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(new Color(0, 0, 255, 100));
            g2d.fillOval(x - 5, y - 5, width + 10, height + 10);
        }
    }

    public void addHealth(int amount) {
        health = Math.min(health + amount, MAX_HEALTH);
    }


}
