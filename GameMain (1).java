package OurTetrisGame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameMain extends Application {
    private Grid grid;
    private Tetromino currentTetromino;
    private Score score;
    private boolean isGameOver = false;
    private int linesCleared = 0;

    @Override
    public void start(Stage primaryStage) {
        int highestScore = FileHandler.readHighScore();

        // Welcome Screen with High Score
        StackPane welcomePane = new StackPane();
        Canvas welcomeCanvas = new Canvas(500, 600);
        GraphicsContext gc = welcomeCanvas.getGraphicsContext2D();
        welcomePane.getChildren().add(welcomeCanvas);

        // Draw welcome message and highest score
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Monotype corsiva", FontWeight.BOLD, 40));
        gc.fillText("Welcome to Tetris!", 120, 200);
        gc.fillText("Highest Score: " + highestScore, 120, 250);

        // Play Button
        javafx.scene.control.Button playButton = new javafx.scene.control.Button("Play");
        playButton.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        playButton.setOnAction(e -> startGame(primaryStage)); // Start game on button click
        welcomePane.getChildren().add(playButton);
        StackPane.setAlignment(playButton, javafx.geometry.Pos.CENTER);

        Scene welcomeScene = new Scene(welcomePane, 500, 600);
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Tetris Game");
        primaryStage.show();
    }

    private void startGame(Stage primaryStage) {
        grid = new Grid();
        currentTetromino = generateNewTetromino();
        score = new Score();

        Canvas canvas = new Canvas(500, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        StackPane root = new StackPane();
        root.getChildren().add(canvas);

        Scene gameScene = new Scene(root, 500, 600);
        gameScene.setOnKeyPressed(this::handleKeyPress);

        primaryStage.setScene(gameScene);

        // Game loop
        new Thread(() -> {
            while (!isGameOver) {
                try {
                    Thread.sleep(500);
                    updateGame(gc);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // Handle key press for movement and actions
    private void handleKeyPress(KeyEvent event) {
        if (isGameOver) {
            return;
        }

        switch (event.getCode()) {
            case LEFT:
                if (currentTetromino.canDisplace(-1, 0, grid)) {
                    currentTetromino.displace(-1, 0);
                }
                break;
            case RIGHT:
                if (currentTetromino.canDisplace(1, 0, grid)) {
                    currentTetromino.displace(1, 0);
                }
                break;
            case DOWN:
                if (currentTetromino.canDisplace(0, 1, grid)) {
                    currentTetromino.displace(0, 1);
                }
                break;
            case UP:
                currentTetromino.changeDirection();
                break;
            default:
                break;
        }
    }

    private void updateGame(GraphicsContext gc) {
        if (currentTetromino.canDisplace(0, 1, grid)) {  //When the Tetromino Can Move Down
            currentTetromino.displace(0, 1); // Move the tetromino down
        }
        else {  //When the Tetromino Cannot Move Down
            // Place the tetromino on the grid
            grid.placeTetromino(currentTetromino);

            // Check for and remove full lines
            int linesClearedThisRound = grid.removeFullLines();
            if (linesClearedThisRound > 0) {
                linesCleared += linesClearedThisRound;
                score.addPoints(linesClearedThisRound * 5); // Score increases by 5 for each line cleared
            }

            // Check for game over
            if (checkGameOver()) {
                gameOver(gc);
                return;
            }

            // Generate a new tetromino after the current one lands
            currentTetromino = generateNewTetromino();
        }

        draw(gc);
    }

    // Check if the tetromino reaches the top of the grid, game over if true
    private boolean checkGameOver() {
        for (int j = 0; j < grid.WIDTH; j++) {
            if (grid.isOccupied(j, 0)) {
                return true; // Game over if any cell in the top row is occupied
            }
        }
        return false;
    }

    private void gameOver(GraphicsContext gc) {
        isGameOver = true;

        // Check if the current score is a new high score
        int highestScore = FileHandler.readHighScore();
        if (score.getScore() > highestScore) {
            FileHandler.writeHighScore(score.getScore());
            highestScore = score.getScore();
        }

        gc.setFill(Color.RED);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
        gc.fillText("GAME OVER", 100, 300);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        gc.fillText("Highest Score: " + highestScore, 150, 350);
    }

    // Generate a new random Tetromino
    private Tetromino generateNewTetromino() {
        TypeOfTetromino[] types = TypeOfTetromino.values();
        int index = (int) (Math.random() * types.length);
        return new Tetromino(types[index]);
    }

    private void draw(GraphicsContext gc) {
        // Clear the screen
        gc.clearRect(0, 0, 500, 600);

        // Draw the grid and current Tetromino
        grid.drawGrid(gc);
        drawTetromino(currentTetromino, gc);

        // Set font style for score and lines cleared
        gc.setFont(Font.font("Bernard MT Condensed", FontWeight.BOLD, 30)); // Using Bernard MT Condensed font
        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + score.getScore(), 350, 50);

        // Display Lines cleared text
        gc.fillText("Lines: " + linesCleared, 350, 100);
    }

    // Draw the current Tetromino
    private void drawTetromino(Tetromino tetromino, GraphicsContext gc) {
        Color color = tetromino.getTetrominoColor();
        for (int i = 0; i < tetromino.getTetrominoShape().length; i++) {
            for (int j = 0; j < tetromino.getTetrominoShape()[i].length; j++) {
                if (tetromino.getTetrominoShape()[i][j] != 0) {
                    gc.setFill(color);
                    gc.fillRect((tetromino.getX() + j) * 30, (tetromino.getY() + i) * 30, 30, 30);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

