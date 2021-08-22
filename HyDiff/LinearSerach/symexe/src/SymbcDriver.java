     import java.io.File;
     import java.io.FileInputStream;
     import java.io.IOException;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {
		int key = 6;
		int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				key = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_0");
			for (int i = 1; i <= arr.length; i++) {
				if (fis.read(bytes) != -1) 
					arr[i-1] = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_"+i);
			}
		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

				 
	       } else {
            		key = Debug.makeSymbolicInteger("sym_0");
			for (int i = 1; i <= arr.length; i++) 
            			arr[i-1] = Debug.makeSymbolicInteger("sym_"+i);
              }
        
        	System.out.println("key = " + key);
		System.out.print("{ ");
		for (int i = 0; i < arr.length - 1; i++) {
        		System.out.print(arr[i] + ", ");
		}
		System.out.print(arr[arr.length -1] + " }");

		LinearSearch obj = new LinearSearch();
        	obj.linearSearch(key, arr);	
	}
}
