import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JPanel;


public class Field extends JPanel implements Runnable {
    public static final int COL = 12;
    public static final int ROW = 14;
    public static final int BALL_SIZE = Ball.BALL_SIZE;
    private int[][] fieldMatrix;
    private int[][] fieldPoint;
    private JPanel panel;
    public Field() {
        fieldMatrix = new int[ROW][COL];
        fieldPoint = new int[ROW * BALL_SIZE][COL * BALL_SIZE];
        // フィールドを初期化
        init(fieldMatrix);
    }

    public void init(int[][] fieldMatrix) {
        for (int y = 0; y < ROW; y++) {
            for (int x = 0; x < COL; x++) {
                // 壁をつくる
                if (x == 0 || x == COL - 1) {
                    fieldMatrix[y][x] = 1;
                } else if (y == ROW - 1) {
                    fieldMatrix[y][x] = 1;
                } else {
                    fieldMatrix[y][x] = 0;
                }
            }
        }
    }

    public void draw(Graphics g) {
        
        for (int y = 1; y < ROW - 1; y++) {
            for (int x = 1; x < COL - 1; x++) {
                if (fieldMatrix[y][x] != 0) {
                    if (fieldMatrix[y][x] == 1) {
                        g.setColor(Color.RED);
                    } else if (fieldMatrix[y][x] == 2) {
                        g.setColor(Color.BLUE);
                    } else {
                        g.setColor(Color.YELLOW);
                    }
                    
                } else {
                    g.setColor(Color.WHITE);
                }
                if (y % 2 == 0) {
                    g.fillOval(x * BALL_SIZE, y * BALL_SIZE, BALL_SIZE, BALL_SIZE);
                } else {
                    if (x == COL - 2) {
                        break;
                    }
                    g.fillOval(x * BALL_SIZE + BALL_SIZE / 2, y * BALL_SIZE, BALL_SIZE, BALL_SIZE);
                }
            }
        }
        g.setColor(Color.MAGENTA);
        g.drawLine(BALL_SIZE, BALL_SIZE, BALL_SIZE, BALL_SIZE * (ROW - 1));
        g.drawLine(BALL_SIZE * (COL - 1), BALL_SIZE, BALL_SIZE * (COL - 1), BALL_SIZE * (ROW - 1));
        g.drawLine(BALL_SIZE, BALL_SIZE * (ROW - 1), BALL_SIZE * (COL - 1), BALL_SIZE * (ROW - 1));
    }

    public boolean isMovable(Point newPos) {
        if (newPos.x < BALL_SIZE || newPos.x > BALL_SIZE * (COL - 2)) {
            return false;
        }
        if (newPos.y > (ROW - 2) * BALL_SIZE) {
            return false;
        }
        if (fieldPoint[newPos.y + BALL_SIZE][newPos.x] == 1) {
            return false;
        }
        if (fieldPoint[newPos.y + BALL_SIZE][newPos.x + BALL_SIZE / 2] == 1) {
            return false;
        }
        return true;
    }

    public void fixBlock(Point pos, int blockColor) {
        fieldMatrix[pos.y][pos.x] = blockColor;
        fillFieldPoint();
    }

    public void fillFieldPoint() {
        fieldPoint = new int[ROW * BALL_SIZE][COL * BALL_SIZE];
        for (int y = 1; y < ROW - 1; y++) {
            for (int x = 1; x < COL - 1; x++) {
                if (fieldMatrix[y][x] != 0) {
                    int k = 0;
                    if (y % 2 == 1) {
                        k = 1;
                    }
                    for (int i = 0; i < BALL_SIZE; i++) {
                        for (int j = 0; j < BALL_SIZE; j++) {
                            fieldPoint[y * BALL_SIZE + i][x * BALL_SIZE + j + k * BALL_SIZE / 2] = 1;
                        }
                    }
                }
            }
        }
    }

