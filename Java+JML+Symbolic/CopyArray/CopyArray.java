import gov.nasa.jpf.symbc.Debug;

public class CopyArray {
    //@ requires 0 < a.length && 0 < b.length;
    //@ requires 0 <= iBegin && 0 <= iEnd && iBegin <= iEnd;
    //@ requires iBegin < a.length && iBegin < b.length && iEnd < a.length && iEnd < b.length;
    //@ ensures (\forall int i; iBegin <= i && i < iEnd; a[i] == b[i]);
    public static void CopyArray(int[] b, int iBegin, int iEnd, int[] a) {
        int k = iBegin;
        //@ maintaining iBegin <= k && k <= iEnd;
        //@ maintaining (\forall int i; iBegin <= i && i < k; a[i] == b[i]);
        //@ decreases iEnd  - k;
        while (iEnd - k > 0) {
            a[k] = b[k];
            k = k + 1 ;
        }
    } 
    public static void main(String[] args) {
        int x = 1; 
        int y = 8;
        int[] arrA = {7, 11, 12, 8, 4, 6, 5, 0, 3, 9, 10, 1, 2};
        int[] arrB = {4, 6, 5, 0, 3, 7, 10, 1, 2, 11, 12, 8, 9, 13, 15, 14};
        
        for (int i = 0; i < arrA.length; i++) {
            arrA[i] = Debug.addSymbolicInt(arrA[i], "symA_"+i);   
        }
        for (int i = 0; i < arrB.length; i++) {
            arrB[i] = Debug.addSymbolicInt(arrB[i], "symB_"+i);   
        }
        x = Debug.addSymbolicInt(x, "sym_x");
        y = Debug.addSymbolicInt(y, "sym_y");
        
        // assert (x <= y && x < arrA.length && y < arrB.length);
        
        CopyArray(arrB,x, y, arrA);
        Debug.printPC("PC: ");
    }
}
