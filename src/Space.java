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

}
