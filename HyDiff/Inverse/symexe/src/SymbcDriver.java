     import java.io.*;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {
		int[] arrA = {7, 11, 12, 8, 4, 6, 5, 3, 0, 5, 10, 6, 4};
        	int[] arrB = {4, 6, 5, 0, 3, 7, 10, 1, 2, 11, 12, 8, 9};
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			for (int i = 0; i < arrA.length; i++) {
				if (fis.read(bytes) != -1) 
					arrA[i] = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "symA_"+i);
			}
			for (int i = 0; i < arrB.length; i++) {
				if (fis.read(bytes) != -1) 
					arrB[i] = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "symB_"+i);
			}
			
		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

				 
	       } else {
			for (int i = 0; i < arrA.length; i++) 
            			arrA[i] = Debug.makeSymbolicInteger("symA_"+i);

			for (int i = 0; i < arrB.length; i++) 
            			arrB[i] = Debug.makeSymbolicInteger("symB_"+i);
              }
        
		System.out.print("arrA = { ");
		for (int i = 0; i < arrA.length - 1; i++) {
        		System.out.print(arrA[i] + ", ");
		}
		System.out.print(" }");

		System.out.println("arrB = { ");
		for (int i = 0; i < arrB.length - 1; i++) {
        		System.out.print(arrB[i] + ", ");
		}
		System.out.print(" }");

		Inverse obj = new Inverse();
               	obj.Inverse(arrA, arrB);
	}
}
