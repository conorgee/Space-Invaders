import java.awt.Image;
public class Alien extends Sprite2D {
    private static double xSpeed = 0;
    public boolean isAlive = true;
    public Alien(Image i, Image ii) {
        super(i); // invoke constructor on superclass Sprite2D
        myImage2 = ii;
    }
    // public interface
    public boolean move() {
        x += xSpeed;
        // direction reversal needed?
        if (x <= 0 || x >= winWidth - myImage.getWidth(null))
            return true;
        else
            return false;
    }
    public static void setFleetXSpeed(double dx) {
        xSpeed = dx;
    }
    public static void reverseDirection() {
        xSpeed = -xSpeed;
    }
    public void jumpDownwards() {
        y += 20;
    }
}