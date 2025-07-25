import java.awt.*;

public class Alien extends Space{


    public Alien(int start_x, int start_y) {
        super(start_x, start_y);
        width = 35;
        height = 35;
        speed = 7;
        direction = Space.RIGHT;
        image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/alien3.png"));
    }

    public void move(){
        super.move();
        if (x > 1320){
            direction = LEFT;
            y += 70;
        }

        if (x < 30){
            direction = RIGHT;
            y += 70;
        }
    }

}
