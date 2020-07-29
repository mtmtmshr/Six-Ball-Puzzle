import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.io.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

import java.awt.*;

public class MainPanel extends JPanel implements KeyListener, Runnable {
    private static int WIDTH = 500;
    private static int HEIGHT = 1000;
    private Field field;
    private Ball ball;

    public MainPanel() {
        // パネルの推奨サイズを設定、pack()するときに必要
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        addKeyListener(this);
        field = new Field();
        ball = new Ball(field);
    }

    public void paintComponent(Graphics g) {
        field.draw(g);
        ball.draw(g);
    }
    public void run() {
    }
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP) {
            ball.move(Ball.FALL);
        } else if (key == KeyEvent.VK_DOWN) {
            ball.move(Ball.DOWN);
        } else if (key == KeyEvent.VK_RIGHT) {
            ball.move(Ball.RIGHT);
        } else if (key == KeyEvent.VK_LEFT) {
            ball.move(Ball.LEFT);
        } else if (key == KeyEvent.VK_S) {
            ball.turn(Ball.RIGHT);
        } else if (key == KeyEvent.VK_A) {
            ball.turn(Ball.LEFT);
        }
        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }
}