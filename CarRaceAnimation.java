import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class CarRaceAnimation extends JPanel implements ActionListener {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 800;
    private static final int CAR_WIDTH = 200;
    private static final int CAR_HEIGHT = 100;
    private static final int FINISH_LINE = WIDTH - CAR_WIDTH;
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color FINISH_LINE_COLOR = Color.RED; // Цвет финишной линии
    private static final Font WINNER_FONT = new Font("Big Caslon", Font.BOLD, 24);

    private Timer timer;
    private Car[] cars;
    private boolean raceFinished;
    private int winner;

    public CarRaceAnimation() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(BACKGROUND_COLOR);

        cars = new Car[5];
        for (int i = 0; i < cars.length; i++) {
            ImageIcon carIcon = new ImageIcon(i + 1 + ".png");
            carIcon = resizeImage(carIcon, CAR_WIDTH, CAR_HEIGHT);

            int verticalSpacing = (HEIGHT - (CAR_HEIGHT * cars.length)) / (cars.length + 1);

            int x = 50 + i * 20;
            int y = verticalSpacing * (i + 1) + CAR_HEIGHT * i;
            cars[i] = new Car(x, y, carIcon, i); // Номер машины устанавливается здесь
        }

        timer = new Timer(50, this);
        timer.setInitialDelay(3000);
        timer.start();
    }

    private ImageIcon resizeImage(ImageIcon imageIcon, int width, int height) {
        Image image = imageIcon.getImage();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (Car car : cars) {
            car.draw(g);
        }

        // Рисуем финишную линию
        g.setColor(FINISH_LINE_COLOR);
        int finishLineX = FINISH_LINE;
        g.fillRect(finishLineX, 0, 5, HEIGHT);

        if (raceFinished) {
            g.setColor(Color.BLACK);
            g.setFont(WINNER_FONT);
            String winnerText = "Победитель: Машинка " + (winner + 1) + "!";

            FontMetrics fontMetrics = g.getFontMetrics(WINNER_FONT);
            int textWidth = fontMetrics.stringWidth(winnerText);
            int textHeight = fontMetrics.getHeight();

            int x = (WIDTH - textWidth) / 2;
            int y = textHeight + 20;

            g.drawString(winnerText, x, y);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!raceFinished) {
            for (Car car : cars) {
                car.move();
                if (car.getX() >= FINISH_LINE) {
                    raceFinished = true;
                    winner = car.getNumber();
                    break;
                }
            }
            repaint();
        }
    }

    private class Car {
        private int x;
        private int y;
        private int number;
        private int speed;
        private ImageIcon carIcon;

        public Car(int x, int y, ImageIcon carIcon, int number) {
            this.x = x;
            this.y = y;
            this.number = number; // Номер машины теперь инициализируется здесь
            this.speed = new Random().nextInt(10) + 1;
            this.carIcon = carIcon;
        }

        public int getX() {
            return x;
        }

        public int getNumber() {
            return number;
        }

        public void draw(Graphics g) {
            carIcon.paintIcon(null, g, x, y);
        }

        public void move() {
            x += speed;
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Гонка машинок");
        CarRaceAnimation carRace = new CarRaceAnimation();
        frame.add(carRace);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
