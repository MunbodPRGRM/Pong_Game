
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

// เป็น class ที่สร้าง frame และเป็น class main ในการรันโปรแกรม
public class PongGame extends JFrame {
    // สร้าง object จาก class GamePanel 
    private GamePanel gamePanel;

    public PongGame() {
        setTitle("Pong Game");
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        new PongGame();
    }
}

class GamePanel extends JPanel implements KeyListener, Runnable {
    private Paddle[] paddles;               // สร้าง object paddle แบบ array
    private Ball ball;                      // สร้าง object ball
    private Thread gameThread;              // สร้าง thread ที่ควบคุมการทำงาน panel ของ GamePanel
    private int winningScore = 10;          // สร้าง Goal Score อยู่ที่ 10
    private boolean gameRunning = true;     // สร้าง flag เพื่อทำให้ thread ทำงานต่อไป หรือทำให้ thread ตาย

    // constructor ของ GamePanel
    public GamePanel() {
        setBackground(Color.black);
        setFocusable(true);     // ถ้าไม่ใช้ setFocusable จะไม่สามารถกดปุ่มบนคีย์บอร์ดได้
        addKeyListener(this);

        // สร้าง paddle และกำหนดตำแหน่ง
        paddles = new Paddle[4];
        paddles[0] = new Paddle(110, 410, KeyEvent.VK_W, KeyEvent.VK_S);
        paddles[1] = new Paddle(310, 410, KeyEvent.VK_R, KeyEvent.VK_F);
        paddles[2] = new Paddle(1260, 410, KeyEvent.VK_U, KeyEvent.VK_J);
        paddles[3] = new Paddle(1450, 410, KeyEvent.VK_UP, KeyEvent.VK_DOWN);

        // สร้าง thread ของ paddle แต่ละตัว
        for (int i = 0; i < paddles.length; i++) {
            Thread paddleThread = new Thread(paddles[i]);
            paddleThread.start();
        }

        // สร้าง ball และกำหนดตำแหน่ง, paddles ใช้เพื่อกำหนดทิศทางของ ball เมื่อชนกับ paddle
        ball = new Ball(785, 490, paddles);
        // สร้าง thread ของ ball แค่ตัวเดียว
        Thread ballThread = new Thread(ball);
        ballThread.start();

        // สร้าง thread ของ GamePanel เพื่อให้ panel ทำการ repaint
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);        // ล้างและรีเฟรชหน้าจอก่อนวาด

        // สร้างกรอบสนาม
        g.setColor(Color.white);
        g.fillRect(20, 150, 10, 680);
        g.fillRect(20, 830, 1540, 10);
        g.fillRect(20, 150, 1540, 10);
        g.fillRect(1550, 150, 10, 680);

        // สร้างเส้นแบ่งกึ่งกลาง
        g.setColor(Color.lightGray);
        g.fillRect(798, 160, 5, 670);

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

        // สร้างตัวอักษร Team, Player
        g.setColor(Color.black);
        g.setFont(new Font("Tahoma", Font.BOLD, 20));
        g.drawString("Team1", 130, 40);
        g.drawString("Team2", 1380, 40);
        g.drawString("Player 1", 40, 75);
        g.drawString("Player 2", 40, 105);
        g.drawString("Player 3", 1280, 75);
        g.drawString("Player 4", 1280, 105);

        // สร้างแถบสีให้ player (อยู่ที่กล่องข้อมูล)
        g.setColor(Color.red);      // สีแดง player1
        g.fillRect(150, 58, 150, 20);
        g.setColor(Color.orange);   // สีส้ม player2
        g.fillRect(150, 88, 150, 20);
        g.setColor(Color.blue);     // สีน้ำเงิน player3
        g.fillRect(1390, 58, 150, 20);
        g.setColor(Color.green);    // สีเขียว player4
        g.fillRect(1390, 88, 150, 20);

