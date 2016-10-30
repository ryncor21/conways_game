/* This is a class that was built to test the ConwaysGame class in a GUI.
** It uses java swing to accomplish this. It will load the start input from 
** a text file specified in the run arguements. It can also accept input 
** specified in a text field.
** Author: Ryan P. Corcoran
*/
import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class conwaysTesterGUI {
   
   //--Accessible anywhere in this class
   //Conways Game instance
   private static ConwaysGame x = new ConwaysGame();
   //Pixel size of square cells
   private static final int CELL_SIZE = 15;
   
   public static void main (String args[]) {
   //try to get runtime input to start game.      
      if(args.length!=0) {
         try {
            x = new ConwaysGame(args[0]);
         }
         catch (IOException q) {
            System.out.println("ERROR: File specified by runtime arguement not found.");
         }
      }
      
      //Init GUI Objects
      ConwaysPanel gameBoard = new ConwaysPanel();
      JFrame gameFrame = new JFrame();
      JFrame controlFrame = new JFrame();
      JPanel buttonPanel = new JPanel();
      JLabel countLbl = new JLabel();
      JLabel inputFileLbl = new JLabel("Input File:");
      JButton iterateBtn = new JButton("Iterate");
      JButton loadBtn = new JButton("Load");
      JButton resetBtn = new JButton("Reset");
      JButton hexBtn = new JButton("Show hex code");
      JTextField inputBox = new JTextField();
      
      //Add things together
      gameFrame.add(gameBoard);
      controlFrame.add(buttonPanel);
      buttonPanel.add(inputFileLbl);
      buttonPanel.add(inputBox);
      buttonPanel.add(loadBtn);
      buttonPanel.add(resetBtn);
      buttonPanel.add(hexBtn);
      buttonPanel.add(iterateBtn);
      buttonPanel.add(countLbl);
      gameBoard.addMouseListener(
         new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               Point p = e.getPoint();
               int j = (int)p.getX()/CELL_SIZE;
               int i = (int)p.getY()/CELL_SIZE;
               System.out.println(""+i+","+j);
               x.alternate(i,j);
               gameFrame.repaint();
               controlFrame.setTitle("Game Controls - "+x.getPatternName());
            }
         });
      hexBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               String output = x.getCompact();
               JFrame codeFrame = new JFrame("Hex output");
               codeFrame.setBounds(500,550,300,700);
               JTextArea codeField = new JTextArea(output);
               codeFrame.add(codeField);
               codeFrame.setVisible(true);           
            }
         });
      resetBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               x = new ConwaysGame();
               gameFrame.repaint();
               countLbl.setText("Iteration: "+x.getCount()+"");
               controlFrame.setTitle("Game Controls - "+x.getPatternName());
            }
         });
      iterateBtn.addActionListener(
         new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
               x.iterate();
               gameFrame.repaint();
               countLbl.setText("Iteration: "+x.getCount()+"");
               controlFrame.setTitle("Game Controls - "+x.getPatternName());
            } 
         } );
      loadBtn.addActionListener(
         new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
               try {
                  x = new ConwaysGame(inputBox.getText());
                  gameFrame.repaint();
                  countLbl.setText("Iteration: "+x.getCount()+"");
                  controlFrame.setTitle("Game Controls - "+x.getPatternName());
               }
               catch (IOException q) {
                  controlFrame.setTitle("ERROR: File not found.");
               }
            } 
         } );
      
      //Set it all up
      gameFrame.setTitle("Conway's Game of Life "+x.iSIZE+"x"+x.jSIZE+" by Ryan P. Corcoran");
      gameFrame.setBounds(200,200,17+x.jSIZE*(CELL_SIZE),40+x.iSIZE*(CELL_SIZE));
      gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      gameFrame.setVisible(true);
      countLbl.setText("Iteration: "+x.getCount()+"");
      inputBox.setPreferredSize( new Dimension( 200, 24 ) );
      controlFrame.setTitle("Game Controls - "+x.getPatternName());
      controlFrame.setBounds(200,750,725,90);
      controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      controlFrame.setVisible(true);
   }
   
   //Panel to draw the game
   public static class ConwaysPanel extends JPanel {
      public void paint(Graphics g) {
         for(int i = 0; i < x.iSIZE; i++) {
            for (int j = 0; j < x.jSIZE; j++) {
               if(x.getBoard()[i][j] == true) {
                  g.fillRect((CELL_SIZE*j),(CELL_SIZE*i),CELL_SIZE,CELL_SIZE);
               }
               else {
                  g.drawRect((CELL_SIZE*j),(CELL_SIZE*i),CELL_SIZE,CELL_SIZE);
               }
            }
         }
      }
   }
}