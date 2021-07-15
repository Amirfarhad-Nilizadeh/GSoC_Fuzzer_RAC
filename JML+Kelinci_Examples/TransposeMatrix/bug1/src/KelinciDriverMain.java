     import java.io.File;
     import java.io.FileInputStream;
     import java.io.IOException;
     import java.nio.ByteBuffer;
     import java.io.FileWriter;
     import java.io.PrintWriter; 
     import edu.cmu.sv.kelinci.Kelinci;
     import edu.cmu.sv.kelinci.Mem;

     public class KelinciDriverMain {	 
       private static String address ="jml";
       private static String addressPost ="jmlPost"; 
       public static void main(String args[]) {

	 	if (args.length != 1) {
           		System.out.println("Expects file name as parameter");
           		return;
		}

		int range = 80;
		int arraySize1 = 20;
		int arraySize2 = 20;
		boolean flag = false;
		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				arraySize1 = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");
			
			if (fis.read(bytes) != -1)
				arraySize2 = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			arraySize1%=range; // This is not for Specification. "arraySize1%range" is for memory limitation of System.
			arraySize2%=range; // This is not for Specification. "arraySize2%range" is for memory limitation of System.

			int matrix[][] = new int[arraySize1][arraySize2]; 

			for (int i = 0; i < arraySize1; i++){ 
				for (int j = 0; j < arraySize2; j++){
					if (fis.read(bytes) != -1) 
						matrix[i][j] = ByteBuffer.wrap(bytes).getInt();					
					else
						throw new RuntimeException("too less data");
				}
			}
			boolean precondition = RunRAC(matrix, flag);
		
			if (precondition) {
				flag = true;
				boolean postcondition = RunRAC(matrix, flag);
				TransposeMatrix p = new TransposeMatrix();
                		p.transposeMat(matrix);
				flag = false;				
	        	}	
			
		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}	 
	 }
	 public static boolean RunRAC(int[][] mat, boolean flag) {
		String size1 = String.valueOf(mat.length);
		String size2 = String.valueOf(mat[0].length);
	     
		String[] strs = new String[(mat.length)*(mat[0].length)];	
		String matArg = "";
		int k = 0;
        	for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
            			strs[k] = String.valueOf(mat[i][j]);
				k++;
			}
       		}

		if (mat.length != 0) {
			matArg = strs[0];
        		for (int i = 1; i < strs.length; i++) {
            			matArg += " " + strs[i];
       			}
		}

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		driverArg = "JMLDriver " + size1 + " " + size2 + " " + matArg;
		if (!flag) {	
			builderProg.command("sh", "-c", "runrac " + driverArg);
			builderProg.directory(new File(address));
		} else {
			builderProg.command("sh", "-c", "runrac -Dorg.jmlspecs.openjml.racexceptions " + driverArg);
			builderProg.directory(new File(addressPost));
		}
		Process process = null;
		boolean result = false;
		try {
			process = builderProg.start();
			process.waitFor();
			result = process.exitValue() == 0;
			if (!result && flag) {
				File writer = new File("counterexamples.txt");
				if (writer.exists()==false) {
            				System.out.println("We had to make a new file.");
            				writer.createNewFile();
    				}
				PrintWriter printWriter = new PrintWriter(new FileWriter(writer, true));
				printWriter.println("RAC Violation input : " + driverArg);
				printWriter.close();
			}	
		} catch (Exception e) {	
			e.printStackTrace();	
			System.err.println("Error in the RunRac method");
		}
		return result;
     	}
      }
