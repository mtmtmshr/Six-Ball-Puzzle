import java.awt.Graphics;
import java.awt.Point;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Color;


public class Ball {
    private Field field;
    public static final int ROW_SIZE = 2;
    public static final int COL_SIZE = 3;
    public static final int  BALL_SIZE = 40;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int FALL = 3;
    private static boolean isReverse;
    private Point pos = new Point(0, 0);
    private int[][] block = new int[ROW_SIZE][COL_SIZE];
    public Ball(Field field) {
        this.field = field;
        init();
        isReverse = false;
    }
    public void init() {
        for (int i = 0; i < ROW_SIZE; i++) {
            for (int j = 0; j < COL_SIZE; j++) {
                block[i][j] = 0;
            }
        }
        block[0][1] = 1;
        block[1][0] = 2;
        block[1][1] = 3;
    }
    public void draw(Graphics g) {
        for (int y = 0; y < ROW_SIZE; y++) {
            for (int x = 0; x < COL_SIZE; x++) {
                if (block[y][x] != 0) {
                    if (block[y][x] == 1) {
                        g.setColor(Color.RED);
                    } else if (block[y][x] == 2) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.YELLOW);
                    }
                    if ( y % 2 == 0 ) {
                        g.fillOval((pos.x + x) * BALL_SIZE, (pos.y + y) * BALL_SIZE, BALL_SIZE, BALL_SIZE);  
                    } else {
                        g.fillOval((pos.x + x) * BALL_SIZE + BALL_SIZE / 2, (pos.y + y) * BALL_SIZE, BALL_SIZE, BALL_SIZE);
                    }
                }
            }
        }
    }

    public void move(int dir) {
        switch (dir) {
            case LEFT :
                Point newPos = new Point(pos.x - 1, pos.y);
                pos = newPos;
                break;
            case RIGHT :
                newPos = new Point(pos.x + 1, pos.y);
                pos = newPos;
                break;
            case DOWN :
                newPos = new Point(pos.x, pos.y + 1);
                pos = newPos;
                break;
        }
    }

    public void turn(int dir) {
        switch (dir) {
            case LEFT :
            if (isReverse) {
                block[1][0] = block[0][1];
                block[0][1] = block[0][2];
                block[0][2] = 0;
            } else {
                block[0][2] = block[1][1];
                block[1][1] = block[1][0];
                block[1][0] = 0;
            }
            break;
            case RIGHT :
                if (isReverse) {
                    block[1][0] = block[1][1];
                    block[1][1] = block[0][2];
                    block[0][2] = 0;
                } else {
                    block[0][2] = block[0][1];
                    block[0][1] = block[1][0];
                    block[1][0] = 0;
                }
                break;
        }
        isReverse = ! isReverse;
    }
}