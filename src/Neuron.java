import java.util.Random;

//создали новый ява класс Нейрон
public class Neuron {
    //создаем массив
    private double[] weight;
    // вектор синоптических весов
    private double out; //значение выхода нейрона
    private double sum; //суииатор нейрона, три переменные которые хранит тело нейрона

    public static Random random = new Random(); //обьявляем создаем экземпляр генератора случацных чисел

    public static final double rangeMin = -0.0003;
    public static final double rangeMax = 0.0003;


    //созданем конструктор принимающий значение синоптических весов
    public Neuron(int weightCount)
    {
        weight = new double [weightCount];
        out=0.0;//присваиваем переменной колличество весов
        randomizeWeights(); //добавили в нейрон созданный позднее матод
    }

    public void randomizeWeights(){
        for (int i = 0; i < weight.length; i++) {
            weight [i] = rangeMin + (rangeMax - rangeMin) * random.nextDouble();
        }
    }

    public double getOut()//геттер для выхода
    {
        return out;
    }
//alt inset геттер для веса
    public double[] getWeight() {
        return weight;
    }
// сеттер для синопитческих весов
    public void setWeight(double[] weight) {
        this.weight = weight;
    }

    //метод для активации

    private double activationFunc(double val){
        return val>= 0 ? 1 : 0;
    }



    public void calcOut(double[] x)
    {
        sum = 0.0;
        for (int i=0; i < x.length; i++)
        {
            sum += x[i]*weight[i];
        }
        out = activationFunc(sum);
    }

    public void correctWeights(double [] deltaWeight) {
        for (int i = 0; i < weight.length; i++) {
            weight[i] += deltaWeight[i];
        }
    }

    //описали класс нейрон, реализуем функцию И
}
