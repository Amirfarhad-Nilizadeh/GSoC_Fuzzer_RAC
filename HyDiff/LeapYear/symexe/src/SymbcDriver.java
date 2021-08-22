     import java.io.*;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {
		int year = 100;
		
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				year = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_0");

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}
				 
	       } else {
            		year = Debug.makeSymbolicInteger("sym_0");
              }
        
        	System.out.println("year = " + year);

		LeapYear obj = new LeapYear();
		obj.isLeapYear(year);
	}
}
