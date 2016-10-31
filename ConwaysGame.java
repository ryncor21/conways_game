/* This is a class that represents Conway's Game of Life as a booolean array.
** The array size is given in the constructor. It can accept a start matrix in 
** hexadecimal form by text file or by passing from another class. 
** It can also accept a start matrix in full form by passing from another class.
** The input array is padded with dead cells if it is below the size of the game board.
** The class uses the  "dead-border" approach. Any live borders in the input 
** will be killed after the first iteration.
** Author: Ryan P. Corcoran
*/

//Used when the game initialization is supplied by a text file
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
//Used to help convert binary to hex
import java.util.Hashtable;

public class ConwaysGame implements ConwaysInterface{

   //---Constants
   private final int BINARY_WORD = 4;
   private final String UNTITLED_NAME = "Untitled Pattern";
   private final Hashtable<String, String> BINARY_TO_HEX = new Hashtable<String, String>();
   private final Hashtable<String, String> HEX_TO_BINARY = new Hashtable<String, String>();
   private final String[] BINARY = {"0000","0001","0010","0011","0100","0101","0110",
                     "0111", "1000","1001","1010","1011","1100","1101","1110","1111" };
   private final char CHAR_TRUE = '1';
   private final char CHAR_FALSE = '0';
   private final String[] HEX = {"0","1","2","3","4","5","6","7",
                                 "8","9","A","B","C","D","E","F"};
   private final char COMMENT_INDICATOR = '/';
   private final char PATTERN_NAME_INDICATOR = '@';
   private final char LIVING_CHAR = '*';
   private final char DEAD_CHAR = '`';
      
   //---Instance Variables
   //Size of the game board going down
   private final int iSize;
   //Size of the game board going across
   private final int jSize;
   //Array to store the initial and current state of the game
   private Boolean[][] firstArray;
   //Array to temporarily store the next iteration of the game
   private Boolean[][] nextArray;   
   //Array of strings to store compact hexadecimal input
   private String[] input;
   //How many times the game was iterated        
   private Integer iterationCount = 0; 
   //String to store pattern name
   private String patternName = UNTITLED_NAME;
   
   //---Constructors
   //-Given a full start array
   public ConwaysGame(Boolean[][] start){
      iSize = start.length;
      jSize = start[0].length;
      setupHash();
      firstArray = start;   
   }
   //-Given a valid compact input array and size of game board
   public ConwaysGame(String[] input, int iSize, int jSize) {
      this.iSize = iSize;
      this.jSize = jSize;
      setupHash();
      this.input = input;
      processInput();          
   }
   //-Given the name of a text file containing compact input and size of game board
   public ConwaysGame(String FileName, int iSize, int jSize) throws IOException  {
      this.iSize = iSize;
      this.jSize = jSize;
      setupHash();
      processFile(FileName);
      processInput();
   }
   
   //-Constructs an empty game object of given size
   public ConwaysGame(int iSize, int jSize) {
      this.iSize = iSize;
      this.jSize = jSize;
      setupHash();
      makeEmptyGrid();
      patternName = "Empty Grid";
   }
   
   /* Fills the game array with dead cells.
   */
   private void makeEmptyGrid() {
      firstArray = new Boolean[iSize][jSize];
      for(int i = 0; i <iSize; i++)
         for(int j = 0; j <jSize; j++)
            firstArray[i][j] = false;
   }
   
   /* Will add the necessary information to the hashtables used in
   ** converting hex and binary.
   */
   private void setupHash() {
      for(int i = 0 ; i < 16; i++) {
         BINARY_TO_HEX.put(BINARY[i],HEX[i]);
         HEX_TO_BINARY.put(HEX[i],BINARY[i]);
      }
   }
   
   /* Will process text file and store in input array for use in initializing
   ** the game. The text file only needs to contain the pattern. It does
   ** not need to have enough zeros to fill the whole game board.
   */
   private void processFile(String FileName) throws IOException {
      BufferedReader buffer = new BufferedReader(new FileReader(FileName));
      int count = 0;
      while (buffer.ready()){
         String line = buffer.readLine();
         if (line.charAt(0) != COMMENT_INDICATOR && 
                line.charAt(0) != PATTERN_NAME_INDICATOR) {
            count++;
         }
      }         
      buffer.close();
      input = new String[count];
      buffer = new BufferedReader(new FileReader(FileName));
      count = 0;
      while (buffer.ready()){
         String line = buffer.readLine();
         if (line.charAt(0) == COMMENT_INDICATOR) {
               //Skip this line
         }
         else if(line.charAt(0) == PATTERN_NAME_INDICATOR) {
            patternName = line.substring(1);
         }
         else {
            input[count] = line;
            count++;
         }
      }
      buffer.close();   
   }
   
   /* Will process the input array that is in compact trimmed form.
   ** It will place the pattern in the middle of the game board
   ** and fill in dead cells around it.
   */
   private void processInput() {
      makeEmptyGrid();
      int patternWidth = input[0].length()*4;
      int patternHeight = input.length;
      int iStart = (iSize - patternHeight)/2;
      int jStart = (jSize - patternWidth)/2;
      for(int i = 0; i < input.length; i++) {
         for (int j = 0; j < input[i].length(); j++) {
            String binary = HEX_TO_BINARY.get(""+input[i].charAt(j));
            for(int k = 0; k < BINARY_WORD; k++) {
               firstArray[i+iStart][k+jStart+(j*BINARY_WORD)]=charToBool(binary.charAt(k));
            }
         }
      }      
   }

