import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Image;

public class Field {
    public static final int COL = 12;
    public static final int  ROW = 13;
    public static final int  BALL_SIZE = 40;
    private int[][] field;
    public Field() {
        field = new int[ROW][COL];
        // フィールドを初期化
        init(field);
    }

    public void init(int[][] field) {
        for (int y = 0; y < ROW; y++) {
            for (int x = 0; x < COL; x++) {
                // 壁をつくる
                if (x == 0 || x == COL - 1) {
                    field[y][x] = 1;
                } else if (y == ROW - 1) {
                    field[y][x] = 1;
                } else {
                    field[y][x] = 0;
                }
            }
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        for (int y = 0; y < ROW-1; y++) {
            for (int x = 1; x < COL-1; x++) {
                if (field[y][x] != 0) {
                    if (field[y][x] == 1) {
                        g.setColor(Color.RED);
                    } else if (field[y][x] == 2) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.YELLOW);
                    }
                    if ( y % 2 == 1 ) {
                        g.fillOval(x * BALL_SIZE, y * BALL_SIZE, BALL_SIZE, BALL_SIZE);  
                    } else {
                        if (x == COL-2) {
                            break;
                        }
                        g.fillOval(x * BALL_SIZE + BALL_SIZE / 2, y * BALL_SIZE, BALL_SIZE, BALL_SIZE);
                    }
                }
            }
        }
    }
}