
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameClient extends JFrame {
    // Attribute GameClient
    boolean gameRunning = true;

    // Object from Class
    GamePanel gamePanel = new GamePanel();
    Paddle[] paddles = new Paddle[4];
    Ball ball = new Ball(paddles);

    // Object from JAVA Socket
    Socket socket;
    PrintWriter out;
    BufferedReader in;
    
    public GameClient() {
        setTitle("Pong Client");
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setFocusable(true);
        add(gamePanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        GameClient gameClient = new GameClient();
        gameClient.startClient();
    }

    public void startClient() {
        try {
            socket = new Socket("192.168.1.7", 50101);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Thread receiveThread = new Thread(new GameStateReceiver());
            receiveThread.start();

            gamePanel.addKeyListener(new KeyAdapter() {

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
        if (ball.getScore1() >= gamePanel.getWinningScore()) {
            gameRunning = false;
            // แสดงหน้าว่าทีมที่ 1 ชนะ
            JOptionPane.showMessageDialog(this, "Team 1 wins!");
        } 
        else if (ball.getScore1() >= gamePanel.getWinningScore()) {
            gameRunning = false;
            // แสดงหน้าว่าทีมที่ 2 ชนะ
            JOptionPane.showMessageDialog(this, "Team 2 wins!");
        }
    }

    class GameStateReceiver implements Runnable {

        @Override
        public void run() {
            while (gameRunning) {
                String gameState;

                try {
                    while ((gameState = in.readLine()) != null) {
                        String[] tokens = gameState.split(",");

                        synchronized (gamePanel) {
                            gamePanel.setPlayer1_y(Integer.parseInt(tokens[0]));
                            gamePanel.setPlayer2_y(Integer.parseInt(tokens[1]));
                            gamePanel.setPlayer3_y(Integer.parseInt(tokens[2]));
                            gamePanel.setPlayer4_y(Integer.parseInt(tokens[3]));
                            gamePanel.setBall_x(Integer.parseInt(tokens[4]));
                            gamePanel.setBall_y(Integer.parseInt(tokens[4]));
                            gamePanel.setScore1(Integer.parseInt(tokens[6]));
                            gamePanel.setScore2(Integer.parseInt(tokens[7]));
                        }

                        gamePanel.repaint();
                        checkWinningCondition();
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            } 
        }
    }

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
}