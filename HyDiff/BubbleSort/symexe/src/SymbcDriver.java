     import java.io.File;
     import java.io.FileInputStream;
     import java.io.IOException;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {

		int[] arr = {7, 11, 12, 8, 4, 6, 5, 0, 3, 9, 10, 1, 2};
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

		BubbleSort b = new BubbleSort();
               	b.bubbleSort(arr);
	}
}
