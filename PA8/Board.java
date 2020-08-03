import java.lang.StringBuilder;
import java.util.*;
import java.io.*;

/**
 * This file contains the class for the game board, as well as the functions to
 * manipulate game characters and their movements.
 * @author      Kathy Li <kal005@ucsd.edu>
 */
public class Board{

    // FIELD
    public final int GRID_SIZE;

    private char[][] grid;          // String Representation of Pac-man Game Board
    private boolean[][] visited;    // Record of where Pac-man has visited
    private PacCharacter pacman;    // Pac-man that user controls
    private PacCharacter[] ghosts;  // 4 Ghosts that controlled by the program
    private int score;              // Score Recorded for the gamer

    
    /*
     * Constructor
     *
     * Description: Create a new Pac-Man board to play
     *
     * Parameters:  size: height and width of board to be created 
     */
    public Board(int size) {

        // Initialize instance variables
        GRID_SIZE = size;
        grid = new char[GRID_SIZE][GRID_SIZE];
        visited = new boolean[GRID_SIZE][GRID_SIZE];
        score = 0;

        pacman = new PacCharacter(GRID_SIZE/2, GRID_SIZE/2, 'P');
        ghosts = new PacCharacter[4];
        ghosts[0] = new PacCharacter(          0,           0, 'G');
        ghosts[1] = new PacCharacter(          0, GRID_SIZE-1, 'G');
        ghosts[2] = new PacCharacter(GRID_SIZE-1,           0, 'G');
        ghosts[3] = new PacCharacter(GRID_SIZE-1, GRID_SIZE-1, 'G');

        setVisited(GRID_SIZE/2, GRID_SIZE/2);

        refreshGrid();
    }

    
    public Board(String inputBoard) throws IOException {
        // Create a scanner to scan the inputBoard.
        Scanner input = new Scanner(new File(inputBoard));

        // First integer in inputBoard is GRID_SIZE.
        GRID_SIZE = input.nextInt();
        // Second integer in inputBoard is score.
        score = input.nextInt();

        grid = new char[GRID_SIZE][GRID_SIZE];
        visited = new boolean[GRID_SIZE][GRID_SIZE];
        String line = input.nextLine(); // Skip current line (score line)

        char rep;
        ghosts = new PacCharacter[4];
        for ( int rowIndex = 0; rowIndex < GRID_SIZE; rowIndex++ )
        {
            line = input.nextLine();
            for ( int colIndex = 0; colIndex < GRID_SIZE; colIndex++ ) {
                rep = line.charAt(colIndex);
                grid[rowIndex][colIndex] = rep;

                switch (rep) {
                    case 'P':
                        setVisited(rowIndex, colIndex);
                        pacman = new PacCharacter(rowIndex, colIndex, 'P');
                        break;
                    case 'G':
                        for (int i = 0; i < ghosts.length; i++) {
                            if (ghosts[i] == null) {
                                ghosts[i] = new PacCharacter(rowIndex, colIndex, 'G');;
                                break;
                            }
                        }
                        break;
                    case 'X':
                        pacman = new PacCharacter(rowIndex, colIndex, 'P');
                        for (int i = 0; i < ghosts.length; i++) {
                            if (ghosts[i] == null) {
                                ghosts[i] = new PacCharacter(rowIndex, colIndex, 'G');;
                                break;
                            }
                        }
                        break;
                    case ' ':
                        setVisited(rowIndex, colIndex);
                        break;
                }
            }
        }
    }


    public int getScore() {
        return score;
    }


    public char[][] getGrid() {
        return grid;
    }

    public void setVisited(int x, int y) {
        if (x < 0 || y < 0 || x >= GRID_SIZE || y > GRID_SIZE) return;
        visited[x][y] = true;
    }
  /*
   * Name:      refreshGrid
   * Purpose:   Reset the board grid based on the matrix of visited spots and characters' locations.
   *            visited is a 2D array of boolean values, representing whether Pac-man had visited that
   *            spot or not.
   * Parameter: None
   * Return:    None
   */
    public void refreshGrid() {
        for (int i = 0; i < GRID_SIZE; i++)
            for (int j = 0; j < GRID_SIZE; j++) {
                if (!visited[i][j])
                    grid[i][j] = '*';
                else
                    grid[i][j] = ' ';
            }
        grid[pacman.getRow()][pacman.getCol()] = pacman.getAppearance();
        for (PacCharacter ghost : ghosts) {
            if (pacman.getRow() == ghost.getRow() && pacman.getCol() == ghost.getCol())
                grid[ghost.getRow()][ghost.getCol()] = 'X';
            else grid[ghost.getRow()][ghost.getCol()] = ghost.getAppearance();
        }

    }

