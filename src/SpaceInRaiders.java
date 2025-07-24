import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    Shield shield;

    Image healthIcon, bulletIcon, shieldIcon, superBoxIcon;
    boolean gameOver = false;
    Image gameOverImage;

    // Wave-System
    int currentWave = 1;
    boolean waveInProgress = false;
    int enemiesRemaining = 0;

    public static void main(String[] args) {
        new SpaceInRaiders();
    }

    public SpaceInRaiders() {
        JFrame main = new JFrame("SpaceX");
        main.setSize(1400, 800);
        main.add(this);
        setBackground(Color.BLACK);
        main.addKeyListener(this);
        main.setVisible(true);

        player = new Player(650, 700);
        shield = new Shield(600, 400);

        background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/background.png"));
        healthIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/health.png"));
        bulletIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/bullet.png"));
        shieldIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/shield.png"));
        superBoxIcon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/superBox.png"));
        gameOverImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/gameover.png"));

        timer = new Timer(1000, this);
        timer.start();

        new Thread(this).start();
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        player.draw(g, this);

        for (Alien alien : aliens) alien.draw(g, this);
        for (Bullets bullet : bullets) bullet.draw(g, this);
        for (PowerUp pu : powerUps) pu.draw(g, this);

        for (int i = 0; i < player.health; i++) {
            g.drawImage(healthIcon, 20 + (i * 30), 20, 25, 25, this);
        }

        g.drawImage(bulletIcon, 20, 60, 25, 25, this);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("x " + player.ammo, 55, 80);

        if (player.isShieldActive()) {
            g.drawImage(shieldIcon, 20, 100, 25, 25, this);
            g.setColor(Color.CYAN);
            g.drawString("SHIELD ACTIVE", 55, 120);
        }

        g.setColor(Color.WHITE);
        g.drawString("Score: " + player.score, getWidth() - 150, 30);

        if (gameOver) {
            g.drawImage(gameOverImage, getWidth() / 2 - 200, getHeight() / 2 - 100, 400, 200, this);
        }

        g.drawString("Wave: " + currentWave, getWidth() - 150, 60);
    }

    @Override public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT -> player.direction = 3;
            case KeyEvent.VK_RIGHT -> player.direction = 4;
            case KeyEvent.VK_SPACE -> {
                if (player.ammo > 0) {
                    int bulletX = player.x + player.width / 2 - 5;
                    int bulletY = player.y;
                    bullets.add(new Bullets(bulletX, bulletY));
                    player.ammo--;
                }
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
        if (gameOver) return;

        if (!waveInProgress && aliens.isEmpty()) {
            startNextWave();
        }

        player.update();

        if (shield != null && shield.isActive()) {
            shield.collect(player);
        }

        if (Math.random() < 0.2) {
            int x = (int) (Math.random() * 1300);
            int type = (int) (Math.random() * 4) + 1;
            powerUps.add(new PowerUp(x, 0, type));
        }
    }

    @Override
    public void run() {
        while (true) {
            if (gameOver) {
                repaint();
                break;
            }

            move();
            repaint();

            for (Alien alien : aliens) alien.move();

            for (int i = 0; i < bullets.size(); i++) {
                Bullets bullet = bullets.get(i);
                bullet.move();
                if (bullet.y + bullet.height < 0) {
                    bullets.remove(i);
                    i--;
                }
            }

            for (int i = 0; i < bullets.size(); i++) {
                Bullets bullet = bullets.get(i);
                for (int j = 0; j < aliens.size(); j++) {
                    Alien alien = aliens.get(j);
                    if (bullet.intersects(alien)) {
                        bullets.remove(i);
                        if (alien instanceof AdvancedAlien advAlien) {
                            advAlien.hit();
                            if (advAlien.isDestroyed()) {
                                aliens.remove(j);
                                player.score += advAlien.pointValue;
                                enemiesRemaining--;
                            }
                        } else {
                            aliens.remove(j);
                            player.score += 1;
                            enemiesRemaining--;
                        }
                        if (enemiesRemaining <= 0) waveInProgress = false;
                        break;
                    }
                }
            }

            for (int i = 0; i < aliens.size(); i++) {
                Alien alien = aliens.get(i);
                if (alien.intersects(player)) {
                    aliens.remove(i);
                    i--;
                    if (!player.isShieldActive() && !invincible) {
                        player.health--;
                        if (player.health <= 0) {
                            gameOver = true;
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < powerUps.size(); i++) {
                PowerUp pu = powerUps.get(i);
                pu.move();
                if (pu.intersects(player)) {
                    powerUps.remove(i);
                    switch (pu.type) {
                        case PowerUp.AMMO -> player.ammo += 10;
                        case PowerUp.HEALTH -> player.addHealth(1);
                        case PowerUp.INVINCIBLE -> {
                            invincible = true;
                            invincibleEndTime = System.currentTimeMillis() + 5000;
                            player.activateShield();
                        }
                        case PowerUp.SUPERBOX -> {
                            player.ammo += 15;
                            player.addHealth(2);
                            invincible = true;
                            invincibleEndTime = System.currentTimeMillis() + 5000;
                            player.activateShield();
                        }
                    }
                    i--;
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

        if (player.x < -player.width) player.x = getWidth();
        else if (player.x > getWidth()) player.x = -player.width;
    }

    public void startNextWave() {
        waveInProgress = true;

        if (currentWave % 10 == 0) {
            spawnBoss(currentWave);
        } else {
            switch (currentWave) {
                case 1 -> spawnAliens(5, Alien.class);
                case 2 -> {
                    spawnAliens(4, Alien.class);
                    spawnAliens(2, FastAlien.class);
                }
                case 3 -> {
                    spawnAliens(3, Alien.class);
                    spawnAliens(2, FastAlien.class);
                    spawnAliens(2, TankAlien.class);
                }
                case 4 -> {
                    spawnAliens(3, AdvancedAlien.class);
                    spawnAliens(3, TankAlien.class);
                }
                default -> {
                    spawnAliens(currentWave, Alien.class);
                    spawnAliens(currentWave / 2, FastAlien.class);
                    spawnAliens(currentWave / 3, TankAlien.class);
                }
            }
        }
        currentWave++;
    }

    public void spawnBoss(int waveLevel) {
        int x = getWidth() / 2 - 75;
        int y = 50;
        aliens.add(new BossAlien(x, y, waveLevel));
        enemiesRemaining = 1;
    }

    public void spawnAliens(int count, Class<?> type) {
        for (int i = 0; i < count; i++) {
            int x = (int) (Math.random() * 1300);
            int y = (int) (Math.random() * 200);
            try {
                if (type == Alien.class) aliens.add(new Alien(x, y));
                else if (type == FastAlien.class) aliens.add(new FastAlien(x, y));
                else if (type == TankAlien.class) aliens.add(new TankAlien(x, y));
                else if (type == AdvancedAlien.class) aliens.add(new AdvancedAlien(x, y, 2, 2, "images/alien.png"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        enemiesRemaining += count;
    }
}
