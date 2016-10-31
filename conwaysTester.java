/* This is a class used to test the ConwaysGame class.
** Author: Ryan P. Corcoran
*/
import java.io.IOException;
public class conwaysTester {
   public static void main (String[] args) throws IOException {
      ConwaysGame x = new ConwaysGame("beacon.cwg",32,32);
      for(int i = 0; i < 3; i++) {
         System.out.println("Iteration: "+i);
         System.out.println("Compact:");
         x.showCompact();
         System.out.println("Full:");
         x.show();
         x.iterate();
      }
   }
}
