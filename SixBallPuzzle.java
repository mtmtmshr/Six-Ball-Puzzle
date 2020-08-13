import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class SixBallPuzzle extends JFrame {
    public SixBallPuzzle() {
        // タイトルを設定
        setTitle("Six Ball Puzzle");
        // サイズ変更不可
        setResizable(false);
        
        Container contentPane = getContentPane();
        // メインパネルを作成してフレームに追加
        MainPanel mainPanel = new MainPanel();
        contentPane.add(mainPanel, BorderLayout.CENTER);
        mainPanel.requestFocus();
        pack();
        Thread gameThread = new Thread(mainPanel);
        gameThread.start();

        // パネルサイズに合わせてフレームサイズを自動設定
        
    }

    public static void main(String[] args) {
        SixBallPuzzle frame = new SixBallPuzzle();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
