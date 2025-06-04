import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SpaceInRaiders extends JPanel implements KeyListener, ActionListener, Runnable {

    Player player;

    public static void main(String[] args) {
        new SpaceInRaiders();
    }

    public SpaceInRaiders(){
        JFrame main = new JFrame("SpaceX");
        main.setSize(1400,800);


        //Panel
        main.add(this);
        setBackground(Color.BLACK);

        //Player
        player = new Player(675,700);

        //GameLoop
        Thread thread = new Thread(this);
        thread.start();


        main.setVisible(true);
        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.setColor(Color.BLUE);
        graphics.fillRect(player.x,player.y,player.width,player.height);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void run() {

    }
}
