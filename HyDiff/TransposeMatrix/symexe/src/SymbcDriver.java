     import java.io.File;
     import java.io.FileInputStream;
     import java.io.IOException;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {

		int[][] matrix = {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}};
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			for (int i = 0; i < matrix.length; i++) {
               			for (int j = 0; j < matrix[0].length ; j++) {
					if (fis.read(bytes) != -1) 
	                   			matrix[i][j] = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_"+i+j); 
               			}
           		}

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}
				 
	       } else {
			for (int i = 0; i < matrix.length; i++) {
               			for (int j = 0; j < matrix[0].length ; j++) {
            				matrix[i][j] = Debug.makeSymbolicInteger("sym_"+i+j);
				}
			}
              }
        

		TransposeMatrix obj = new TransposeMatrix();
                obj.transposeMat(matrix);
	}
}
