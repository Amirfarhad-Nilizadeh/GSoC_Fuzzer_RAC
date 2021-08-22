     import java.io.File;
     import java.io.FileInputStream;
     import java.io.IOException;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {
		int option = 0;
		char _char = 'i';
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				option = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_0");

			if (fis.read(bytes) != -1) 
				_char = Debug.addSymbolicChar(ByteBuffer.wrap(bytes).getChar(), "sym_1");

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}
				 
	       } else {
            		option = Debug.makeSymbolicInteger("sym_0");
			 _char = Debug.makeSymbolicChar("sym_1");
              }
        
        	System.out.println("option = " + option);

		Alphabet a = new Alphabet(_char);
		a.driver(option);
      }
   }
