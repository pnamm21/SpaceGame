import java.awt.*;

public abstract class Space extends Rectangle {

    Image image;
    int speed;
    int direction;
    static int UP = 1;
    static int DOWN = 2;
    static int LEFT = 3;
    static int RIGHT = 4;

    public Space(int start_x, int start_y){
        x = start_x;
        y = start_y;
    }

    public void draw(Graphics graphics, Component component){

        graphics.drawImage(image, x, y,width,height, component);
    }

    public void move() {
        if (direction == RIGHT) {
            x += speed;
        } else if (direction == LEFT) {
            x -= speed;
        }
    }

}
