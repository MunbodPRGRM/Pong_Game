
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Ball {
    // Attribute Ball
    int ball_x = 785;
    int ball_y = 490;
    int ball_size = 30;
    int ball_speedX = 5;
    int ball_speedY = 5;
    int score1 = 0;
    int score2 = 0;
    
    // Object from class
    Paddle[] paddles;

    public Ball(Paddle[] paddles) {
        this.paddles = paddles;
    }

    public void draw(Graphics g) {
        g.setColor(Color.white);
        g.fillOval(ball_x, ball_y, ball_size, ball_size);
    }

    public void ballMove() {
        ball_x = ball_x + ball_speedX;
        ball_y = ball_y + ball_speedY;

        if (ball_y < 160 || ball_y > 800) {
            if (ball_y < 160) {ball_y = 160;}
            else if (ball_y > 800) {ball_y = 800;}
            ball_speedY = randomNumber();
        }

        for (int i = 0; i < paddles.length; i++) {
            if (this.getBounds().intersects(paddles[i].getBounds())) {
                ball_speedX = -ball_speedX;
                ball_speedY = -ball_speedY;
            }
        }

        scoring();
    }

    public void scoring() {
        if (ball_x <= 20) {
            score2++;
            resetBall();
        }
        else if (ball_x >= 1550) {
            score1++;
            resetBall();
        }
    }

    public void resetBall() {
        ball_x = 785;
        ball_y = 490;
        ball_speedX = randomNumber();
        ball_speedY = randomNumber();
    }

    public int randomNumber() {
        Random random = new Random();
        int number = 0;
        boolean isNegative = random.nextBoolean();

        if (isNegative) {
            while (number == 0) {
                number = random.nextInt(6) - 10;
            }
        }
        else {
            while (number == 0) {
                number = random.nextInt(6) + 5;
            }
        }

        return number;
    }

    public Rectangle getBounds() {
        return new Rectangle(ball_x, ball_y, ball_size, ball_size);
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }

    public int getBall_x() {
        return ball_x;
    }

    public int getBall_y() {
        return ball_y;
    }
}
