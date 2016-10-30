# conways_game

This repository is for my Java implementation of Conway's Game of Life.
Information about this game can be found here:
https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life

The game uses a "dead border" approach.
It uses a boolean array to store the game board.
The *.cwg files are used to imput starting patterns into the game.
Patterns are stored as hexadecimal numbers, which are converted to binary digits.
0 represents a dead cell and 1 represents a living cell.
If the pattern in the text file does not fill the game board, it will place the pattern into the middle of the board
and fill in the rest of the board with dead cells.

The GUI was built with swing to test the class.

Current Problems are:
1. This implementation not optimal. The algorithm checks every cell on the board when it probably only has to check cells that are near living cells./n
2. There is a strange delay (sometimes) between clicking on a cell and alternating the cell./n
3. I can't get the ConwaysPanel to show up in the same JFrame as the controls. Thus, I have made a different JFrame./n