   /* When given a char value, will return corresponding boolean value.
   */
   private boolean charToBool(char bin) {
      if (bin == CHAR_TRUE) {
         return true;
      }
      else {
         return false;
      }
   }
   
   /* When given a boolean value, will return corresponding char.
   */
   private char boolToChar(boolean val) {
      if (val) {
         return CHAR_TRUE;
      }
      else { 
         return CHAR_FALSE;
      }
   }
   
   /* Will return true if the coordinates indicated by i and j fall on the
   ** border of the game area.
   */
   private boolean isBorder(int i, int j) {
      if(i == iSize-1 || i == 0 || j == jSize-1 || j == 0) {
         return true;
      }
      else {
         return false;
      }
   }
   
   /* Will return an integer count of how many living cells reside next to 
   ** the cell specified by the coordinates.
   */
   private Integer liveNeighbors(int i, int j) {
      assert !isBorder(i,j) : "Why are you counting the live neighbors of the border?";
      int tally = 0;
      if(firstArray[i][j]) {
         tally--;
      }
      for(int a = (i-1); a <= (i+1); a++) {
         for(int b = (j-1); b <= (j+1); b++) {
            if(firstArray[a][b]) {
               tally++;
            }
         }
      }
      return tally;
   }
   
   /* Overloaded method, will iterate() x number of times.
   */
   @Override
   public void iterate(Integer x) {
      for(int i = 0; i < x; i++) {
         iterate();
      }
   }
   
   /* Will perform one iteration of the game's rules for life and death.
   */
   @Override
   public void iterate() {
      iterationCount++;
      nextArray = new Boolean[iSize][jSize];
      for(int i = 0; i < iSize; i++) {
         for (int j = 0; j < jSize; j++) {
            if(isBorder(i,j)) {
               nextArray[i][j] = false;
            }
            else{
               int neighbors = liveNeighbors(i,j);
               if(firstArray[i][j]){
                  if(neighbors > 3 || neighbors < 2) {
                     nextArray[i][j] = false;
                  }
                  else {
                     nextArray[i][j] = true;
                  }
               }
               else {
                  if(neighbors==3) {
                     nextArray[i][j] = true;
                  }
                  else {
                     nextArray[i][j] = false;
                  }
               }
            }
         }
      }
      firstArray = nextArray;
      nextArray = null;
   }
   
   /* Will show the current state of the game in the console in complete form.
   */
   @Override
   public void show() {
      for (int i = 0; i < iSize; i++) {
         for (int j = 0; j < jSize; j++) {
            if(firstArray[i][j]) {
               System.out.print(LIVING_CHAR); 
            }
            else {
               System.out.print(DEAD_CHAR);
            }
         }
         System.out.println();
      }
   }
   
   /* Will show the current state of the game in the console in compact form
   ** including all the dead cells surrounding the pattern.
   */
   @Override
   public void showCompact() {
      String[] result = new String[iSize];
      String binary = "";
      for(int i = 0; i < iSize; i++) {
         result[i] = "";
         for(int j = 0; j < jSize; j++) {
            binary+=boolToChar(firstArray[i][j]);
            if((j+1)%BINARY_WORD == 0) {
               result[i]+=BINARY_TO_HEX.get(binary);
               binary = "";
            }
         }
         System.out.println(result[i]);
      }
   }
   
   /* Returns the current game state as a compact hex string, including
   ** all the dead cells surrounding the pattern.
   */
   @Override
   public String getCompact() {
      String result = "";
      String binary = "";
      for(int i = 0; i < iSize; i++) {
         for(int j = 0; j < jSize; j++) {
            binary+=boolToChar(firstArray[i][j]);
            if((j+1)%BINARY_WORD == 0) {
               result+=BINARY_TO_HEX.get(binary);
               binary = "";
            }
         }
         result+="\n";
      }
      return result;
   }
   
   /* Will return the game board array for use by another class.
   */
   @Override
   public Boolean[][] getBoard() {
      return firstArray;
   }
   
   /* Will return the current iteration count for this instance of the game.
   */
   @Override
   public Integer getCount() {
      return iterationCount;
   }
   
   /* Will return the name of the pattern currently loaded as a String.
   */
   @Override
   public String getPatternName() {
      return patternName;
   }
   
   /* Counts the current number of dead cells and returns as a Double.
   */
   @Override
   public Double deadCells() {
      Double count = 0.0;
      for(int i = 0; i < iSize; i++) {
         for(int j = 0; j < jSize; j++) {
            if(!firstArray[i][j]){
               count+=1.0;
            }
         }
      }
      return count;
   }
   
   /* Counts the current number of living cells and returns as a Double.
   */
   @Override
   public Double livingCells() {
      Double count = 0.0;
      for(int i = 0; i < iSize; i++) {
         for(int j = 0; j < jSize; j++) {
            if(firstArray[i][j]){
               count+=1.0;
            }
         }
      }
      return count;
   }
   
   /* Alternates whether the cell specified by the coordinates is living or dead.
   */
   @Override
   public void alternate(int i, int j) {
      patternName = UNTITLED_NAME;
      if(firstArray[i][j]) {
         firstArray[i][j] = false;
      }
      else if (!isBorder(i,j)){
         firstArray[i][j] = true;
      }
   }
}