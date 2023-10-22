import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class App {
    private JFrame frame;
    private JPanel panel;
    private List<Bot> bots;
    private List<Food> foods;
    private int foodDelay;
    private int foodDelayCounter;

    private int generation;


    private App() {
        createWindow();
        createGraphics();

        bots = new LinkedList<Bot>();

        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            bots.add(new Bot(Color.RED, 50+15*r.nextInt(50), 50+15*r.nextInt(30), frame.getWidth(), frame.getHeight(), new Brain()));
        }
        for (int i = 0; i < 10; i++) {
            bots.add(new Bot(Color.GREEN, 50+15*r.nextInt(50), 50+15*r.nextInt(30), frame.getWidth(), frame.getHeight(), new Brain()));
        }
        for (int i = 0; i < 10; i++) {
            bots.add(new Bot(Color.BLUE, 50+15*r.nextInt(50), 50+15*r.nextInt(30), frame.getWidth(), frame.getHeight(), new Brain()));
        }
        for (int i = 0; i < 10; i++) {
            bots.add(new Bot(Color.YELLOW, 50+15*r.nextInt(50), 50+15*r.nextInt(30), frame.getWidth(), frame.getHeight(), new Brain()));
        }
        for (int i = 0; i < 10; i++) {
            bots.add(new Bot(Color.PINK, 50+15*r.nextInt(50), 50+15*r.nextInt(30), frame.getWidth(), frame.getHeight(), new Brain()));
        }
        
        foods = new LinkedList<Food>();

        foodDelay = 0;
        foodDelayCounter = 0;

        generation = 1;

        while (true) {
            botAct();
            checkFood();
            
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } 

            panel.repaint();
        }
    }

    private void createWindow() {
        frame = new JFrame();

        frame.setTitle("Simulation");
        
        frame.setSize(800, 600);
        frame.setResizable(false);

        frame.setVisible(true);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setLocationRelativeTo(null);

        panel = new JPanel() {
            @Override
            public void paint(Graphics gh) {
                super.paint(gh);
                for (Bot bot : bots) {
                    bot.paint(gh);
                }
                for (Food food: foods) {
                    food.paint(gh);
                }
            }
        };

        frame.add(panel);
    }

    private void createGraphics() {
        frame.createBufferStrategy(30);
        panel.setDoubleBuffered(true);

        panel.setBackground(Color.WHITE);
    }

    private void botAct() {
        for (Bot bot: bots){
            bot.act(bots, foods);
        }
        checkBots();
    }

    private void checkFood() {        
        if (foodDelayCounter > 0) {
            foodDelayCounter -= 1;
        } else {
            foodDelayCounter = foodDelay;

            Random rnd = new Random();
            foods.add(new Food(50+15*rnd.nextInt((frame.getWidth()-50)/15), 50+15*rnd.nextInt((frame.getHeight()-50)/15)));
        }

        List<Food> newFoods = new LinkedList<Food>();
        for (Food food: foods) {
            if (!food.isEaten()) {
                newFoods.add(food);
            }
        }
        foods = newFoods;

    }

    private void checkBots() {
        List<Bot> newBots = new LinkedList<Bot>();
        for (Bot bot : bots) {
            if (!bot.isDead()) {
                newBots.add(bot);
                if (bot.hasBaby()) {
                    newBots.add(bot.getBaby());
                }
            } else {
                foods.add(new Food(bot.X(), bot.Y(), -10));
            }
        }
        bots = newBots;

        if (bots.size() <= 10) {
            if (bots.size() == 0)
            {
                System.out.println("DEATH!!!");
                try {
                    wait();
                } catch (InterruptedException e) {
                    
                }
            }
            evolve();
        }
    }

    private void evolve() {
        generation += 1;
        System.out.print("Generation: ");
        System.out.println(generation);

        foods = new LinkedList<Food>();

        Random rnd = new Random();

        List<Bot> newBots = new LinkedList<Bot>();
        for (Bot bot: bots) {
            for (int i = 0; i < 5; i++) {
                newBots.add(new Bot(bot.getColor(), 50+15*rnd.nextInt(50), 50+15*rnd.nextInt(30), frame.getWidth(), frame.getHeight(), bot.getBrain()));
            }
        }

        bots = newBots;
    }
    public static void main(String[] args) throws Exception {
        new App();
    }
}