        // วาด paddle
        for (int i = 0; i < paddles.length; i++) {
            if (i == 0) {g.setColor(Color.red);}            // สีแดง player1
            else if (i == 1) {g.setColor(Color.orange);}    // สีส้ม player2
            else if (i == 2) {g.setColor(Color.blue);}      // สีน้ำเงิน player3
            else if (i == 3) {g.setColor(Color.green);}     // สีเขียว player4

            paddles[i].draw(g);
        }

        // วาด ball
        ball.draw(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        for (int i = 0; i < paddles.length; i++) {
            paddles[i].keyPressed(e);       // method บรรทัดที่ 233
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        for (int i = 0; i < paddles.length; i++) {
            paddles[i].keyReleased(e);      // method บรรทัดที่ 243
        }
    }

    private void checkWinningCondition() {
        if (ball.getScore1() >= winningScore) {
            // thread ที่ควบคุม panel ตาย
            this.gameRunning = false;
            // thread ที่ควบคุม ball ตาย
            ball.setGameRunning(false);     // method บรรทัดที่ 367
            // thread ที่ควบคุม paddle ตาย
            for (int i = 0 ; i < paddles.length; i++) {
                paddles[i].setGameRunning(false);       // method บรรทัดที่ 255
            }

            // แสดงหน้าว่าทีมที่ 1 ชนะ
            JOptionPane.showMessageDialog(this, "Team 1 wins!");
        } 
        else if (ball.getScore2() >= winningScore) {
            this.gameRunning = false;
            ball.setGameRunning(false);
            for (int i = 0 ; i < paddles.length; i++) {
                paddles[i].setGameRunning(false);
            }

            // แสดงหน้าว่าทีมที่ 2 ชนะ
            JOptionPane.showMessageDialog(this, "Team 2 wins!");
        }
    }

    @Override
    public void run() {
        // ถ้า gameRunning เป็น true, panel ก็จะ repaint ต่อเรื่อยๆ
        while (gameRunning) { 
            checkWinningCondition();        // method บรรทัดที่ 159
            repaint();
            
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
            }
        }
    }
}

// เป็น class ที่มีลักษณะ และการทำงานของ paddle
class Paddle implements Runnable {
    private int x, y;                       // ตำแหน่งของ paddle
    private int width = 20;                 // ความกว้างของ paddle
    private int height = 150;               // ความสูงของ paddle
    private int yVelocity = 0;              // การเคลื่อนที่ของ paddle ในแนวแกน y
    private int speed = 5;                  // ความเร็วในการเคลื่อนที่ของ paddle
    private int upKey, downKey;             // สร้างเพื่อใช้แทนปุ่มลูกศร
    private boolean gameRunning = true;     // สร้าง flag เพื่อทำให้ thread ทำงานต่อไป หรือทำให้ thread ตาย

    // constructor ของ Paddle
    public Paddle(int x, int y, int upKey, int downKey) {
        this.x = x;
        this.y = y;
        this.upKey = upKey;
        this.downKey = downKey;
    }

    // method วาด paddle
    public void draw(Graphics g) {
        g.fillRect(x, y, width, height);
    }

    // method ที่ควบคุมการเคลื่อนที่ของ paddle
    public void paddleMove() {
        y = y + yVelocity;

        if (y < 160) {y = 160;}
        if (y > 680) {y = 680;}
    }

    // method ที่เมื่อกดปุ่มลูกศร จะทำให้ทิศทางการเคลื่อนที่ของ paddle เปลี่ยนไป
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == upKey) {
            yVelocity = -speed;
        }
        else if (e.getKeyCode() == downKey) {
            yVelocity = speed;
        }
    }

    // method ที่เมื่อปล่อยปุ่ม จะทำให้ paddle อยู่กับที่
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == upKey || e.getKeyCode() == downKey) {
            yVelocity = 0;
        }
    }

    // method ที่จะคืนค่าขอบเขตของ paddle
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    // setter method ที่กำหนดค่าของ gameRunning
    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    @Override
    public void run() {
        // ถ้า gameRunning เป็น true, paddle จะสามารถขยับได้
        while (gameRunning) { 
            paddleMove();       // method บรรทัดที่ 224

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }
    }
}

