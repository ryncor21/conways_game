/* This is an interface for use with an implementation of a class
** representing Conway's Game of Life.
** Author: Ryan P. Corcoran
*/
public interface ConwaysInterface {
   //Show the current game state in full format in the console
   public void show();
   
   //Show the current game state in hexadecimal format in the console
   public void showCompact();
   
   //Returns current game state in hexadecimal format as a String
   public String getCompact();
   
   //Advance to the next gameplay state
   public void iterate();
   
   //Advance to the next gameplay state x number of times
   public void iterate(Integer x);
   
   //Alternate whether the cell specified by the coordinates is live or dead
   public void alternate(int i, int j);
   
   //Returns the array that represents this game
   public Boolean[][] getBoard();
   
   //Returns the count of how many times the game was iterated
   public Integer getCount();
   
   //Returns the number of living cells currently in the game
   public Double livingCells();
   
   //Returns the number of dead cells currently in the game
   public Double deadCells();
   
   //Returns the name of the pattern currently loaded
   public String getPatternName();
   
}