    public boolean fall() {
        int x, y, iy, ix;
        // まだ落とせるか
        boolean flag = false;
        int ckd[] = new int[COL + (ROW - 2 - 2) / 2];
        for (x = 1; x < COL - 1; x++) {
            // 一番下は壁なのでROW-2から。
            for (y = ROW - 2; y >= 0; y--) {
                int row = x - (ROW - 2 - y) / 2;
                if (row <= -1) {
                    row = row * -1 + COL - 1;
                }
                if (ckd[row] == 1) {
                    break;
                }
                if (fieldMatrix[y][x] == 0) {
                    ix = x;
                    if (y % 2 != 0) {
                        ix += 1;
                    }
                    // 空の場所の読み飛ばし
                    for (iy = y - 1; iy >= 0 && fieldMatrix[iy][ix] == 0; iy--) {
                        if (iy % 2 != 0) {
                            ix += 1;
                        }
                    }
                    if (ix >= COL - 1 || iy < 0) {
                        break;
                    }
                    // まだ落とせる！！
                    ix = x;
                    flag = true;
                    for (iy = y; iy >= 0; iy--) {
                        // 下に空白がある文字を1マスずつ落とす
                        if (iy - 1 >= 0) {
                            if (iy % 2 != 0) {
                                fieldMatrix[iy][ix] = fieldMatrix[iy - 1][ix + 1];
                                ix += 1;
                            } else {
                                fieldMatrix[iy][ix] = fieldMatrix[iy - 1][ix];
                            }
                        } else {
                            fieldMatrix[iy][ix] = 0;
                        }
                        if (ix >= COL - 1) {
                            fieldMatrix[iy][ix - 1] = 0;
                            break;
                        }
                    }
                    ckd[row] = 1;
                    break;
                }
            }
        }
        ckd = new int[COL + (ROW - 2 - 2) / 2];
        for (x = COL - 2; x >= 1; x--) {
            // 一番下は壁なのでROW-2から。
            for (y = ROW - 2; y >= 0; y--) {
                int row = x + (ROW - 1 - y) / 2;
                if (ckd[row] == 1) {
                    break;
                }
                if (x == COL - 2 && y % 2 == 1) {
                    continue;
                }
                if (fieldMatrix[y][x] == 0) {
                    ix = x;
                    if (y % 2 == 0) {
                        ix -= 1;
                    }
                    // 空の場所の読み飛ばし
                    for (iy = y - 1; iy >= 0 && fieldMatrix[iy][ix] == 0; iy--) {
                        if (iy % 2 == 0) {
                            ix -= 1;
                        }
                    }
                    if (ix <= 0 || iy < 0) {
                        break;
                    }
                    // まだ落とせる！！
                    ix = x;
                    flag = true;
                    for (iy = y; iy >= 0; iy--) {

                        // 下に空白がある文字を1マスずつ落とす
                        if (iy - 1 >= 0) {
                            if (iy % 2 == 0) {
                                fieldMatrix[iy][ix] = fieldMatrix[iy - 1][ix - 1];
                                ix -= 1;
                            } else {
                                fieldMatrix[iy][ix] = fieldMatrix[iy - 1][ix];
                            }
                        } else {
                            fieldMatrix[iy][ix] = 0;
                        }
                        if (ix <= 0) {
                            fieldMatrix[iy][ix + 1] = 0;
                            break;
                        }
                    }
                    ckd[row] = 1;
                    break;
                }

            }
        }
        return flag;
    }

    public Point pointToMatrix(Point pos) {
        int row = pos.y / BALL_SIZE + 1;
        if (pos.y % BALL_SIZE == 0) {
            row -= 1;
        }
        int col = 0;
        if (row % 2 == 0) {// 偶数列
            col = (pos.x + BALL_SIZE / 2) / BALL_SIZE;
            if ((pos.x + BALL_SIZE / 2) % BALL_SIZE == 0) {
                col -= 1;
            }
        } else {// 奇数列
            col = pos.x / BALL_SIZE;
            if (pos.x % BALL_SIZE == 0) {
                col -= 1;
            }
        }
        Point matrixPos = new Point(col, row);
        return matrixPos;
    }

