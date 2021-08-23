     import java.io.*;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {
		int num = 0;
		
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				num = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_0");

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}
				 
	       } else {
            		num = Debug.makeSymbolicInteger("sym_0");
              }
        
        	System.out.println("num = " + num);

		OddEven obj = new OddEven();
                obj.isEven(num);
		obj.isOdd(num);
	}
}
