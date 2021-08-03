import gov.nasa.jpf.symbc.Debug;

public class OddEven {  
         //@ ensures \result <==>  x%2 == 0;
         //@ ensures !\result <==> x%2 != 0;
     public /*@ pure @*/ boolean isEven(int x) { 
            return x%2 == 0;
         } 

         //@ ensures !\result <==> x%2 == 0;
         //@ ensures \result <==>  x%2 != 0;
     public /*@ pure @*/ boolean isOdd(int x) { 
            return x%2 != 0;
         } 
     
     public static void main(String[] args) {
         int x = 100; 
         OddEven num = new OddEven();
         x = Debug.addSymbolicInt(x, "sym_x");
         num.isEven(x);
         num.isOdd(x);
         Debug.printPC("PC: ");
     }
}