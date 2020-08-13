import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.io.*;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;


public class MainPanel extends JPanel implements KeyListener, Runnable {
    public static int WIDTH = Ball.BALL_SIZE * Field.COL;
    public static int HEIGHT = Ball.BALL_SIZE * Field.ROW;
    private Field field;
    private Ball ball;


    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
    }


    public void paintComponent(Graphics g) {
        field.draw(g);
        ball.draw(g);
    }


    public void threadSleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void fixNewBlock(){
        Point pos = ball.getPos();
        int [][] block = ball.getBlock();
        if ( !ball.getIsReverse() ) {
            // 左下
            Point matrixPos = field.pointToMatrix(new Point(pos.x+Field.BALL_SIZE/2, pos.y+Field.BALL_SIZE));
            field.fixBlock(matrixPos, block[1][0]);
            // 上
            matrixPos = field.pointToMatrix(new Point(pos.x+Field.BALL_SIZE, pos.y));
            field.fixBlock(matrixPos, block[0][1]);
            // 右下
            matrixPos = field.pointToMatrix(new Point(pos.x+Field.BALL_SIZE*3/2, pos.y+Field.BALL_SIZE));
            field.fixBlock(matrixPos, block[1][1]);
        } else {
            // 下
            Point matrixPos = field.pointToMatrix(new Point(pos.x+Field.BALL_SIZE*3/2, pos.y+Field.BALL_SIZE));
            if ( matrixPos.y % 2 == 0 && matrixPos.x == 1 ) {
                matrixPos.x ++;
            }
            field.fixBlock(matrixPos, block[1][1]);
            // 左上
            matrixPos = field.pointToMatrix(new Point(pos.x+Field.BALL_SIZE, pos.y));
            if ( matrixPos.y % 2 == 1 && matrixPos.x == 0 ) {
                matrixPos.x++;
            }
            field.fixBlock(matrixPos, block[0][1]);
            
            //　右上
            matrixPos = field.pointToMatrix(new Point(pos.x+Field.BALL_SIZE*2, pos.y));
            if ( matrixPos.y % 2 == 1 && matrixPos.x == 1 ) {
                matrixPos.x++;
            }
            field.fixBlock(matrixPos, block[0][2]);
            
        }
    }


    public void run() {
        field = new Field();
        ball = new Ball(field);
        while (true) {
            addKeyListener(this);
            while ( ! ball.isFixed() ) {
                threadSleep(100);
                repaint();
                ball.autoMove();
            }
            removeKeyListener(this);
            threadSleep(100);
            fixNewBlock();
            ball.remove();
            repaint();
            while (field.fall()) {
                Graphics g = this.getGraphics();
                field.draw(g);
                threadSleep(100);
            }
            field.fillFieldPoint();
            field.setPanel(this);
            while ( true ) {
                Thread vanishThread = new Thread(field);
                vanishThread.start();
                while ( vanishThread.isAlive() ) {}
                if ( ! field.fall() ) {
                    break;
                } else {
                    repaint();
                    threadSleep(200);
                }
                while (field.fall()) {
                    repaint();
                    threadSleep(200);
                }
                field.fillFieldPoint();
            }
            ball = new Ball(field);
        }
    }


    public void keyTyped(KeyEvent e) {
    }


    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_DOWN) {
            ball.move(Ball.DOWN);
        } else if (key == KeyEvent.VK_RIGHT) {
            ball.move(Ball.RIGHT);
        } else if (key == KeyEvent.VK_LEFT) {
            ball.move(Ball.LEFT);
        } else if (key == KeyEvent.VK_S) {
            ball.turn(Ball.RIGHT);
        } else if (key == KeyEvent.VK_A) {
            ball.turn(Ball.LEFT);
        } else if (key == KeyEvent.VK_UP) {
            ball.move(Ball.FALL);
        }
        repaint();
    }


    public void keyReleased(KeyEvent e) {
    }
}