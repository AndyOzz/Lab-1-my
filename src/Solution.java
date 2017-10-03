    // класс для построения сети на основе класса нейрона
    public class Solution {

        public static void main(String[] args) {//psvm
            Neuron and2Neural = new Neuron(3); // 1 значение смещения и 2 вхояих, создаем нейрон с тремя синоптическими вечами

            double[] weights = {-1.5, 1.0, 1.0};
            and2Neural.setWeight(weights);

            double[] testVector1 = {1.0, 1.0, 1.0}; // первая констстанта дальше х1 и х2
            and2Neural.calcOut(testVector1);
            System.out.println(and2Neural.getOut());

            double[] testVector2 = {1.0, 1.0, 0.0}; // первая констстанта дальше х1 и х2
            and2Neural.calcOut(testVector2);
            System.out.println(and2Neural.getOut());

            double[] testVector3 = {1.0, 0.0, 1.0}; // первая констстанта дальше х1 и х2
            and2Neural.calcOut(testVector3);
            System.out.println(and2Neural.getOut());

            double[] testVector4 = {1.0, 0.0, 0.0}; // первая констстанта дальше х1 и х2
            and2Neural.calcOut(testVector4);
            System.out.println(and2Neural.getOut());

        }
    }
