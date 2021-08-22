     import java.io.File;
     import java.io.FileInputStream;
     import java.io.IOException;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {
		int x = 15;
		int y = 4;
		int tmp = 0;
        	boolean flag = true;
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				x = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_0");

			if (fis.read(bytes) != -1) 
				y = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_1");

			if (fis.read(bytes) != -1) 
				tmp = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_2");

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

				 
	       } else {
            		x = Debug.makeSymbolicInteger("sym_0");
            		y = Debug.makeSymbolicInteger("sym_1");
			tmp = Debug.makeSymbolicInteger("sym_1");
              }
        
        	System.out.println("num1 = " + x);
        	System.out.println("num2 = " + y);
		System.out.println("tmp = " + tmp);

		flag = (tmp != 0);

		CombinationPermutation p = new CombinationPermutation();
		p.select(x, y, flag);
	}
}
