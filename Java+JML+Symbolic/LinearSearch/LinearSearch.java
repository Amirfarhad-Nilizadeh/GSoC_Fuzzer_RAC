import gov.nasa.jpf.symbc.Debug;

public class LinearSearch {
     private static /*@ spec_public*/ int location;

      //@ assignable location;
      //@ ensures \result == -1 <==>  (\forall int i; 0 <= i && i < array.length; array[i] != search);
      //@ ensures 0 <= \result && \result < array.length ==>  array[\result] == search;
      public static int linearSearch(int search, int array[]) {
    int c;
    //@ maintaining 0 <= c && c <= array.length;
    //@ maintaining (\forall int i; 0 <= i && i < c; array[i] != search);
    //@ decreases array.length - c;
        for (c = 0; c < array.length; c++) {  
          if (array[c] == search) {
          location = c;
              break;
          }
       }
       if (c == array.length) {
            location = -1;
       }
     return location;
     }
      
      public static void main(String[] args) {
          int y = 6;
          int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
          y = Debug.addSymbolicInt(y, "sym_y");
         
          for (int i = 0; i < arr.length; i++) {
              arr[i] = Debug.addSymbolicInt(arr[i], "sym_"+i);   
          }
         linearSearch(y, arr);
          Debug.printPC("PC: ");
      }
  }