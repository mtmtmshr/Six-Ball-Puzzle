import java.awt.Graphics;
import java.awt.Point;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Ball {
    private Field field;
    public static final int ROW_SIZE = 2;
    public static final int COL_SIZE = 3;
    public static final int  BALL_SIZE = 40;
    private int[][] block = new int[ROW_SIZE][COL_SIZE];
    public Ball(Field field) {
        this.field = field;
        init();
    }
    public void init() {
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++) {
                block[i][j] = 0;
            }
        }
        block[0][1] = 1;
        block[1][0] = 1;
        block[1][1] = 1;
    }
    public void draw(Graphics g) {
        for (int y = 0; y < ROW_SIZE; y++) {
            for (int x = 0; x < COL_SIZE; x++) {
                if (block[y][x] != 0) {
                    if ( y % 2 == 0 ) {
                        g.fillOval(x * BALL_SIZE, y * BALL_SIZE, BALL_SIZE, BALL_SIZE);  
                    } else {
                        g.fillOval(x * BALL_SIZE + BALL_SIZE / 2, y * BALL_SIZE, BALL_SIZE, BALL_SIZE);
                    }
                }
            }
        }
    }
}