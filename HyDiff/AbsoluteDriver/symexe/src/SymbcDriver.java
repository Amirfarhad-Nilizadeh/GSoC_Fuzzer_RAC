     import java.io.*;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
      public static void main(String args[]) {
		short shortNum = 0;
		int intNum = 0;
		long longNum = 0;

	 	if (args.length == 1) {		
		
			try (FileInputStream fis = new FileInputStream(args[0])) {
				byte[] shortBytes = new byte[Short.BYTES];
				byte[] intBytes = new byte[Integer.BYTES];
				byte[] longBytes = new byte[Long.BYTES];

				if (fis.read(shortBytes) != -1)
					shortNum = (short)Debug.addSymbolicInt(ByteBuffer.wrap(shortBytes).getInt(), "sym_0");
				else
					throw new RuntimeException("too less data");


				if (fis.read(intBytes) != -1) 
					intNum = Debug.addSymbolicInt(ByteBuffer.wrap(intBytes).getInt(), "sym_1");
				else
					throw new RuntimeException("too less data");

				if (fis.read(longBytes) != -1) 
					longNum = Debug.addSymbolicInt(ByteBuffer.wrap(longBytes).getInt(), "sym_2");
				else
					throw new RuntimeException("too less data");
			

			} catch (IOException e) {
           			System.err.println("Error reading input");
            			e.printStackTrace();
            			return;
			}
		} else {

			shortNum = (short) Debug.makeSymbolicInteger("sym_0");
            		intNum = Debug.makeSymbolicInteger("sym_1");
			longNum = Debug.makeSymbolicInteger("sym_2");
               }

		System.out.println("shortNum=" + shortNum);
        	System.out.println("intNum=" + intNum);
		System.out.println("longNum=" + longNum);

	       AbsoluteDriver d = new AbsoluteDriver(shortNum, intNum, longNum);
	       d.driver();	
      }
}



    	
