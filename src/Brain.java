import java.util.Random;
import java.util.List;

public class Brain {
    private double[][] weights1, weights2, weights3;
    private int layer1, layer2, layerInput, layerOutput;

    public Brain() {
        layer1 = 5;
        layer2 = 5;

        layerInput = 6;
        layerOutput = 5;

        weights1 = new double[layer1][layerInput];
        weights2 = new double[layer2][layer1];
        weights3 = new double[layerOutput][layer2];

        for (int i = 0; i < layer1; i++) {
            for (int j = 0; j < layerInput; j++) {
                weights1[i][j] = 1.0;
            }
        }
        for (int i = 0; i < layer2; i++) {
            for (int j = 0; j < layer1; j++) {
                weights2[i][j] = 1.0;
            }
        }
        for (int i = 0; i < layerOutput; i++) {
            for (int j = 0; j < layer2; j++) {
                weights3[i][j] = 1.0;
            }
        }
    }

    public Brain(Brain brain) {
        layerInput = brain.layerInput;
        layerOutput = brain.layerOutput;
        layer1 = brain.layer1;
        layer2 = brain.layer2;
        
        weights1 = brain.weights1.clone();
        weights2 = brain.weights2.clone();
        weights3 = brain.weights3.clone();

        Random rnd = new Random();
        switch (rnd.nextInt(3)) {
            case 0: {
                weights1[rnd.nextInt(layer1)][rnd.nextInt(layerInput)] += rnd.nextDouble();
                break;
            } case 1: {
                weights2[rnd.nextInt(layer2)][rnd.nextInt(layer1)] += rnd.nextDouble();
                break;
            } case 2: {
                weights3[rnd.nextInt(layerOutput)][rnd.nextInt(layer2)] += rnd.nextDouble();
                break;
            }
        }
    }

    public Action calculate(Bot bot, List<Bot> bots, List<Food> foods) {
        double[] input = new double[layerInput];
        input[0] = bot.X();
        input[1] = bot.Y();
        input[2] = bot.getDirection().X + bot.getDirection().Y*3 + 5;
        input[3] = bot.getEnergy();
        input[4] = bot.seesFood(foods);
        input[5] = bot.seesBot(bots);

        double[] nodes1 = new double[layer1];
        for (int i = 0; i < layer1; i++) {
            nodes1[i] = 0;
            for (int j = 0; j < layerInput; j++) {
                nodes1[i] += input[j]*weights1[i][j];
            }
        }

        double[] nodes2 = new double[layer2];
        for (int i = 0; i < layer2; i++) {
            nodes2[i] = 0;
            for (int j = 0; j < layer1; j++) {
                nodes2[i] += nodes1[j]*weights2[i][j];
            }
        }

        double[] nodes3 = new double[layerOutput];
        for (int i = 0; i < layerOutput; i++) {
            nodes3[i] = 0;
            for (int j = 0; j < layer2; j++) {
                nodes3[i] += nodes2[j]*weights3[i][j];
            }
        }

        Random rnd = new Random();

        double sm = 0;
        for (double node: nodes3) {
            sm += node;
        }
        
        double n;
        try {
            n = rnd.nextDouble()+rnd.nextInt((int) sm);
        } catch (Exception e) {
            n = 0;
        }

        double t_sm = 0;

        int res = 0;

        for (int i = 0; i < layerOutput; ++i) {
            t_sm += nodes3[i];
            if (n < t_sm) {
                res = i;
                break;
            }
        }

        switch (res) {
            case 0: {
                return Action.Move;
            } case 1: {
                return Action.TurnCW;
            } case 2: {
                return Action.TurnCCW;
            } case 3: {
                return Action.PhotoSynthesis;
            } case 4: {
                return Action.Reproduce;
            }
        }

        return Action.PhotoSynthesis;
    }
}