    public void paintComponent(Graphics g, boolean straightFlag) {
        draw(g);
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public void run() {
        boolean[][] ckd = new boolean[ROW][COL];
        boolean[][] prevCkd = new boolean[ROW][COL];
        for (int i = 0; i < ROW - 1; i++) {
            for (int j = 1; j < COL - 1; j++) {
                if (ckd[i][j]) {
                    continue;
                }
                int color = fieldMatrix[i][j];
                ckd[i][j] = true;
                if (color == 0) {
                    copyArrays(prevCkd, ckd);
                    continue;
                }
                int count = check_round(j, i, color, ckd);
                count += 1; // 自分をたす
                int[][] vanishMatrix = new int[ROW][COL];
                if (count >= 6) {
                    for (int iy = 0; iy < ROW - 1; iy++) {
                        for (int jx = 1; jx < COL - 1; jx++) {
                            if (prevCkd[iy][jx] != ckd[iy][jx]) {
                                vanishMatrix[iy][jx] = 1;
                            }
                        }
                    }
                    Graphics g = panel.getGraphics();
                    if ( isStraight(vanishMatrix) ) {
                        
                        g.drawString("ストレート", 500, 600);
                    }
                    if ( isPyramid(vanishMatrix) ) {
                        
                        g.drawString("ピラミッド", 500, 500);
                    }
                    if ( isHexagon(vanishMatrix) ) {
                        g.drawString("ヘキサゴン", 500, 400);
                    }
                    for (int iy = 0; iy < ROW - 1; iy++) {
                        for (int jx = 1; jx < COL - 1; jx++) {
                            if ( vanishMatrix[iy][jx] == 1 ) {
                                fieldMatrix[iy][jx] = 0;
                            }
                        }
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    draw(g);
                }
                copyArrays(prevCkd, ckd);
            }
        }
    }

    public boolean isStraight(int [][] matrix) {
        for (int i = ROW-2; i >= 0; i--) {
            for (int j = 1; j < COL - 1; j++) {
                boolean[][] ckd = new boolean[ROW][COL];
                ckd[i][j] = true;
                if ( matrix[i][j] == 1 ) {
                    if ( checkRowStraight(matrix, j, i, ckd) >= 5) {
                        return true;
                    }
                    
                    ckd = new boolean[ROW][COL];
                    ckd[i][j] = true;
                    if ( checkSlantedStraight1(matrix, j, i, ckd) >= 5) {
                        return true;
                    }
                    ckd = new boolean[ROW][COL];
                    ckd[i][j] = true;
                    if ( checkSlantedStraight2(matrix, j, i, ckd) >= 5) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isPyramid(int [][] matrix) {
        for (int i = ROW-2; i >= 0; i--) {
            for (int j = 1; j < COL - 1; j++) {
                boolean[][] ckd = new boolean[ROW][COL];
                ckd[i][j] = true;
                if ( matrix[i][j] == 1 ) {
                    if ( checkPyramid１(matrix, j, i, ckd) ) {
                        return true;
                    }
                    ckd = new boolean[ROW][COL];
                    ckd[i][j] = true;
                    if ( checkPyramid2(matrix, j, i, ckd) ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public boolean isHexagon(int [][] matrix) {
        for (int i = ROW-2; i >= 0; i--) {
            for (int j = 1; j < COL - 1; j++) {
                boolean[][] ckd = new boolean[ROW][COL];
                ckd[i][j] = true;
                if ( matrix[i][j] == 1 ) {
                    if ( checkHexagon(matrix, j, i, ckd) ) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public boolean checkHexagon(int [][] matrix, int x, int y, boolean[][] ckd) {
        if ( x >= COL-3 ) { return false; }
        if ( y + 2 >= ROW-1 ) { return false; }
        if ( y % 2 == 0 && x <= 1 ) { return false; }
        if ( matrix[y][x+1] == 0) {
            return false;
        }
        y++;
        if ( y % 2 == 0 ) {
            if ( matrix[y][x] == 0 || matrix[y][x+2] == 0) {
                return false;
            }
        } else {
            if ( matrix[y][x-1] == 0 || matrix[y][x+1] == 0) {
                return false;
            }
        }
        y++;
        if ( matrix[y][x] == 0 || matrix[y][x+1] == 0) {
            return false;
        }

        return true;
    }


    public int  checkRowStraight(int [][] matrix, int x, int y, boolean[][] ckd) {
        int count = 0;
        if ( x <= 0 || x >= COL-1 ) { return count; }
        if ( y < 0 || y >= ROW-1 ) { return count; }
        if ( x > 1 ) {
            if ( ! ckd[y][x-1] && matrix[y][x-1] == 1 ) {
                ckd[y][x-1] = true;
                count++;
                count += checkRowStraight(matrix, x-1, y, ckd);
            }
        }

        if ( x < COL-1 ) {
            if ( y % 2 != 1 || x < COL-2 ) {
                if ( ! ckd[y][x+1] && matrix[y][x+1] == 1 ) {
                    ckd[y][x+1] = true;
                    count++;
                    count += checkRowStraight(matrix, x+1, y, ckd);
                }
            }
        }
        return count;
    }


    public boolean checkPyramid１(int [][] matrix, int x, int y, boolean[][] ckd) {
        if ( x <= 1 ) { return false; }
        if ( y + 2 >= ROW-1 ) { return false; }
        if ( y % 2 == 0 ) {
            if ( x >= COL-2 ) {
                return false;
            }
        } else {
            if ( x >= COL-3 ) {
                return false;
            }
        }
        y++;
        if ( y % 2 == 0 ) {
            if ( matrix[y][x] == 0 || matrix[y][x+1] == 0) {
                return false;
            }
        } else {
            if ( matrix[y][x] == 0 || matrix[y][x-1] == 0) {
                return false;
            }
        }
        y++;
        if ( y % 2 == 0 ) {
            if ( matrix[y][x-1] == 0 || matrix[y][x] == 0 || matrix[y][x+1] == 0) {
                return false;
            }
        } else {
            if ( matrix[y][x] == 0 || matrix[y][x-1] == 0 || matrix[y][x+1] == 0  ) {
                return false;
            }
        }
        
        return true;
    }


    public boolean checkPyramid2(int [][] matrix, int x, int y, boolean[][] ckd) {
        if ( x >= COL-3 ) { return false; }
        if ( y % 2 == 1 && x >= COL-4 ) { return false; }
        if ( y + 2 >= ROW-1 ) { return false; }
        if ( y % 2 == 0 ) {
            if ( x >= COL-2 ) {
                return false;
            }
        } else {
            if ( x >= COL-3 ) {
                return false;
            }
        }
        if ( matrix[y][x+1] == 0 || matrix[y][x+2] == 0) {
            return false;
        }
        y++;
        if ( y % 2 == 0 ) {
            if ( matrix[y][x+2] == 0 || matrix[y][x+1] == 0) {
                return false;
            }
        } else {
            if ( matrix[y][x] == 0 || matrix[y][x+1] == 0) {
                return false;
            }
        }
        y++;
        if ( matrix[y][x+1] == 0) {
            return false;
        }
        
        return true;
    }


    public int checkSlantedStraight1(int [][] matrix, int x, int y, boolean[][] ckd) {
        int count = 0;
        if ( x <= 0 || x >= COL-1 ) { return count; }
        if ( y < 0 || y >= ROW-1 ) { return count; }
        if ( matrix[y][x] != 1 ) {
            return count;
        }
        if (y-1 >= 0) {
            if ( y % 2 == 0 ) {
                if ( !ckd[y-1][x] && matrix[y-1][x] == 1) {
                    ckd[y-1][x] = true;
                    count++;
                    count += checkSlantedStraight1(matrix, x, y-1, ckd);
                }
            } else {
                if ( !ckd[y-1][x+1] && matrix[y-1][x+1] == 1) {
                    ckd[y-1][x+1] = true;
                    count++;
                    count += checkSlantedStraight1(matrix, x+1, y-1, ckd);
                }
            }
        }
        return count;
    }


    public int checkSlantedStraight2(int [][] matrix, int x, int y, boolean[][] ckd) {
        int count = 0;
        if ( x <= 0 || x >= COL-1 ) { return count; }
        if ( y < 0 || y >= ROW-1 ) { return count; }
        if ( matrix[y][x] != 1 ) {
            return count;
        }
        if (y-1 >= 0) {
            if ( y % 2 == 0 ) {
                if ( !ckd[y-1][x-1] && matrix[y-1][x-1] == 1) {
                    ckd[y-1][x-1] = true;
                    count++;
                    count += checkSlantedStraight2(matrix, x-1, y-1, ckd);
                }
            } else {
                if ( !ckd[y-1][x] && matrix[y-1][x] == 1) {
                    ckd[y-1][x] = true;
                    count++;
                    count += checkSlantedStraight2(matrix, x, y-1, ckd);
                }
            }
        }
        return count;
    }



    public void copyArrays(boolean[][] a1, boolean[][] a2) {
        for ( int i = 0; i < ROW-1; i++ ) {
            for ( int j = 1; j < COL-1; j++ ) {
                a1[i][j] = a2[i][j];
            }
        }
    }

    public int check_round(int x, int y, int color, boolean[][] ckd) {
        int count = 0;
        if ( x <= 0 || x >= COL-1 ) { return count; }
        if ( y % 2 == 1 && x >= COL-2 ) { return count; }
        if ( y < 0 || y >= ROW-1 ) { return count; }
        if ( fieldMatrix[y][x] != color ) {
            return count;
        }
        if (y-1 >= 0) {
            if ( y % 2 == 0 ) {
                if ( !ckd[y-1][x] && fieldMatrix[y-1][x] == color) {
                    ckd[y-1][x] = true;
                    count++;
                    count += check_round(x, y-1, color, ckd);
                }
                if ( !ckd[y-1][x-1] && x > 2 && fieldMatrix[y-1][x-1] == color) {
                    ckd[y-1][x-1] = true;
                    count++;
                    count += check_round(x-1, y-1, color, ckd);
                }
            } else {
                if ( !ckd[y-1][x] && fieldMatrix[y-1][x] == color) {
                    ckd[y-1][x] = true;
                    count++;
                    count += check_round(x, y-1, color, ckd);
                }
                if ( x <= COL-2 && !ckd[y-1][x+1] && fieldMatrix[y-1][x+1] == color) {
                    ckd[y-1][x+1] = true;
                    count++;
                    count += check_round(x+1, y-1, color, ckd);
                }
            }
        }

        if ( x > 1 ) {
            if ( ! ckd[y][x-1] && fieldMatrix[y][x-1] == color ) {
                ckd[y][x-1] = true;
                count++;
                count += check_round(x-1, y, color, ckd);
            }
        }

        if ( x < COL-1 ) {
            if ( y % 2 != 1 || x < COL-2 ) {
                if ( ! ckd[y][x+1] && fieldMatrix[y][x+1] == color ) {
                    ckd[y][x+1] = true;
                    count++;
                    count += check_round(x+1, y, color, ckd);
                }
            }
        }

        if (y < ROW-2) {
            if ( y % 2 == 0 ) {
                if ( ! ckd[y+1][x] && x < COL-1 && fieldMatrix[y+1][x] == color) {
                    ckd[y+1][x] = true;
                    count++;
                    count += check_round(x, y+1, color, ckd);
                }
                if ( x > 1 && ! ckd[y+1][x-1] && fieldMatrix[y+1][x-1] == color) {
                    ckd[y+1][x-1] = true;
                    count++;
                    count += check_round(x-1, y+1, color, ckd);
                }
            } else {
                if ( ! ckd[y+1][x] && fieldMatrix[y+1][x] == color) {
                    ckd[y+1][x] = true;
                    count++;
                    count += check_round(x, y+1, color, ckd);
                }
                if ( x < COL-2 && ! ckd[y+1][x+1] && fieldMatrix[y+1][x+1] == color) {
                    ckd[y+1][x+1] = true;
                    count++;
                    count += check_round(x+1, y+1, color, ckd);
                }
            }
        }
        return count;
    }
}