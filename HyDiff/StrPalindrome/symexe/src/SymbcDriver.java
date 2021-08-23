     import java.io.*;
     import java.util.Arrays;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {

		char[] char_arr = {'r', 'e', 'f', 'e', 'r'};
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			for (int i = 0; i < char_arr.length; i++) {
				if (fis.read(bytes) != -1) 
					char_arr[i] = Debug.addSymbolicChar(ByteBuffer.wrap(bytes).getChar(), "sym_"+i);
			}
		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

				 
	       } else {
			for (int i = 0; i < char_arr.length; i++) 
            			char_arr[i] = Debug.addSymbolicChar(char_arr[i], "sym_" + i);
              }
        
		System.out.print("{ ");
		for (int i = 0; i < char_arr.length - 1; i++) {
        		System.out.print(char_arr[i] + ", ");
		}
		System.out.print(char_arr[char_arr.length - 1]);
		System.out.print(" }");

		StrPalindrome obj = new StrPalindrome();
               	String str = Arrays.toString(char_arr);
       		obj.isPalindrome(str);
	}
}
