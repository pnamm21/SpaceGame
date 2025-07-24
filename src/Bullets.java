import java.awt.*;

public class Bullets extends Space {

    public Bullets(int start_x, int start_y) {
        super(start_x, start_y);
        direction = SPACE;
        speed = 7;
        width = 100;
        height = 60;
        image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/bullet.png"));
    }

    public void move() {
        super.move();
    }
}
