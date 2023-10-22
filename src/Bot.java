import java.awt.*;
import java.util.List;

public class Bot {
    private Color clr;
    private int x, y;
    private int size;
    private Direction dir;
    private Action action;
    private int energy;
    private int repr_energy;
    private int maxEnergy;
    private boolean dead;
    private int worldWidth, worldHeight;
    private Bot baby;
    private Brain brain;

    public Bot(Color clr, int x, int y, int worldW, int worldH, Brain brain) {
        this.clr = clr;
        this.x = x;
        this.y = y;
        this.brain = brain;
        worldWidth = worldW;
        worldHeight = worldH;

        size = 15;
        energy = 50;
        repr_energy = 75;
        maxEnergy = 300;
        dir = new Direction();

        dead = false;
        action = Action.PhotoSynthesis;
        baby = null;
    }

    public void paint(Graphics gh) {
        gh.setColor(clr);
        gh.fillRect(x-size/2, y-size/2, size, size);
    }

    private void move(List<Food> foods) {
        if (!(x < size || x > worldWidth - size)) {
            x += size*dir.X;
        }
        if (!(y < size || y > worldHeight - size)) {
            y += size*dir.Y;
        }

        for (Food food: foods) {
            if (food.X() == x && food.Y() == y) {
                changeEnergy(food.getEnergy());
                food.eat();
            }
        }
    }

    private void turn(Turn trnDir) {
        switch (trnDir) {
            case Clockwise: {
                int sm = dir.X + dir.Y;
                if (sm == 0) {
                    dir.X = 0;
                } else if (Math.abs(sm) == 1) {
                    dir.Y -= dir.X;
                    dir.X = sm;
                } else {
                    dir.Y = 0;
                }
                break;
            }
            case CounterClockwise: {
                int sm = dir.X + dir.Y;
                if (sm == 0) {
                    dir.X = 0;
                } else if (Math.abs(sm) == 1) {
                    dir.X -= dir.Y;
                    dir.Y = sm;
                } else {
                    dir.X = 0;
                }
                break;
            }
        }
    }

    private void photo() {
        changeEnergy(20);
    }

    private void reproduce(List<Bot> bots) {
        if (energy <= repr_energy) {
            changeEnergy(-25);
            return;
        }

        boolean[][] pos = new boolean[3][3];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                pos[i][j] = !(i == 1 && j == 1);
            }
        }

        for (Bot bot: bots) {
            if ((bot.X()-x)/size >= -1 && (bot.X()-x)/size <= 1 && (bot.Y()-y)/size >= -1 && (bot.Y()-y)/size <= 1) {
                pos[(bot.X()-x)/size+1][(bot.Y()-y)/size+1] = false;
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                if (pos[i][j]){
                    changeEnergy(-1*repr_energy);
                    baby = new Bot(clr, x+size*(i-1),y+size*(j-1), worldWidth, worldHeight, new Brain(brain));
                    return;
                }
            }
        }
    }

    private void changeEnergy(int change) {
        if (change == 0) {
            return;
        } else {
            energy += change;
            if (energy > maxEnergy) {
                maxEnergy = energy;
            }
            if (energy <= 0) {
                die();
            }
        }
    }

    private void die() {
        dead = true;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean hasBaby() {
        return !(baby == null);
    }

    public Bot getBaby() {
        Bot t_baby = baby;
        baby = null;
        return t_baby;
    }

    public int getEnergy() {
        return energy;
    }

    public int seesFood(List<Food> foods) {
        for (Food food: foods) {
            if (x+size*dir.X == food.X() && y+size*dir.Y == food.Y()) {
                if (food.isCorpse()) {
                    return 2;
                } else {
                    return 1;
                }
            }
        }
        return 0;
    }

    public int seesBot(List<Bot> bots) {
        for (Bot bot: bots) {
            if (x+size*dir.X == bot.X() && y+size*dir.Y == bot.Y()) {
                return 1;
            }
        }
        return 0;
    }

    public void act(List<Bot> bots, List<Food> foods) {
        switch (action) {
            case Move: {
                move(foods);
                changeEnergy(-10);
                break;
            } case TurnCW: {
                turn(Turn.Clockwise);
                break;
            } case TurnCCW: {
                turn(Turn.CounterClockwise);
                break;
            } case PhotoSynthesis: {
                photo();
                break;
            } case Reproduce: {
                reproduce(bots);
                break;
            }
        }

        action = brain.calculate(this, bots, foods);
    }

    public int X() {
        return x;
    }

    public int Y() {
        return y;
    }

    public Direction getDirection() {
        return dir;
    }

    public Brain getBrain() {
        return brain;
    }

    public Color getColor() {
        return clr;
    }
}
