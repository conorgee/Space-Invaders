import java.awt.Graphics;
import java.awt.Image;



public class PlayerBullet extends Sprite2D {
    private int isDead = 0;
    public static int best, score = 0;
    public PlayerBullet(Image i, Image ii) {
        super(i);
        myImage2 = ii;

    }
    public boolean move(int num, Alien[] AliensArray) {
        y += -10;

        // direction reversal needed?
        for (int i = 0; i < num; i++) {
            double x2 = x;
            double y2 = y;
            double x1 = AliensArray[i].x;
            double y1 = AliensArray[i].y;
            double h2 = myImage.getHeight(null);
            double h1 = myImage2.getHeight(null);
            double w2 = myImage.getWidth(null);
            double w1 = myImage2.getWidth(null);

            if (
                ((x1 < x2 && x1 + w1 > x2) ||
                    (x2 < x1 && x2 + w2 > x1)) &&
                ((y1 < y2 && y1 + h1 > y2) ||
                    (y2 < y1 && y2 + h2 > y1))) {

                AliensArray[i].isAlive = false;
                score += 5;

            }
            if (AliensArray[i].isAlive == false) {
                isDead++;
            }
        }
        if (score > best) {
            best = score;
        }
        if (isDead == 30) {
            isDead = 0;
            return true;
        } else
            isDead = 0;
        return false;
    }

    public void paint(Graphics g) {
        g.drawImage(myImage, (int) x, (int) y, null);
    }
}