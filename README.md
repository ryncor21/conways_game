# conways_game

This repository is for my Java implementation of Conway's Game of Life.
Information about this game can be found here:
https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life

The game uses a "dead border" approach.  
It uses a boolean array to store the game board.  
The *.cwg files are used to input starting patterns into the game.  
Patterns are stored as hexadecimal numbers, which are converted to binary digits.  
0 represents a dead cell and 1 represents a living cell.  
If the pattern in the text file does not fill the game board, it will place the pattern into the middle of the board
and fill in the rest of the board with dead cells.  

The GUI was built with swing to test the class.   
  
Current Problems are:  
0.  The game board size is a fixed constant. It may be desirable to change the size of the board.
1.  This implementation not optimal. There are ways to improve the computing time of the game.  
2.  Will show unneccessary dead cells when exporting the game as a hexadecimal String.  
3.  There is a strange delay (sometimes) between clicking on a cell and alternating the cell.  
4.  I can't get the ConwaysPanel to show up in the same JFrame as the controls. Thus, I have made a different JFrame.  
