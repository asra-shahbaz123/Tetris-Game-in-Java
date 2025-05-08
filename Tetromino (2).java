package OurTetrisGame;

import javafx.scene.paint.Color;

public class Tetromino implements Displaceable, DirectionChanger {

    private int[][] tetrominoShape;
    private int x, y;               // indicate current position of tetromino on grid
    private Color tetrominoColor;

    public Tetromino(TypeOfTetromino type) {
        this.tetrominoShape = type.getTetrominoShape();
        this.tetrominoColor = type.getTetrominoColor();
        this.x = 3;                                         // tetromino starts coming from forth column of grid
        this.y =0;                                          // tetromino starts coming from top i.e. first row
    }

    @Override
    public void displace(int horizontalMovement, int verticalMovement) {
        this.x += horizontalMovement;   //move tetromino horizontally
        this.y += verticalMovement;     //move tetromino vertically
    }

    @Override
    public boolean canDisplace(int horizontalMovement, int verticalMovement, Grid grid) {
        for(int i=0; i<tetrominoShape.length; i++) {
            for(int j=0; j<tetrominoShape[0].length; j++) {
                if(tetrominoShape[i][j] != 0) {            // when block of grid is occupied by tetromino
                    if(grid.isOccupied(horizontalMovement + j + x, verticalMovement + i + y)) {    // calculates new vertical and horizontal position
                        return false;
                        // tetromino can't move/displace if cell is occupied
                    }
                }
            }
        }
        return true;  // movement of tetromino is possible
    }

    @Override
    public void changeDirection() {
        if(squareShape()) {
            return;
        }

        int[][] rotatedShape = spinShape();
        tetrominoShape = rotatedShape;  // update the original tetromino shape by rotated one
    }

    public int[][] spinShape() {
        int rows = tetrominoShape.length;
        int cols = tetrominoShape[0].length;

        // Create a new 2D array to store the rotated shape
        int[][] rotatedShape = new int[cols][rows];

        // Rotate 90 degrees clockwise
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                rotatedShape[j][i] = tetrominoShape[i][j];
            }
        }
        return rotatedShape;
    }

    private boolean squareShape() {
        return tetrominoShape.length == 2 && tetrominoShape[0].length == 2;  //both rows and columns are 2
    }

    public int[][] getTetrominoShape() {
        return tetrominoShape;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Color getTetrominoColor() {
        return tetrominoColor;
    }
}
