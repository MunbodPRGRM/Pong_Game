
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PongClient extends JFrame {
    // Attribute PongClient
    int player1_y = 410;
    int player2_y = 410;
    int player3_y = 410;
    int player4_y = 410;

    // Attribute PongPanel
    int winningScore = 10;
    boolean gameRunning = true;

    // Attribute Ball
    int ball_x = 785;
    int ball_y = 490;
    int ball_size = 30;
    int ball_speedX = 5;
    int ball_speedY = 5;
    int score1 = 0;
    int score2 = 0;

    // Attribute Paddle
    int paddle_x;
    int paddle_y;
    int paddle_height = 150;
    int paddle_width = 20;

    // Object from Class
    PongPanel clientPanel = new PongPanel();
    Paddle[] paddles = new Paddle[4];
    Ball ball = new Ball();

    // Object from JAVA Socket
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    // PongClient Constructor
    public PongClient() {
        setTitle("Pong Client");
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        clientPanel.setFocusable(true);
        add(clientPanel);

        setVisible(true);
    }

    // Main Method || class PongClient
    public static void main(String[] args) {
        PongClient pongClient = new PongClient();
        pongClient.startClient();
    }

    public void startClient() {
        try {
            socket = new Socket("192.168.1.7", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread receiveThread = new Thread(new GameStateReceiver());
            receiveThread.start();

            clientPanel.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    Thread keyPressThread = new Thread(new KeyPressHandler(e));
                    keyPressThread.start();
                }
            });
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void checkWinningCondition() {
        if (score1 >= winningScore) {
            gameRunning = false;
            // แสดงหน้าว่าทีมที่ 1 ชนะ
            JOptionPane.showMessageDialog(this, "Team 1 wins!");
        } 
        else if (score2 >= winningScore) {
            gameRunning = false;
            // แสดงหน้าว่าทีมที่ 2 ชนะ
            JOptionPane.showMessageDialog(this, "Team 2 wins!");
        }
    }

    // Inner class GameStateReciver
    class GameStateReceiver implements Runnable {

        @Override
        public void run() {
            while (gameRunning) {
                String gameState;

                try {
                    while ((gameState = in.readLine()) != null) {
                        String[] tokens = gameState.split(",");

                        synchronized (PongClient.this) {
                            player1_y = Integer.parseInt(tokens[0]);
                            player2_y = Integer.parseInt(tokens[1]);
                            player3_y = Integer.parseInt(tokens[2]);
                            player4_y = Integer.parseInt(tokens[3]);
                            ball_x = Integer.parseInt(tokens[4]);
                            ball_y = Integer.parseInt(tokens[5]);
                            score1 = Integer.parseInt(tokens[6]);
                            score2 = Integer.parseInt(tokens[7]);
                        }

                        clientPanel.repaint();
                        checkWinningCondition();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            } 
        }
    }

    // Inner class KeyPressHandler
    class KeyPressHandler implements Runnable {
        KeyEvent e;

        public KeyPressHandler(KeyEvent e) {
            this.e = e;
        }

        @Override
        public void run() {
            try {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    out.println("UP");
                }
                else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    out.println("DOWN");
                }
            } 
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Inner class PongPanel
    class PongPanel extends JPanel {
        public PongPanel() {
            setBackground(Color.black);
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
            paddles[0] = new Paddle(110, player1_y, paddle_width, paddle_height);
            paddles[1] = new Paddle(1450, player2_y, paddle_width, paddle_height);
            paddles[2] = new Paddle(310, player3_y, paddle_width, paddle_height);
            paddles[3] = new Paddle(1260, player4_y, paddle_width, paddle_height);

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
    }

    // Inner class Ball
    class Ball {
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
                if (ball.getBounds().intersects(paddles[i].getBounds())) {
                    ball_speedX = -ball_speedX;
                    ball_speedY = -ball_speedY;
                    break;
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
    }

    // Inner class Paddle
    class Paddle {
        int x, y, width, height;

        public Paddle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void draw(Graphics g) {
            g.fillRect(x, y, width, height);
        }

        public Rectangle getBounds() {
            return new Rectangle(paddle_x, paddle_y, paddle_width, paddle_height);
        }
    }
}
