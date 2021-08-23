     import java.io.*;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     	public static void main(String args[]) {
		int key = 90; 
        	int sel = 1;
       		int[] arr = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
	 	if (args.length == 1) {
           		
			try (FileInputStream fis = new FileInputStream(args[0])) {
				byte[] bytes = new byte[Integer.BYTES];

				if (fis.read(bytes) != -1) 
					key = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_0");

				if (fis.read(bytes) != -1) 
					sel = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_1");

				for (int i = 2; i <= arr.length + 1; i++) {
					if (fis.read(bytes) != -1) 
						arr[i-2] = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_"+i);
				}
			} catch (IOException e) {
           			System.err.println("Error reading input");
            			e.printStackTrace();
            			return;
			}	 
	       	} else {
            		key = Debug.makeSymbolicInteger("sym_0");
			sel = Debug.makeSymbolicInteger("sym_1");
			for (int i = 2; i <= arr.length + 1 ; i++) 
            			arr[i-2] = Debug.makeSymbolicInteger("sym_"+i);
               }

	       if (sel == 1) {
            		FindInArray obj = new FindInArray(arr);
            		obj.isMoreThanOneKey();
       	       } else {
            		FindInArray obj = new FindInArray(arr, key);
           		obj.isMoreThanOneKey();
               }
	}
    }
