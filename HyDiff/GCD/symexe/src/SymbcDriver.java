     import java.io.File;
     import java.io.FileInputStream;
     import java.io.IOException;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {
		int num1 = 0;
		int num2 = 0;
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				num1 = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_0");

			if (fis.read(bytes) != -1) 
				num2 = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_1");
		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

				 
	       } else {
            		num1 = Debug.makeSymbolicInteger("sym_0");
            		num2 = Debug.makeSymbolicInteger("sym_1");
              }
        
        	System.out.println("num1=" + num1);
        	System.out.println("num2=" + num2);

		GCD g = new GCD();
		g.gcd(num1, num2);
	}
}
