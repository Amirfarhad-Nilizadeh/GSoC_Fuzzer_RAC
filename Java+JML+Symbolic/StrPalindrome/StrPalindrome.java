import java.util.Arrays;
import gov.nasa.jpf.symbc.Debug;

public class StrPalindrome {
   private /*@ spec_public @*/ String reverse = "";
   /*@ public normal_behavior
     @    requires (\forall int i; 0 <= i && i < str.length(); (int)Character.MIN_VALUE <= (int)str.charAt(i) && (int)str.charAt(i) <= (int)Character.MAX_VALUE);
     @    assignable reverse;
     @    ensures \result <==> reverse.equals(str); @*/
   public boolean isPalindrome(String str) {
      
      int length = str.length();
    
      //@ ghost int i_counter;
      //@ set i_counter = 0;
      //@ maintaining -1 <= i && i < str.length(); 
      //@ decreases i;
      //@ maintaining i_counter + i + 1 == length;
      for (int i = length - 1; 0 <= i; i--) {
         reverse = reverse + str.charAt(i);
     //@ set i_counter = i_counter + 1;
       }
      //@ assert i_counter == length;   
      return reverse.equals(str);
   }
   
   public static void main(String[] args) {
       StrPalindrome obj = new StrPalindrome();
       char[] char_ar = {'r', 'e', 'f', 'e', 'r'};
       
       for (int i = 0; i < char_ar.length; i++) {
           char_ar[i] = Debug.addSymbolicChar(char_ar[i], "sym_" + i);   
       }
       
       String str = Arrays.toString(char_ar);
       obj.isPalindrome(str);
       Debug.printPC("PC: ");
   }
} 