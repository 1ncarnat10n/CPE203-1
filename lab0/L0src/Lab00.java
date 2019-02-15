public class Lab00
{
   public static void main(String[] args) {
       // Declaring and initializing some variables
      int x = 5;
      String y = "hello";
      float z = 9.8F;

      // Printing the variables
      System.out.println("x: " + x + " y: " + y + " z: " + z);

       // A list (make an array in java)
       int[] nums = {3, 6, -1, 2};
       for (int val:nums) {
            System.out.println(val);
       }

       // Call a function
       int numFound = Lab00.charCount(y.toCharArray(), "l".charAt(0));
       System.out.println("Found:" + numFound);

       // A counting for loop
       for (int i=1; i < 11; i++) {
           System.out.print(i + " ");
       }

       System.out.println();

   }

   private static int charCount(char[] s, char c) {
       int count = 0;
       for (char ch: s) {
           if (ch == c) {
               count++;
           }
       }
       return count;
   }
}