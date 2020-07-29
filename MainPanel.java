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
    }

    public void keyReleased(KeyEvent e) {
    }
}