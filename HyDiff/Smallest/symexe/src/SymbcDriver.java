     import java.io.*;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {

		int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 8};
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			for (int i = 0; i < arr.length; i++) {
				if (fis.read(bytes) != -1) 
					arr[i] = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_"+i);
			}
		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}
				 
	       } else {
			for (int i = 0; i < arr.length; i++) 
            			arr[i] = Debug.makeSymbolicInteger("sym_"+i);
              }
        
		System.out.print("{ ");
		for (int i = 0; i < arr.length - 1; i++) {
        		System.out.print(arr[i] + ", ");
		}
		System.out.print(" }");

		Smallest obj = new Smallest();
                obj.Smallest(arr);
	}
}
