import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static javax.swing.JOptionPane.showMessageDialog;

public class GUI extends JFrame
{
    private JPanel rootPanel;
    private JButton buttonTrain;
    private JPanel imagePanel;
    private JLabel imageLabel;
    private JLabel labelError;
    private JLabel labelAnswer;
    private JLabel labelEpoch;
    private JTextArea textAreaAnswer;
    private JButton buttonTest;
    private NeuralNet neuralNet;

    public GUI()
    {
        setContentPane(rootPanel);
        pack();
        setTitle("Обучение через дельта-правило"); //название окна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //закрывать окно по нажатию кнопки закрыть
        setLocationRelativeTo(null); //окно по центру
        setVisible(true); //видимое окно
        buttonTest.setEnabled(false); //блокировка кнопки тест до конца обучения

        buttonTrain.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                train();
            } //вешаем прослушивателей - нажатия
        });
        buttonTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                test();
            }
        });
    }

    public void train()// здесь свои трейны и тесты как готовится выборка и подвеится
    {
        try //защищенна облатськода, отлов ошибки
        {
            buttonTest.setEnabled(false); //кнопка тесь не активна
            Vector[] trainVectorSet = readTrainVectors("C://Train an"); //считчаем вектор обучения, массив массивов ,
            neuralNet = new NeuralNet(trainVectorSet[0].getX().length, trainVectorSet[0].getDesireOutputs().length);
            neuralNet.setComplete(false);//присваиваем обучениею чети 0

            Runnable task1 = () -> { try { neuralNet.train(trainVectorSet); } catch (InterruptedException e) {} }; //запускаем два потока первый поток фоновый. трейн из нейронной сети
            Thread thread1 = new Thread(task1);//сам поток
            Runnable task2 = () -> {//вторая задача
                while (!neuralNet.isComplete())//пока нейронная сеть не обчена. считывать номер эпохи и ошибки каждоо нейрона
                {
                    labelEpoch.setText("Номер эпохи: " + neuralNet.getEpochNumber());
                    labelError.setText("Ошибки нейронов: " + Arrays.toString(neuralNet.getError()));
                }
                textAreaAnswer.append("Обучение завершено\n"); //когдаобучиться вывести сообщение
                buttonTest.setEnabled(true); // сделать доступной кнопку сест
            };
            Thread thread2 = new Thread(task2); // задаем второй поток

            thread1.start();
            thread2.start();//запускаме потоки
        }
        catch (IOException e)
        {
            showMessageDialog(null, "Файл не найден");
        }
        catch (Exception e)
        {
            showMessageDialog(null, e.toString());
        }

        int f = 0;
    }

    public void test() { //считываем тестовые образы
        try
        {
            String path = "c://Test an//"; //диреткория файлов
            for (int i = 0; i < 4; i++)
            {
                File[] files = new File(path + i).listFiles();//считываем вектора
                for (File file : files)
                {
                    double[] testVector = readVector(file.getPath());
                    double[] answer = neuralNet.test(testVector); //получение ветора ответов
                    textAreaAnswer.append(String.format("Тест-образ №%d = %s;%n", i, Arrays.toString(answer)));//задаем подпист
                    //textAreaAnswer.setCaretPosition(textAreaAnswer.getDocument().getLength());
                }
            }
        }
        catch (IOException e)
        {
            showMessageDialog(null, "Файл не найден");
        }
        catch (Exception e)
        {
            showMessageDialog(null, e.toString());
        }
    }

    public double[] readVector(String path) throws IOException
    {
        BufferedImage image = ImageIO.read(new File(path));
        int[][] grayImage = imageToGrayScale(image);
        double[] imageVector = imageToVector(grayImage);
        return imageVector;
    }


    public Vector[] readTrainVectors(String rootDir) throws IOException //указываем каталог векторов и если что на трай что бы вылоить ошибку
    {
        List<Vector> trainVectorSet = new ArrayList(); //создаем пустую переменную

        for (int i = 0; i < 4; i++) //переираем папки под номерами
        {
            File[] files = new File(rootDir + "//" + i).listFiles(); //считываем все пути к файлам в папке рут дир(с:трейн) + I(ноиера папок) C;\train\0
            for (File file : files) //перебор файлов
            {
                BufferedImage image = ImageIO.read(file); //файлы в буфер

                int[][] grayImage = imageToGrayScale(image); //выхзов метода переводяшего изобразение в 0 или 1
                double[] imageVector = imageToVector(grayImage); //получение гармошки значений

                double[] desireOutputs = new double[4]; //получение всх 6 гармошек
                for (int k = 0; k < desireOutputs.length; k++) //цикл заполняющий вектор желаемых значений
                {
                    desireOutputs[k] = i == k ? 1 : 0;
                }

                trainVectorSet.add(new Vector(imageVector, desireOutputs)); //подаем гармошки и наш векитор желаем знач
                //imageLabel.setIcon(new ImageIcon(image));
            }
        }
        return (Vector[])trainVectorSet.toArray(new Vector[trainVectorSet.size()]);
    }

    public int[][] imageToGrayScale(BufferedImage image) //перевод цветов в один канал ч\б и получение цыфры в зависимости от цвета 0 или 1
    {
        int[][] resultImage = new int[image.getWidth()][image.getHeight()];
        for(int x = 0; x < image.getWidth(); x++)
        {
            for (int y = 0; y < image.getHeight(); y++)
            {
                Color c = new Color(image.getRGB(x, y));
                resultImage[x][y] = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
            }
        }
        return resultImage;
    }

    public BufferedImage grayScaleToImage(int[][] grayImage)
    {
        int height = grayImage[0].length;
        int width = grayImage[1].length;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                Color c = new Color(grayImage[x][y], grayImage[x][y], grayImage[x][y], 0);
                image.setRGB(x, y, c.getRGB());
            }
        }
        return image;
    }


    public double[] imageToVector(int[][] image)//перевод из таблицы в строку
    {
        double[] resultVector = new double[image[0].length * image[1].length];
        int i = 0;
        for(int x = 0; x < image.length; x++)
        {
            for (int y = 0; y < image.length; y++)
            {
                resultVector[i++] = image[x][y];
            }
        }
        return resultVector;
    }

}
