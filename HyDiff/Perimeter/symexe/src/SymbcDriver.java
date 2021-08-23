     import java.io.*;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
      public static void main(String args[]) {
		int sel = 5, w = 5, x = 5, y= 5, z = 5;
         	short shortNum = 5;
         	long longNum = 5;

	 	if (args.length == 1) {		
		
			try (FileInputStream fis = new FileInputStream(args[0])) {
				byte[] shortBytes = new byte[Short.BYTES];
				byte[] intBytes = new byte[Integer.BYTES];
				byte[] longBytes = new byte[Long.BYTES];

				if (fis.read(shortBytes) != -1)
					shortNum = (short)Debug.addSymbolicInt(ByteBuffer.wrap(shortBytes).getInt(), "sym_0");
				else
					throw new RuntimeException("too less data");

				if (fis.read(longBytes) != -1) 
					longNum = Debug.addSymbolicInt(ByteBuffer.wrap(longBytes).getInt(), "sym_1");
				else
					throw new RuntimeException("too less data");
				
				if (fis.read(intBytes) != -1) 
					sel = Debug.addSymbolicInt(ByteBuffer.wrap(intBytes).getInt(), "sym_2");
				else
					throw new RuntimeException("too less data");

				if (fis.read(intBytes) != -1) 
					w = Debug.addSymbolicInt(ByteBuffer.wrap(intBytes).getInt(), "sym_3");
				else
					throw new RuntimeException("too less data");

				if (fis.read(intBytes) != -1) 
					x = Debug.addSymbolicInt(ByteBuffer.wrap(intBytes).getInt(), "sym_4");
				else
					throw new RuntimeException("too less data");

				if (fis.read(intBytes) != -1) 
					y = Debug.addSymbolicInt(ByteBuffer.wrap(intBytes).getInt(), "sym_5");
				else
					throw new RuntimeException("too less data");

				if (fis.read(intBytes) != -1) 
					z = Debug.addSymbolicInt(ByteBuffer.wrap(intBytes).getInt(), "sym_6");
				else
					throw new RuntimeException("too less data");
			

			} catch (IOException e) {
           			System.err.println("Error reading input");
            			e.printStackTrace();
            			return;
			}
		} else {

			shortNum = (short) Debug.makeSymbolicInteger("sym_0");
			longNum = Debug.makeSymbolicInteger("sym_1");
			sel = Debug.makeSymbolicInteger("sym_2");
			w = Debug.makeSymbolicInteger("sym_3");
			x = Debug.makeSymbolicInteger("sym_4");
			y = Debug.makeSymbolicInteger("sym_5");
			z = Debug.makeSymbolicInteger("sym_6");
               }


	       PerimeterDriver obj = new PerimeterDriver();
	       obj.driver(sel, w, x, y, z, shortNum, longNum);	
      }
}



    	
