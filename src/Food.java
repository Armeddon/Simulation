import java.awt.*;

public class Food {
    private int x, y;
    private int size;
    private Color clr;
    private int energy;
    private boolean eaten;
    private boolean corpse;

    public Food(int x, int y) {
        this.x = x;
        this.y = y;

        size = 5;
        energy = 60;
        clr = Color.DARK_GRAY;

        eaten = false;
        corpse = false;
    }

    public Food(int x, int y, int energy) {
        this.x = x;
        this.y = y;
        this.energy = energy;

        size = 10;
        clr = Color.GRAY;

        eaten = false;
        corpse = true;
    }

    public void paint(Graphics gh) {
        gh.setColor(clr);
        gh.fillOval(x-size/2, y-size/2, size, size);
    }

    public int getEnergy() {
        return energy;
    }

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }

    public boolean isEaten() {
        return eaten;
    }

    public void eat() {
        eaten = true;
    }

    public boolean isCorpse() {
        return corpse;
    }
}
