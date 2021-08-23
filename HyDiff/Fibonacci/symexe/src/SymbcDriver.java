     import java.io.*;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {
		int num = 90; 
        	int sel = 1;
		
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				num = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_0");

			if (fis.read(bytes) != -1) 
				sel = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_1");

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}
				 
	        } else {
            		num = Debug.makeSymbolicInteger("sym_0");
			sel = Debug.makeSymbolicInteger("sym_");
                }
        
        	System.out.println("num = " + num);
		System.out.println("sel = " + sel);

		if (sel == 1) {
            		Fibonacci Fib = new Fibonacci(num);
            		Fib.fibCompute();
        	} else {
            		Fibonacci Fib = new Fibonacci();
            		Fib.fibCompute();
        	}
	}
}
