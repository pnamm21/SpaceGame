import java.awt.*;

public abstract class Space extends Rectangle {

    Image image;
    int speed;
    int direction;
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int SPACE = 5;

    public Space(int start_x, int start_y){
        x = start_x;
        y = start_y;
    }

    public void draw(Graphics graphics, Component component){

        graphics.drawImage(image, x, y,width,height, component);
    }

    public void move() {
        switch (direction) {
            case RIGHT -> x += speed;
            case LEFT -> x -= speed;
            case SPACE -> y -= speed;
            case DOWN -> y += speed;
        }
    }

}