  /* 
   * Name:      canMove
   * Purpose:   Check if a character can move in a specific direction
   * Parameter: The direction to move
   * Return:    True if the direction is a valid move, otherwise false
   */
    public boolean canMove(Direction direction) {
        if (direction == null) return false;
        // Calculate Coordinate after Displacement
        int pacmanRow = pacman.getRow() + direction.getY();
        int pacmanCol = pacman.getCol() + direction.getX();

        return pacmanRow >= 0 && pacmanRow < GRID_SIZE && pacmanCol >= 0 && pacmanCol < GRID_SIZE;
    }

  /* 
   * Name:      move
   * Purpose:   Move a character a space in one direction, then refresh the grid to reflect the change
   * Parameter: The direction to move
   * Return:    None
   */
    public void move(Direction direction) {
        // Calculate Coordinate after Displacement
        int pacmanRow = pacman.getRow() + direction.getY();
        int pacmanCol = pacman.getCol() + direction.getX();

        pacman.setPosition(pacmanRow, pacmanCol);
        if (!visited[pacmanRow][pacmanCol]) {
            score += 10;
            visited[pacmanRow][pacmanCol] = true;
        }

        for (PacCharacter ghost : ghosts) {
            ghostMove(ghost);
        }
        refreshGrid();
    }

  /* 
   * Name:      isGameOver
   * Purpose:   Check for game end condition
   * Parameter: None
   * Return:    true if the game is over, otherwise false
   */
    public boolean isGameOver() {
        int pacmanRow = pacman.getRow();
        int pacmanCol = pacman.getCol();
        // If any ghost has the same location as Pac-man, the game is over
        for (PacCharacter ghost : ghosts)
            if (ghost.getRow() == pacmanRow && ghost.getCol() == pacmanCol)
                return true;
        return false;
    }

  /* 
   * Name:      ghostMove
   * Purpose:   Calculate where each ghost should move next. Ghosts will always try to get closer to Pac-man
   * Parameter: The ghost to move
   * Return:    A direction for the ghost to move
   */
    public Direction ghostMove(PacCharacter ghost) {
        // Calculate Pac-man's current position
        int pacmanRow = pacman.getRow();
        int pacmanCol = pacman.getCol();

        // Calculate ghost's current position
        int ghostRow = ghost.getRow();
        int ghostCol = ghost.getCol();
        
        // Get distance between Pac-man and ghost
        int rowDist = Math.abs(ghostRow - pacmanRow);
        int colDist = Math.abs(ghostCol - pacmanCol);

        // Ghost is in the same row but not same column
        if (rowDist == 0 && colDist > 0) {
            if (ghostCol - pacmanCol > 0) {
                ghost.setPosition(ghostRow, ghostCol - 1);
                return Direction.LEFT;
            } else { // ghostCol - pacmanCol < 0
                ghost.setPosition(ghostRow, ghostCol + 1);
                return Direction.RIGHT;
            }
        }
        // Ghost is in the same column but not same row
        else if (rowDist > 0 && colDist == 0 ) {
            if (ghostRow - pacmanRow > 0) {
                ghost.setPosition(ghostRow - 1, ghostCol);
                return Direction.UP;
            } else { // ghostRow - pacmanRow < 0
                ghost.setPosition(ghostRow + 1, ghostCol);
                return Direction.DOWN;
            }
        }
        // Pac-man captured, ghost shouldn't move
        else if (rowDist == 0 && colDist == 0) {
            return Direction.STAY;
        } else {
            // Minimize row or column distance depending on which is larger    
            if (rowDist < colDist) {
                if (ghostRow - pacmanRow > 0) {
                    ghost.setPosition(ghostRow - 1, ghostCol);
                    return Direction.UP;
                } else { // ghostRow - pacmanRow < 0
                    ghost.setPosition(ghostRow + 1, ghostCol);
                    return Direction.DOWN;
                }
            } else {
                if (ghostCol - pacmanCol > 0) {
                    ghost.setPosition(ghostRow, ghostCol - 1);
                    return Direction.LEFT;
                } else { // ghostCol - pacmanCol < 0
                    ghost.setPosition(ghostRow, ghostCol + 1);
                    return Direction.RIGHT;
                }
            }
        }
    }

  /* 
   * Name:      toString
   * Purpose:   Convert the board to a string to be displayed in terminal
   * Parameter: None
   * Return:    The board as a string
   */
    public String toString(){
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", this.score));

        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int column = 0; column < GRID_SIZE; column++) {
                outputString.append("  ");
                outputString.append(grid[row][column]);
            }
            outputString.append("\n");
        }
        return outputString.toString();
    }
}
