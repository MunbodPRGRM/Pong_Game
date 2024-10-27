
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;

public class GameServer extends JFrame {
    // Attribute GameServer
    int max_clients = 4;
    int count_clients = 0;
    int player1_y = 410;
    int player2_y = 410;
    int player3_y = 410;
    int player4_y = 410;
    boolean gameRunning = true;

    // Object from Class
    GamePanel gamePanel = new GamePanel();
    Paddle[] paddles = new Paddle[4];
    Ball ball = new Ball(paddles);

    // Object from JAVA Socket
    Socket[] clients = new Socket[max_clients];

    public GameServer() {
        setTitle("Pong Server");
        setSize(1600, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(gamePanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        gameServer.startServer();
    }

    public void startServer() {
        try {
            ServerSocket serverSocket = new ServerSocket(50101);
            System.out.println("Server Started. Waiting for clients...");

            while (gameRunning) {
                if (count_clients >= 1) {
                    updateGame();
                }

                broadcastGameState();
                gamePanel.repaint();
                Thread.sleep(10);
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        } 
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void updateGame() {
        ball.ballMove();
        checkWinningCondition();
    }

    public void checkWinningCondition() {
        if (ball.getScore1() >= gamePanel.getWinningScore()) {
            gameRunning = false;
        } 
        else if (ball.getScore2() >= gamePanel.getWinningScore()) {
            gameRunning = false;
        }
    }

    public void broadcastGameState() {
        synchronized (clients) {
            String gameState = player1_y + "," + player2_y + "," + player3_y + "," + player4_y + "," 
            + ball.getBall_x() + "," + ball.getBall_y() + "," + ball.getScore1() + "," + ball.getScore2();

            for (int i = 0; i < count_clients; i++) {
                try {
                    PrintWriter out = new PrintWriter(clients[i].getOutputStream(), true);
                    out.println(gameState);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isGameRunning() {
        return gameRunning;
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

    class ClientAcceptor implements Runnable {
        ServerSocket serverSocket;

        public ClientAcceptor(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            while (gameRunning) { 
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("Client Connected : " + socket.getInetAddress());

                    synchronized (clients) {
                        if (count_clients < max_clients) {
                            clients[count_clients++] = socket;

                            Thread clientHandlerThread = new Thread(new ClientHandler(socket));
                            clientHandlerThread.start();
                        } 
                        else {
                            socket.close();
                        }
                    }
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class ClientHandler implements Runnable {
        Socket client;

        public ClientHandler(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                String input;

                while (gameRunning) {
                    while ((input = in.readLine()) != null) {
                        synchronized (gamePanel) {
                            if (client == clients[0]) {
                                if (input.equals("UP") && player1_y > 160) {
                                    player1_y = paddles[0].paddleMoveUp();
                                }

                                if (input.equals("DOWN") && player1_y < 680) {
                                    player1_y = paddles[0].paddleMoveDown();
                                }
                            }
                            else if (client == clients[1]) {
                                if (input.equals("UP") && player2_y > 160) {
                                    player1_y = paddles[1].paddleMoveUp();
                                }

                                if (input.equals("DOWN") && player2_y < 680) {
                                    player2_y = paddles[1].paddleMoveDown();
                                }
                            }
                            else if (client == clients[2]) {
                                if (input.equals("UP") && player2_y > 160) {
                                    player1_y = paddles[2].paddleMoveUp();
                                }

                                if (input.equals("DOWN") && player2_y < 680) {
                                    player2_y = paddles[2].paddleMoveDown();
                                }
                            }
                            else if (client == clients[3]) {
                                if (input.equals("UP") && player2_y > 160) {
                                    player1_y = paddles[3].paddleMoveUp();
                                }

                                if (input.equals("DOWN") && player2_y < 680) {
                                    player2_y = paddles[3].paddleMoveDown();
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}