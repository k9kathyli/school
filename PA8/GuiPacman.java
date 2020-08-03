import javafx.application.*;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.io.*;

/**
 * This file creates a GUI to allow a user to play a simplified game of Pac-Man
 * using the arrow keys. It has save and restore capabilities, as well as 
 * adjustments to the game size through command-line arguments.
 * @author      Kathy Li <kal005@ucsd.edu>
 */


public class GuiPacman extends Application
/*
 * Construct and display the Pac-Man game in a new window. 
 */
{
  private String outputBoard; // The filename for where to save the Board
  private Board board; // The Game Board

  // Fill colors to choose
  private static final Color COLOR_GAME_OVER = Color.rgb(238, 228, 218, 0.73);
  private static final Color COLOR_VALUE_LIGHT = Color.rgb(249, 246, 242);
  private static final Color COLOR_VALUE_DARK = Color.rgb(119, 110, 101);

  GridPane game; // This GridPane contains all the elements of the actual game
  StackPane glass; // This StackPane contains the game-over screen 

  
  /* 
   * Name:      start
   * Purpose:   Start and keep the game running.
   * Parameter: the primary stage in which to set the scene of the game
   * Return:    none 
   */
  @Override
  public void start(Stage primaryStage)
  {
    // Process Arguments and Initialize the Game Board
    processArgs(getParameters().getRaw().toArray(new String[0]));

    game = new GridPane();
    game.setAlignment(Pos.CENTER); 
    // Set the padding of the pane.
    game.setPadding(new Insets(11.5,12.5,13.5,14.5)); 
    game.setHgap(5.5); 
    game.setVgap(5.5); 
    game.setStyle("-fx-background-color: rgb(0, 0, 0)");


    System.out.println(board.getGrid().length);
    // Set up initial game board
    updateGamePane(0);
    glass = new StackPane();
    StackPane rootPane = new StackPane();
    Scene scene = new Scene(rootPane); 
    // Scene has one root pane, which has the game pane and the game over pane.
    rootPane.getChildren().addAll(game, glass);

    primaryStage.setTitle("GuiPacman"); 
    primaryStage.setScene(scene); // Setting the scene
    primaryStage.show(); 

    scene.setOnKeyPressed(new myKeyHandler()); // Key press event handling 
  }


  /* 
   * Name:      updateGamePane
   * Purpose:   refresh the game window after a key is pressed
   * Parameter: How much Pac-Man should be rotated
   * Return:    none 
   */
  public void updateGamePane(int rotation){
    game.getChildren().clear();

    // Formatting title and current score of game board
    Text title = new Text();
    title.setText("Pac-Man");
    title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
    title.setFill(Color.WHITE);

    Text score = new Text();
    score.setText("Score: " + board.getScore());
    score.setFont(Font.font("Times New Roman", FontWeight.BOLD, 20));
    score.setFill(Color.WHITE);

    game.add(title, 0, 0, 4, 1);
    game.add(score, 5, 0, 2, 1);

    // Refresh all entities on the board
    for (int i = 0; i < board.getGrid().length; i++){
      for (int j = 0; j < board.getGrid().length; j++){

        ImageView rotated;
        Tile toAdd = new Tile(board.getGrid()[i][j]);
        if (board.getGrid()[i][j] == 'P'){
          // Rotate Pac-Man correctly visually
          rotated = toAdd.getNode();
          rotated.setRotate(rotation);
        }else{
          rotated = toAdd.getNode();

        }
        // Add the configured entity to the game board
        game.add(rotated, j, i+1); 
      }
    }
  }

  
  /*
   * Name:       myKeyHandler
   *
   * Purpose:    Handle key presses of user input 
   *
   *
   */
  private class myKeyHandler implements EventHandler<KeyEvent> {

   /* 
    * Name:      handle
    * Purpose:   handle the KeyEvent of user's input.
    * Parameter: KeyEvent e
    * Return:    none
    */
    @Override
    public void handle (KeyEvent e){
      int rotation = 0;
      // Only handle events as long as game is not over.
      if (board.isGameOver() == false){
        if (e.getCode() == (KeyCode.UP) && board.canMove(Direction.UP)){
          board.move(Direction.UP);
          rotation = 270;
          System.out.println("Moving up");

        }else if (e.getCode() == (KeyCode.LEFT) && board.canMove(Direction.LEFT)){
          board.move(Direction.LEFT);
          rotation = 180;

          System.out.println("Moving left");
        }else if (e.getCode() ==(KeyCode.DOWN) && board.canMove(Direction.DOWN)){
          board.move(Direction.DOWN);
          rotation = 90;
          
          System.out.println("Moving down");
        }else if (e.getCode() == (KeyCode.RIGHT) && board.canMove(Direction.RIGHT)){
          board.move(Direction.RIGHT);
          rotation = 0;

          System.out.println("Moving right");

        }else if (e.getCode() == (KeyCode.S)){
          try { 
            board.saveBoard(outputBoard); 
            System.out.println("Saving Board to " + outputBoard);

          } catch (IOException exception) { 
            System.out.println("saveBoard threw an Exception"); 
          } 
        }else{
          ;
        }
        if (board.isGameOver()){
          gameIsOver();
        }
      }
      else{
        gameIsOver();
      }
      updateGamePane(rotation);
      }
    

    /* 
     * Name:      gameIsOver
     * Purpose:   Check if the game is over and show the gameover board.
     * Parameter: None
     * Return:    None
     */
    private void gameIsOver() {
      Text over = new Text();
      over.setText("Game Over");
      over.setFont(Font.font("Times New Roman", FontWeight.BOLD, 40));
      glass.getChildren().add(over);
      glass.setStyle("-fx-background-color: rgb(238, 228, 218, 0.73)");  
    }
  } // End of Inner Class myKeyHandler.



  /*
   * Name:        Tile
   *
   * Purpose:     This class tile helps to make the tiles in the board 
   *              presented using JavaFX. Whenever a tile is needed,
   *              the constructor taking one char parameter is called
   *              and create certain ImageView fit to the char representation
   *              of the tile.
   * 
   *
   */
  private class Tile {

    private ImageView repr;   // This field is for the Rectangle of tile.
 
    /* 
     * Constructor
     *
     * Purpose:   Assigns images to specific Pac-Man game elements
     * Parameter: The char representation of an entity in the game grid 
     *
     */
    public Tile(char tileAppearance) {
      Image image;
        if (tileAppearance =='G'){
          image = new Image("image/blinky_left.png"); 

        }else if (tileAppearance =='P'){
          image = new Image("image/pacman_right.png"); 

        }else if (tileAppearance == '*'){
          image = new Image("image/dot_uneaten.png"); 

        }else if (tileAppearance == 'X'){
          image = new Image("image/pacman_dead.png"); 
        }

        else{
          image = new Image("image/dot_eaten.png");

        }
        repr = new ImageView(image);
        repr.setFitWidth(50);  //set the width and height 
        repr.setFitHeight(50);

    }

    // Getter for inner class. Returns the configured node to insert into Pane.
    public ImageView getNode() {
      return repr;
    }

  }  // End of Inner class Tile




  /** DO NOT EDIT BELOW */

  // The method used to process the command line arguments
  private void processArgs(String[] args)
  {
    String inputBoard = null;   // The filename for where to load the Board
    int boardSize = 0;          // The Size of the Board

    // Arguments must come in pairs
    if((args.length % 2) != 0)
    {
      printUsage();
      System.exit(-1);
    }

    // Process all the arguments 
    for(int i = 0; i < args.length; i += 2)
    {
      if(args[i].equals("-i"))
      {   // We are processing the argument that specifies
        // the input file to be used to set the board
        inputBoard = args[i + 1];
      }
      else if(args[i].equals("-o"))
      {   // We are processing the argument that specifies
        // the output file to be used to save the board
        outputBoard = args[i + 1];
      }
      else if(args[i].equals("-s"))
      {   // We are processing the argument that specifies
        // the size of the Board
        boardSize = Integer.parseInt(args[i + 1]);
      }
      else
      {   // Incorrect Argument 
        printUsage();
        System.exit(-1);
      }
    }

    // Set the default output file if none specified
    if(outputBoard == null)
      outputBoard = "Pac-Man.board";
    // Set the default Board size if none specified or less than 2
    if(boardSize < 3)
      boardSize = 10;

    // Initialize the Game Board
    try{
      if(inputBoard != null)
        board = new Board(inputBoard);
      else
        board = new Board(boardSize);
    }
    catch (Exception e)
    {
      System.out.println(e.getClass().getName() + " was thrown while creating a " +
          "Board from file " + inputBoard);
      System.out.println("Either your Board(String, Random) " +
          "Constructor is broken or the file isn't " +
          "formated correctly");
      System.exit(-1);
    }
  }

  // Print the Usage Message 
  private static void printUsage()
  {
    System.out.println("GuiPacman");
    System.out.println("Usage:  GuiPacman [-i|o file ...]");
    System.out.println();
    System.out.println("  Command line arguments come in pairs of the form: <command> <argument>");
    System.out.println();
    System.out.println("  -i [file]  -> Specifies a Pacman board that should be loaded");
    System.out.println();
    System.out.println("  -o [file]  -> Specifies a file that should be used to save the Pac-Man board");
    System.out.println("                If none specified then the default \"Pac-Man.board\" file will be used");
    System.out.println("  -s [size]  -> Specifies the size of the Pac-Man board if an input file hasn't been");
    System.out.println("                specified.  If both -s and -i are used, then the size of the board");
    System.out.println("                will be determined by the input file. The default size is 10.");
  }
}


