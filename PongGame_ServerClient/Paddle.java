
import java.awt.Graphics;
import java.awt.Rectangle;

public class Paddle {
    int paddle_x;
    int paddle_y;
    int paddle_width;
    int paddle_height;
    int paddle_speedY = 10;

    public Paddle(int paddle_x, int paddle_y, int paddle_width, int paddle_height) {
        this.paddle_x = paddle_x;
        this.paddle_y = paddle_y;
        this.paddle_width = paddle_width;
        this.paddle_height = paddle_height;
    }

    public void draw(Graphics g) {
        g.fillRect(paddle_x, paddle_y, paddle_width, paddle_height);
    }

    public int paddleMoveUp() {
        paddle_y = paddle_y - paddle_speedY;
        if (paddle_y < 160) {paddle_y = 160;}
        
        return paddle_y;
    }

    public int paddleMoveDown() {
        paddle_y = paddle_y + paddle_speedY;
        if (paddle_y > 680) {paddle_y = 680;}

        return paddle_y;
    }

    public Rectangle getBounds() {
        return new Rectangle(paddle_x, paddle_y, paddle_x, paddle_height);
    }
}
