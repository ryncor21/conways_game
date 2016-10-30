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
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.BoundedRangeModel;
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
   private static ConwaysGame game = new ConwaysGame();
   //Timer to animate the game
   private static Timer time;
   //Pixel size of square cells
   private static final int CELL_SIZE = 15;
   //Time in milliseconds to animate the game board
   private static int timeCut = 150;
   //Start or stop the animation?
   private static Boolean animate = true;;
   
   public static void main (String args[]) {
         
      //Init GUI Objects
      ConwaysPanel gameBoard = new ConwaysPanel();
      JFrame gameFrame = new JFrame();
      JFrame controlFrame = new JFrame();
      JPanel buttonPanel = new JPanel();
      JLabel countLbl = new JLabel();
      JLabel inputFileLbl = new JLabel("Input File:");
      JLabel delayLbl = new JLabel("Delay in ms:");
      JButton loadBtn = new JButton("Load");
      JButton resetBtn = new JButton("Reset");
      JButton hexBtn = new JButton("Show hex code");
      JButton animateBtn = new JButton("Start Animation");
      JTextField fileInputBox = new JTextField();
      JSlider delaySlider = new JSlider(50,700,400);
      
      
      //try to get runtime input to start game.      
      if(args.length!=0) {
         try {
            fileInputBox.setText(args[0]);
            game = new ConwaysGame(args[0]);
         }
         catch (IOException q) {
            System.out.println("ERROR: File specified by runtime arguement not found.");
            controlFrame.setTitle("ERROR: File not found.");
         }
      }
      else {
         controlFrame.setTitle("Game Controls - "+game.getPatternName());
      }

      //Add things together
      gameFrame.add(gameBoard);
      controlFrame.add(buttonPanel);
      buttonPanel.add(inputFileLbl);
      buttonPanel.add(fileInputBox);
      buttonPanel.add(loadBtn);
      buttonPanel.add(resetBtn);
      buttonPanel.add(hexBtn);
      buttonPanel.add(delayLbl);
      buttonPanel.add(delaySlider);
      buttonPanel.add(animateBtn);
      buttonPanel.add(countLbl);
      time = new Timer(timeCut, 
         new ActionListener() {
            public void actionPerformed (ActionEvent e) {
               game.iterate();
               gameFrame.repaint();
               countLbl.setText("Iteration: "+game.getCount()+"");
               controlFrame.setTitle("Game Controls - "+game.getPatternName());
            }
         });
      delaySlider.addChangeListener(
         new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
               time.setDelay(delaySlider.getModel().getValue());
            }
         });
      gameBoard.addMouseListener(
         new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
               Point p = e.getPoint();
               int j = (int)p.getX()/CELL_SIZE;
               int i = (int)p.getY()/CELL_SIZE;
               game.alternate(i,j);
               gameFrame.repaint();
               controlFrame.setTitle("Game Controls - "+game.getPatternName());
            }
         });
      animateBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               if(animate) {
                  time.start();
                  animate = false;
                  animateBtn.setText("Stop Animation");
               }
               else {
                  time.stop();
                  animate = true;
                  animateBtn.setText("Start Animation");
               }
            }
         });
      hexBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               String output = game.getCompact();
               JFrame codeFrame = new JFrame("Hex output");
               codeFrame.setBounds(850,200,300,700);
               JTextArea codeField = new JTextArea(output);
               codeFrame.add(codeField);
               codeFrame.setVisible(true);           
            }
         });
      resetBtn.addActionListener(
         new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               game = new ConwaysGame();
               gameFrame.repaint();
               countLbl.setText("Iteration: "+game.getCount()+"");
               controlFrame.setTitle("Game Controls - "+game.getPatternName());
            }
         });
      loadBtn.addActionListener(
         new ActionListener() { 
            public void actionPerformed(ActionEvent e) {
               try {
                  game = new ConwaysGame(fileInputBox.getText());
                  gameFrame.repaint();
                  countLbl.setText("Iteration: "+game.getCount()+"");
                  controlFrame.setTitle("Game Controls - "+game.getPatternName());
               }
               catch (IOException q) {
                  controlFrame.setTitle("ERROR: File not found.");
               }
            } 
         } );
      
      //Set it all up
      gameFrame.setTitle("Conway's Game of Life "+game.iSIZE+"x"+game.jSIZE+" by Ryan P. Corcoran");
      gameFrame.setBounds(200,200,17+game.jSIZE*(CELL_SIZE),40+game.iSIZE*(CELL_SIZE));
      gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      gameFrame.setVisible(true);
      countLbl.setText("Iteration: "+game.getCount()+"");
      fileInputBox.setPreferredSize(new Dimension(200,24));
      delaySlider.setMinorTickSpacing(10);
      delaySlider.setMajorTickSpacing(100);
      delaySlider.setPaintTicks(true);
      controlFrame.setBounds(200,750,570,130);
      controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      controlFrame.setVisible(true);      
   }

   public static class ConwaysPanel extends JPanel {
      public void paint(Graphics g) {
         for(int i = 0; i < game.iSIZE; i++) {
            for (int j = 0; j < game.jSIZE; j++) {
               if(game.getBoard()[i][j] == true) {
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