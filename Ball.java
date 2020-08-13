import java.awt.Graphics;
import java.awt.Point;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Color;


public class Ball {
    private Field field;
    public static final int ROW_SIZE = 2;
    public static final int COL_SIZE = 3;
    public static final int  BALL_SIZE = 60;
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int FALL = 3;
    private static boolean isReverse;
    private boolean isFixedFlag;
    private Point pos = new Point(0, 0);
    private int[][] block = new int[ROW_SIZE][COL_SIZE];
    public Ball(Field field) {
        this.field = field;
        isFixedFlag = false;
        init();
        isReverse = false;
    }


    public void init() {
        block = new int[ROW_SIZE][COL_SIZE];
        block[0][1] = 1;
        block[1][0] = 2;
        block[1][1] = 3;
        pos = new Point((BALL_SIZE) * (Field.COL / 2 - 1), BALL_SIZE);
    }


    public void remove() {
        block = new int[ROW_SIZE][COL_SIZE];
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
                        g.fillOval(pos.x + x * BALL_SIZE, pos.y + y * BALL_SIZE, BALL_SIZE, BALL_SIZE);  
                    } else {
                        g.fillOval(pos.x + x * BALL_SIZE + BALL_SIZE / 2, (pos.y) + y * BALL_SIZE, BALL_SIZE, BALL_SIZE);
                    }
                }
            }
        }
    }


    public void move(int dir) {
        Point newPos = new Point(0, 0);
        switch (dir) {
            case LEFT :
                newPos = new Point(pos.x - 15, pos.y);
                break;
            case RIGHT :
                newPos = new Point(pos.x + 15, pos.y);
                break;
            case DOWN :
                newPos = new Point(pos.x, pos.y + 30);
                break;
            default:
                break;
        }
        if (ballIsMovable(newPos, isReverse)) {
            pos = newPos;
        } else {
            if (dir == DOWN) {
                isFixedFlag = true;
            }
        }
    }

    public void autoMove(){
        Point newPos = new Point(pos.x, pos.y + 1);
        if (ballIsMovable(newPos, isReverse)) {
            pos = newPos;
        } else {
            isFixedFlag = true;
        }
    }

    public boolean ballIsMovable(Point newPos, boolean isReverse) {
        if ( isReverse ) {
            Point pos1 = new Point(newPos.x+3*BALL_SIZE/2, newPos.y+BALL_SIZE);
            if ( !field.isMovable(pos1) ) {
                return false;
            }
            pos1 = new Point(newPos.x+BALL_SIZE, newPos.y);
            if ( !field.isMovable(pos1) ) {
                return false;
            }
            pos1 = new Point(newPos.x+2*BALL_SIZE, newPos.y);
            if ( !field.isMovable(pos1) ) {
                return false;
            }

        } else {
            Point pos1 = new Point(newPos.x+BALL_SIZE/2, newPos.y+BALL_SIZE);
            if ( !field.isMovable(pos1) ) {
                return false;
            }
            pos1 = new Point(newPos.x+3*BALL_SIZE/2, newPos.y+BALL_SIZE);
            if ( !field.isMovable(pos1) ) {
                return false;
            }


        }
        return true;
    }


    public Point getPos() {
        return pos;
    }

    public boolean getIsReverse() {
        return isReverse;
    }

    public int[][] getBlock() {
        return block;
    }

    public void turn(int dir) {
        int[][] turnedBlock = new int[ROW_SIZE][COL_SIZE];
        switch (dir) {
            case LEFT :
            if (isReverse) {
                turnedBlock[1][0] = block[0][1];
                turnedBlock[0][1] = block[0][2];
                turnedBlock[1][1] = block[1][1];
            } else {
                turnedBlock[0][2] = block[1][1];
                turnedBlock[1][1] = block[1][0];
                turnedBlock[0][1] = block[0][1];
            }
            break;
            case RIGHT :
                if (isReverse) {
                    turnedBlock[1][0] = block[1][1];
                    turnedBlock[1][1] = block[0][2];
                    turnedBlock[0][1] = block[0][1];
                } else {
                    turnedBlock[0][2] = block[0][1];
                    turnedBlock[0][1] = block[1][0];
                    turnedBlock[1][1] = block[1][1];
                }
                break;
        }
        if (ballIsMovable(pos, ! isReverse)) {
            isReverse = ! isReverse;
            block = turnedBlock;
        }
    }


    public boolean isFixed() {
        return isFixedFlag;
    }
}