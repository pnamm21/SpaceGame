import javax.swing.*;
import java.awt.*;

public class Player extends Space{

    int ammo;
    int score;
    int health;


    public Player(int start_x, int start_y) {
        super(start_x, start_y);
        width = 50;
        height = 50;
        speed = 7;
        direction = 0;
        ammo = 30;
        score = 0;
        health = 5;
        image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("spaceShip.png"));
    }


}