// เป็น class ที่มีลักษณะ และการทำงานของ ball
class Ball implements Runnable {
    private int x, y;                       // ตำแหน่งของ ball
    private int size = 30;                  // ขนาดของ ball
    private int xVelocity = 5;              // ความเร็วแนวแกน x ของ ball
    private int yVelocity = 5;              // ความเร็วแนวแกน y ของ ball
    private int score1 = 0;                 // คะแนนของทีมที่ 1
    private int score2 = 0;                 // คะแนนของทีมที่ 2
    private boolean gameRunning = true;     // สร้าง flag เพื่อทำให้ thread ทำงานต่อไป หรือทำให้ thread ตาย
    private Paddle[] paddles;               // สร้าง array object ของ class Paddle

    // constructor ของ Ball
    public Ball(int x, int y, Paddle[] paddles) {
        this.x = x;
        this.y = y;
        this.paddles = paddles;
    }

    // method วาด ball และคะแนนของแต่ละทีม
    public void draw(Graphics g) {
        // วาด ball
        g.setColor(Color.white);
        g.fillOval(x, y, size, size);

        // วาดคะแนนของแต่ละทีม
        g.setColor(Color.black);
        g.setFont(new Font("Tahoma", Font.BOLD, 40));
        g.drawString(String.valueOf(score1), 387, 88);
        g.drawString(String.valueOf(score2), 1170, 88);
    }

    // method ที่ควบคุมลักษณะการเคลื่อนที่ของ ball
    public void ballMove() {
        // เคลื่อนที่เป็นแนวทะแยง
        x = x + xVelocity;
        y = y + yVelocity;

        // ถ้าชนขอบล่าง หรือขอบบน ให้เปลี่ยนทิศทาง
        if (y < 160 || y > 800) {
            if (y < 160) {y = 160;}
            else if (y > 800) {y = 800;}
            yVelocity = randomNumber();
        }

        // ถ้า ball ชนกับ paddle จะเปลี่ยนทิศทาง จึงต้องมีการรับค่าของ paddle เข้ามาด้วย
        for (int i = 0; i < paddles.length; i++) {
            if (getBounds().intersects(paddles[i].getBounds())) {
                xVelocity = -xVelocity;
                yVelocity = -yVelocity;
                break;
            }
        }

        scoring();      // method บรรทัดที่ 326
    }

    // method นับคะแนน เมื่อ ball อยู่ที่ตำแหน่ง x <= 20 หรือ x >= 1550
    public void scoring() {
        if (x <= 20) {
            score2++;
            resetBall();            // method บรรทัดที่ 341
        }
        else if (x >= 1550) {
            score1++;
            resetBall();
        }
    }

    // method ที่จะทำให้ลูกบอลกลับไปที่ตรงกลาง
    public void resetBall() {
        x = 785;
        y = 490;

        // ถ้าทิศทางของ ball ก่อนที่จะรีเซ็ตเป็นลบ ให้เริ่มเป็นบวก
        if (xVelocity < 0 ) {
            xVelocity = randomNumber();
            yVelocity = randomNumber();
        }
        else {
            xVelocity = randomNumber();
            yVelocity = randomNumber();
        }
    }

    // method ที่จะคืนค่าขอบเขตของ ball
    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    // method ที่จะคืนค่าคะแนนของทีมที่ 1
    public int getScore1() {
        return score1;
    }

    // method ที่จะคืนค่าคะแนนของทีมที่ 1
    public int getScore2() {
        return score2;
    }
    
    // setter method ที่กำหนดค่าของ gameRunning
    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    // method สุ่มตัวเลขและคืนค่าเป็นตัวเลข
    public int randomNumber() {
        Random random = new Random();
        int number = 0;
        boolean isNegative = random.nextBoolean();

        // ต้องการสุ่มเลขที่ไม่ใช่เลข 0
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

    @Override
    public void run() {
        // ถ้า gameRunning เป็น true, ball จะสามารถขยับได้
        while (gameRunning) {
            ballMove();         // method บรรทัดที่ 304

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {}
        }
    }
}