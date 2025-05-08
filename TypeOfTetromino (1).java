package OurTetrisGame;

import javafx.scene.paint.Color;

public enum TypeOfTetromino {

    I(new int[][]{{1, 1, 1, 1}}, Color.CYAN), // I-shaped tetromino
    O(new int[][]{{1, 1}, {1, 1}}, Color.YELLOW), // O-shaped tetromino
    T(new int[][]{{0, 1, 0}, {1, 1, 1}}, Color.ORANGE), // T-shaped tetromino
    S(new int[][]{{0, 1, 1}, {1, 1, 0}}, Color.AQUAMARINE), // S-shaped tetromino
    Z(new int[][]{{1, 1, 0}, {0, 1, 1}}, Color.HOTPINK), // Z-shaped tetromino
    J(new int[][]{{1, 0, 0}, {1, 1, 1}}, Color.MEDIUMSLATEBLUE), // J-shaped tetromino
    L(new int[][]{{0, 0, 1}, {1, 1, 1}}, Color.CRIMSON); // L-shaped tetromino

    private int[][] tetrominoShape;
    private Color tetrominoColor; // Color for each Tetromino

    TypeOfTetromino(int[][] tetrominoShape, Color tetrominoColor) {
        this.tetrominoShape = tetrominoShape;
        this.tetrominoColor = tetrominoColor;      // Give color to tetromino according to its type
    }

    public int[][] getTetrominoShape() {
        return tetrominoShape;
    }

    public Color getTetrominoColor() {
        return tetrominoColor;
    }
}
