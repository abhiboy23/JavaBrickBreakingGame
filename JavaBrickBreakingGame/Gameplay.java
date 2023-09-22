package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballPosX = 310;
    private int ballPosY = 450;
    private int ballXdir = -3;
    private int ballYdir = -4;
    private MapGenerator map;

    public Gameplay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // background
        g.setColor(Color.black);
        g.fillRect(1, 1, 680, 592);

        // drawing map
        map.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 5, 592);
        g.fillRect(0, 0, 692, 5);
        g.fillRect(679, 0, 5, 592);

        // Scores
        g.setColor(Color.red);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString(" " + score, 590, 30);

        // The Paddle
        g.setColor(Color.CYAN);
        g.fillRoundRect(playerX, 545, 105, 8, 8, 8);

        // The Ball
        g.setColor(Color.WHITE);
        g.fillOval(ballPosX, ballPosY, 20, 20);
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("YOU WIN !! ", 190, 300);
            g.drawString("Score = " + score, 160, 250);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to restart ", 230, 350);
        }
        if (ballPosY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over ,Score : " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to restart ", 230, 350);
        }
        g.dispose();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 578) {
                playerX = 578;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 6) {
                playerX = 6;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballXdir = -3;
                ballYdir = -4;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 30;
    }

    public void moveLeft() {
        play = true;
        playerX -= 30;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550,
                    100, 8))) {
                ballYdir = -ballYdir;
            }
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;
                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle brickRect = rect;
                        if (ballRect.intersects(brickRect)) {
                            map.setBrickvalue(0, i, j);
                            totalBricks--;
                            score += 5;
                            if (ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x +
                                    brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballPosX += ballXdir;
            ballPosY += ballYdir;
            if (ballPosX < 5) {
                ballXdir = -ballXdir;
            }
            if (ballPosY < 5) {
                ballYdir = -ballYdir;
            }
            if (ballPosX > 660) {
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }
}