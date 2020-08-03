# Programming Assignment 8

This is a simplified game of Pac-Man with real visuals added and is played in a separate window rather than in the terminal. This assignment introduces us to JavaFX, which is the standard GUI library for Java. Save and restore capabilities added.

To run on Windows command prompt: 
- Make sure that JavaFX is downloaded: https://gluonhq.com/products/javafx/
- Add an environment variable with `set PATH_TO_FX="path\to\javafx-sdk-14\lib"` (modify path as needed)
- Compile with `javac --module-path %PATH_TO_FX% --add-modules javafx.controls GuiPacman.java`
- Run with `java --module-path %PATH_TO_FX% --add-modules javafx.controls GuiPacman`
- Add `-h` flag to see options to configure with custom size, input and output board

Controls: 
 - Arrow keys to move in a certain direction
 - `s` to save the current board 
 

## Author

* **Kathy Li** - *Initial work* - [cs11wml](mailto:kal005@ucsd.edu)


