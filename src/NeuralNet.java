/**
 описание работы нейронной сети // алгоритм обучения
 */
public class NeuralNet {
    private int inputVectorSize;
    private Neuron[] layer;
    private int epochNumber;
    private volatile boolean complete;
    private double[] error;
    private final double eta = 0.5;

    public NeuralNet(int inputVectorSize, int outputNeuronsCount) {
        this.inputVectorSize = inputVectorSize;
        layer = new Neuron[outputNeuronsCount];
        for (int j = 0; j < outputNeuronsCount; j++) {
            layer[j] = new Neuron(inputVectorSize);
        }
        error = new double[layer.length];
    }

    public int getEpochNumber() {
        return epochNumber;
    }

    public double[] getError() {
        return error;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void train(Vector[] vectorSet) throws InterruptedException
    {
        // эпоха обучения равна нулю
        epochNumber = 0;
        do // цикл до останова обучения
        {
            for (int m = 0; m < vectorSet.length; m++) // Шаг 3. перебор обучающих векторов
            {
                // Шаг 4
                for (int j = 0; j < layer.length; j++) //перебор нейронов
                {
                    layer[j].calcOut(vectorSet[m].getX());
                }
                // создаем массив для хранения ошибки каждого нейрона
                error = new double[layer.length];
                double sumError = 0.0;
                // Шаг 5
                for (int j = 0; j < layer.length; j++)
                { // считаем ошибка каждого j-го нейрона
                    error[j] = vectorSet[m].getDesireOutputs()[j] -
                            layer[j].getOut();
                    sumError += error[j];
                }

                Thread.sleep(5); // Задержка, чтобы мы могли наблюдать, как меняются ошибки нейронов
                // Шаг 6. Цикл коррекции синаптических весов
                for (int j = 0; j < layer.length; j++)
                {
                    int n = layer[j].getWeight().length; // кол-во синаптических весов j-го нейрона
                    double[] deltaWeight = new double[n];
                    for (int i = 0; i < n; i++)
                    {
                        deltaWeight[i] += eta * error[j] * vectorSet[m].getX()[i];
                    }
                    layer[j].correctWeights(deltaWeight);

                }

            }
            epochNumber++;
        }
        while (epochNumber <= 5); // критерий останова обучения
        complete = true;
    }

    public double[] test(double[] vector)
    {
        double[] outVector = new double[layer.length];
        for (int j = 0; j < layer.length; j++)
        {
            layer[j].calcOut(vector);
            outVector[j] = layer[j].getOut();
        }
        return outVector;
    }
}
