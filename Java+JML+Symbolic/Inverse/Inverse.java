import gov.nasa.jpf.symbc.Debug;

public class Inverse {
    //@ ensures !\result ==> ((x.length != y.length) || (\exists int i; 0 <= i && i < x.length; x[i] != y[x.length - 1 -i]));
    //@ ensures \result ==> x.length == y.length && (\forall int i; 0 <= i && i < x.length; x[i] == y[x.length - 1 - i]);
    public static boolean Inverse(int[] x, int[] y) {
        if (x.length != y.length) return false;
        int index = 0;
        //@ maintaining 0 <= index && index <= x.length && x.length == y.length;
        //@ maintaining (\forall int i; 0 <= i && i < index; x[i] == y[x.length -1 - i]);
        //@ decreases x.length - index;
        while (index < x.length) {
            if (x[index] != y[x.length - 1 - index]) {
                return false;
            } else {
                index = index + 1;
            }
        }
        return true;
    }
    
    public static void main(String[] args) {
        int[] arrA = {7, 11, 12, 8, 4, 6, 5, 3, 0, 5, 10, 6, 4};
        int[] arrB = {4, 6, 5, 0, 3, 7, 10, 1, 2, 11, 12, 8, 9};
        for (int i = 0; i < arrA.length; i++) {
            arrA[i] = Debug.addSymbolicInt(arrA[i], "symA_"+i);   
        }
        for (int i = 0; i < arrB.length; i++) {
            arrB[i] = Debug.addSymbolicInt(arrB[i], "symB_"+i);   
        }
        Inverse(arrB, arrA);
        Debug.printPC("PC: ");
    }
}
