
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    // Attribute GamePanel
    int winningScore = 10;

    // Attribute GameServer
    int max_clients = 4;
    int count_clients = 0;
    int player1_y = 410;
    int player2_y = 410;
    int player3_y = 410;
    int player4_y = 410;

    // Attribute Paddle
    int paddle_x;
    int paddle_y = 410;
    int paddle_height = 150;
    int paddle_width = 20;
    int paddle_speedY = 10;

    // Attribute Ball
    int ball_x = 785;
    int ball_y = 490;
    int ball_size = 30;
    int ball_speedX = 5;
    int ball_speedY = 5;
    int score1 = 0;
    int score2 = 0;

    // Object from Class
    Paddle[] paddles = new Paddle[4];
    Ball ball = new Ball(paddles);

    public GamePanel() {
        setBackground(Color.black);

        paddles[0] = new Paddle(110, player1_y, paddle_width, paddle_height);
        paddles[1] = new Paddle(1450, player2_y, paddle_width, paddle_height);
        paddles[2] = new Paddle(310, player3_y, paddle_width, paddle_height);
        paddles[3] = new Paddle(1260, player4_y, paddle_width, paddle_height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // สร้างกรอบสนาม
        g.setColor(Color.white);
        g.fillRect(20, 150, 10, 680);
        g.fillRect(20, 830, 1540, 10);
        g.fillRect(20, 150, 1540, 10);
        g.fillRect(1550, 150, 10, 680);

        // สร้างเส้นแบ่งกึ่งกลาง
        g.setColor(Color.white);
        Graphics2D graphics2d = (Graphics2D) g;
        Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0);
        graphics2d.setStroke(dashed);
        graphics2d.drawLine(798, 160, 798, 830);

        // สร้างเส้นกำหนดตำแหน่งของ paddle
        g.setColor(Color.white);
        g.fillRect(120, 150, 1, 680);
        g.fillRect(320, 150, 1, 680);
        g.fillRect(1270, 150, 1, 680);
        g.fillRect(1460, 150, 1, 680);

        // สร้างกล่องแสดงข้อมูล (แค่กล่องเปล่าๆ)
        g.setColor(Color.WHITE);
        g.fillRect(700, 20, 200, 110);
        g.fillRect(20, 20, 300, 110);
        g.fillRect(1260, 20, 300, 110);
        g.fillRect(350, 20, 100, 110);
        g.fillRect(1130, 20, 100, 110);

        // สร้างคะแนนที่เป็นเป้าหมายในการจบเกม
        g.setColor(Color.black);
        g.setFont(new Font("Tahoma", Font.BOLD, 40));
        g.drawString("GOAL", 742, 70);
        g.drawString("" + winningScore, 772, 110);

        // วาดคะแนนของแต่ละทีม
        g.setColor(Color.black);
        g.setFont(new Font("Tahoma", Font.BOLD, 40));
        g.drawString(String.valueOf(score1), 387, 88);
        g.drawString(String.valueOf(score2), 1170, 88);

        // สร้างตัวอักษร Team, Player
        g.setColor(Color.black);
        g.setFont(new Font("Tahoma", Font.BOLD, 20));
        g.drawString("Team1", 130, 40);
        g.drawString("Team2", 1380, 40);
        g.drawString("Player 1", 40, 75);
        g.drawString("Player 2", 1280, 75);
        g.drawString("Player 3", 40, 105);
        g.drawString("Player 4", 1280, 105);

        // สร้างแถบสีให้ player (อยู่ที่กล่องข้อมูล)
        g.setColor(Color.red);      // สีแดง player1
        g.fillRect(150, 58, 150, 20);
        g.setColor(Color.orange);   // สีส้ม player2
        g.fillRect(1390, 58, 150, 20);
        g.setColor(Color.blue);     // สีน้ำเงิน player3
        g.fillRect(150, 88, 150, 20);
        g.setColor(Color.green);    // สีเขียว player4
        g.fillRect(1390, 88, 150, 20);

        // สร้าง paddle

        for (int i = 0; i < paddles.length; i++) {
            if (i == 0) {g.setColor(Color.red);}
            if (i == 1) {g.setColor(Color.orange);}
            if (i == 2) {g.setColor(Color.blue);}
            if (i == 3) {g.setColor(Color.green);}

            paddles[i].draw(g);
        }

        // สร้าง ball
        ball.draw(g);
    }

    public int getWinningScore() {
        return winningScore;
    }

    public void setBall_x(int ball_x) {
        this.ball_x = ball_x;
    }

    public void setBall_y(int ball_y) {
        this.ball_y = ball_y;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public void setPlayer1_y(int player1_y) {
        this.player1_y = player1_y;
    }

    public void setPlayer2_y(int player2_y) {
        this.player2_y = player2_y;
    }

    public void setPlayer3_y(int player3_y) {
        this.player3_y = player3_y;
    }

    public void setPlayer4_y(int player4_y) {
        this.player4_y = player4_y;
    }
}
