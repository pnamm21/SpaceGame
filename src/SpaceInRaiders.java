import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Vector;

public class SpaceInRaiders extends JPanel implements KeyListener, ActionListener, Runnable {

    Player player;
    Image background;
    Timer timer;
    Vector<Alien> aliens = new Vector<>();

    public static void main(String[] args) {
        new SpaceInRaiders();
    }

    public SpaceInRaiders(){
        JFrame main = new JFrame("SpaceX");
        main.setSize(1400,800);

        main.add(this);
        setBackground(Color.BLACK);
        main.addKeyListener(this);
        main.setVisible(true);

        int playerWidth = 50;
        int playerHeight = 50;
        int startX = (getWidth() - playerWidth) / 2;
        int startY = getHeight() - playerHeight - 20; // 20 Pixel Abstand zum unteren Rand

        player = new Player(startX, startY);

        background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("background.png"));

        Thread thread = new Thread(this);
        thread.start();

        // Timer for Aliens beginning
        timer = new Timer(1000,this);
        timer.start();


        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.drawImage(background, 0, 0, getWidth(),getHeight(), this);
        player.draw(graphics,this);

        //Show Aliens
        Enumeration<Alien> enumeration = aliens.elements();
        while(enumeration.hasMoreElements()){
            enumeration.nextElement().draw(graphics,this);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> player.direction = 3;
            case KeyEvent.VK_RIGHT -> player.direction = 4;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.direction = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        aliens.add(new Alien(30, 20));
    }

    @Override
    public void run() {
        while (true) {

            move();
            repaint();

            //Aliens move
            Enumeration<Alien> alienEnumeration = aliens.elements();
            while(alienEnumeration.hasMoreElements()){
                Alien alien = alienEnumeration.nextElement();
                alien.move();
            }

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void move() {
        switch (player.direction) {
            case 3 -> player.x -= player.speed; // LEFT
            case 4 -> player.x += player.speed; // RIGHT
        }

        // Bildschirm-Wrapping
        if (player.x < -player.width) {
            player.x = getWidth();
        } else if (player.x > getWidth()) {
            player.x = -player.width;
        }
    }
}

