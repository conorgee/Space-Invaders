import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.util.ArrayList;
public class InvadersApplication extends JFrame
implements Runnable, KeyListener {
    // member data
    private static String workingDirectory;
    private static boolean isInitialised =
        false;
    private static final Dimension WindowSize = new Dimension(800, 600);
    private BufferStrategy strategy;
    private Graphics offscreenGraphics;
    public static final int NUMALIENS = 30;
    private Alien[] AliensArray = new Alien[NUMALIENS];
    private Spaceship PlayerShip;
    private Image bulletImage;
    private ArrayList < PlayerBullet > bullets = new ArrayList < PlayerBullet > ();
    private int gamespeed = 2;
    private boolean gameState = false;
    // constructor
    public InvadersApplication() {
        //Display the window, centred on the screen
        Dimension screensize =
            java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width / 2 - WindowSize.width / 2;
        int y = screensize.height / 2 - WindowSize.height / 2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);
        this.setTitle("Space Invaders! .. (getting there)");

        // load image from disk
        ImageIcon icon = new ImageIcon(workingDirectory + "\\alien_ship_1.png");
        Image alienImage = icon.getImage();
        icon = new ImageIcon(workingDirectory + "\\alien_ship_2.png");
        Image alienImage2 = icon.getImage();
        // create and initialise some aliens, passing them each the image we have

        for (int i = 0; i < NUMALIENS; i++) {
            AliensArray[i] = new Alien(alienImage, alienImage2);
            double xx = (i % 5) * 80 + 70;
            double yy = (i / 5) * 40 + 50;
            AliensArray[i].setPosition(xx, yy);
        }
        Alien.setFleetXSpeed(gamespeed);

        // create and initialise the player's spaceship
        icon = new ImageIcon(workingDirectory + "\\player_ship.png");
        Image shipImage = icon.getImage();
        PlayerShip = new Spaceship(shipImage, alienImage);
        PlayerShip.setPosition(300, 530);

        // create and initialise the player's bullets
        icon = new ImageIcon(workingDirectory + "\\bullet.png");
        bulletImage = icon.getImage();
        bullets.add(new PlayerBullet(bulletImage, alienImage));
        // tell all sprites the window width
        Sprite2D.setWinWidth(WindowSize.width);

        // create and start our animation thread
        Thread t = new Thread(this);
        t.start();

        // send keyboard events arriving into this JFrame back to its own event handlers
        addKeyListener(this);

        // initialise double-buffering
        createBufferStrategy(2);
        strategy = getBufferStrategy();
        offscreenGraphics = strategy.getDrawGraphics();
        isInitialised = true;
    }
    // thread's entry point
    public void run() {
        while (1 == 1) {
            // 1: sleep for 1/50 sec
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {}
            // 2: animate game objects 
            if (gameState == true) {
                boolean alienDirectionReversalNeeded = false;
                for (int i = 0; i < NUMALIENS; i++) {
                    if (AliensArray[i].move())
                        alienDirectionReversalNeeded = true;
                }
                if (alienDirectionReversalNeeded) {
                    Alien.reverseDirection();
                    for (int i = 0; i < NUMALIENS; i++)
                        AliensArray[i].jumpDownwards();
                }
                //for (int i=0;i<bullets.size(); i++) 
                if (bullets.get(0).move(NUMALIENS, AliensArray)) {
                    resetFleet();
                };

                if (PlayerShip.move(NUMALIENS, AliensArray)) {
                    PlayerBullet.score = 0;
                    gameState = false;
                    resetFleet();
                }
            }

            // 3: force an application repaint
            this.repaint();

        }
    }
    public void resetFleet() {
        int b = 0;
        for (int i = 0; i < NUMALIENS; i++) {

            if (AliensArray[i].isAlive == false) {
                b++;
            }
            double xx = (i % 5) * 80 + 70;
            double yy = (i / 5) * 40 + 50;

            AliensArray[i].setPosition(xx, yy);
            AliensArray[i].isAlive = true;
        }
        if (b == 30) {
            gamespeed += 4;
        }
        Alien.setFleetXSpeed(gamespeed);

    }
    // Three Keyboard Event-Handler functions
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            PlayerShip.setXSpeed(-4);
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            PlayerShip.setXSpeed(4);
        else if (e.getKeyCode() == KeyEvent.VK_SPACE)
            if (gameState == false) {
                gameState = true;
            }
        else {
            bullets.get(0).setPosition(PlayerShip.x + 25, PlayerShip.y);
            bullets.get(0).setXSpeed(-10);
        }

    }


    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT)
            PlayerShip.setXSpeed(0);
    }

    public void keyTyped(KeyEvent e) {}
    //
    // application's paint method
    public void paint2(Graphics g) {
        if (!isInitialised)
            return;



    }

    public void paint(Graphics g) {
        if (!isInitialised)
            return;
        g = offscreenGraphics; // draw to offscreen buffer
        // clear the canvas with a big black rectangle
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WindowSize.width, WindowSize.height);
        // redraw all game objects		
        if (gameState == false) {

            g.setFont(new Font("TimesRoman", Font.PLAIN, 90));
            g.setColor(Color.RED); // Here
            g.drawString("GAME OVER!!", 110, 250);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.setColor(Color.WHITE);
            g.drawString("Press Spacebar to start the game!!!", 270, 300);
            g.drawString("[ Press Arrow keys to move and Spacebar to shoot ]", 190, 320);
            strategy.show();
        } else {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
            g.setColor(Color.WHITE);
            g.drawString("SCORE : " + PlayerBullet.score + "\t BEST : " + PlayerBullet.best, 110, 50);
            for (int i = 0; i < NUMALIENS; i++)
                if (AliensArray[i].isAlive == true) {
                    AliensArray[i].paint(g);
                }

            PlayerShip.paint(g);
            bullets.get(0).paint(g);
            // flip the buffers offscreen<-->onscreen
            strategy.show();
        }

    }


    // application entry point
    public static void main(String[] args) {
        workingDirectory = System.getProperty("user.dir");
        System.out.println("Working Directory = " + workingDirectory);
        InvadersApplication w = new InvadersApplication();
    }
}
