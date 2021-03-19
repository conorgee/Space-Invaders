import java.awt.Graphics;
import java.awt.Image;
public class Spaceship extends Sprite2D {
    public boolean isAlive = true;
    public Spaceship(Image i, Image ii) {
        super(i); // invoke constructor on superclass Sprite2D
        myImage2 = ii;
    }

    public boolean move(int num, Alien[] AliensArray) {
        // apply current movement
        x += xSpeed;
        // stop movement at screen edge?
        if (x <= 0) {
            x = 0;
            xSpeed = 0;
        } else if (x >= winWidth - myImage.getWidth(null)) {
            x = winWidth - myImage.getWidth(null);
            xSpeed = 0;
        }

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

                isAlive = false;


            }
        }
        if (isAlive == false) {

            isAlive = true;
            return true;
        }
        return false;
    }



    public void paint(Graphics g) {
        g.drawImage(myImage, (int) x, (int) y, null);
    }}
