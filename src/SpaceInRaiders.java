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
    Vector<Bullets> bullets = new Vector<>();
    Vector<PowerUp> powerUps = new Vector<>();
    boolean invincible = false;
    long invincibleEndTime = 0;


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
        int startY = getHeight() - playerHeight - 20;

        player = new Player(startX, startY);

        background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("background.png"));

        Thread thread = new Thread(this);
        thread.start();

        timer = new Timer(1000,this);
        timer.start();

        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        graphics.drawImage(background, 0, 0, getWidth(),getHeight(), this);
        player.draw(graphics,this);

        Enumeration<Alien> enumeration = aliens.elements();
        while(enumeration.hasMoreElements()){
            enumeration.nextElement().draw(graphics,this);
        }

        Enumeration<Bullets> bulletsEnumeration = bullets.elements();
        while(bulletsEnumeration.hasMoreElements()){
            bulletsEnumeration.nextElement().draw(graphics,this);
        }

        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Arial", Font.BOLD, 20));
        graphics.drawString("Score: " + player.score, 20, 30);

        Enumeration<PowerUp> puEnum = powerUps.elements();
        while (puEnum.hasMoreElements()) {
            puEnum.nextElement().draw(graphics, this);
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> player.direction = 3;
            case KeyEvent.VK_RIGHT -> player.direction = 4;
            case KeyEvent.VK_SPACE -> {
                int bulletX = player.x + player.width / 2 - 5;
                int bulletY = player.y;
                bullets.add(new Bullets(bulletX, bulletY));
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            player.direction = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < 3; i++) {
            int x = (int)(Math.random() * 1300);
            int y = (int)(Math.random() * 200);

            double type = Math.random();
            if (type < 0.25) {
                aliens.add(new Alien(x, y));
            } else if (type < 0.5) {
                aliens.add(new AdvancedAlien(x, y, 2, 2, "alien.png"));
            } else if (type < 0.75) {
                aliens.add(new TankAlien(x, y));
            } else {
                aliens.add(new FastAlien(x, y));
            }
        }

        if (Math.random() < 0.2) {
            int x = (int)(Math.random() * 1300);
            int type = (int)(Math.random() * 3) + 1;
            powerUps.add(new PowerUp(x, 0, type));
        }
    }


    @Override
    public void run() {
        while (true) {

            move();
            repaint();

            Enumeration<Alien> alienEnumeration = aliens.elements();
            while (alienEnumeration.hasMoreElements()) {
                Alien alien = alienEnumeration.nextElement();
                alien.move();
            }

            for (int i = 0; i < bullets.size(); i++) {
                Bullets bullet = bullets.get(i);
                bullet.move();
                if (bullet.y + bullet.height < 0) {
                    bullets.remove(i);
                    i--;
                }
            }

            Enumeration<Bullets> b = bullets.elements();
            while (b.hasMoreElements()) {
                Bullets bullet = b.nextElement();
                Enumeration<Alien> a = aliens.elements();
                while (a.hasMoreElements()) {
                    Alien alien = a.nextElement();
                    if (bullet.intersects(alien)) {
                        bullets.remove(bullet);
                        if (alien instanceof AdvancedAlien advAlien) {
                            advAlien.hit();
                            if (advAlien.isDestroyed()) {
                                aliens.remove(alien);
                                player.score += advAlien.pointValue;
                            }
                        } else {
                            aliens.remove(alien);
                            player.score += 1;
                        }
                        break;
                    }
                }
            }

            Enumeration<PowerUp> pEnum = powerUps.elements();
            while (pEnum.hasMoreElements()) {
                PowerUp pu = pEnum.nextElement();
                pu.move();

                if (pu.intersects(player)) {
                    powerUps.remove(pu);
                    switch (pu.type) {
                        case PowerUp.AMMO -> player.ammo += 10;
                        case PowerUp.HEALTH -> player.health += 1;
                        case PowerUp.INVINCIBLE -> {
                            invincible = true;
                            invincibleEndTime = System.currentTimeMillis() + 5000;
                        }
                    }
                }
            }
            if (invincible && System.currentTimeMillis() > invincibleEndTime) {
                invincible = false;
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
            case 3 -> player.x -= player.speed;
            case 4 -> player.x += player.speed;
        }

        if (player.x < -player.width) {
            player.x = getWidth();
        } else if (player.x > getWidth()) {
            player.x = -player.width;
        }
    }
}
