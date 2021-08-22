     import java.io.*;
     import java.nio.ByteBuffer;
     public class KelinciDriverMain {	 
     private static String address ="jml"; 
     private static String addressPost ="jmlPost";
     public static void main(String args[]) {

	 	if (args.length != 1) {
           		System.out.println("Expects file name as parameter");
           		return;
		}

		int range = 1000;
		int arraySize = 20;
		int search = 0;
		int i;
		boolean flag = false;
		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1)
				arraySize = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");	

			arraySize%=range; // This is not for Specification. "arraySize%range" is for memory limitation of System.

			int array[] = new int[arraySize];   

			for (i = 0; i <arraySize; i++) {
				if (fis.read(bytes) != -1)
					array[i] = ByteBuffer.wrap(bytes).getInt();
				else
					throw new RuntimeException("too less data");						
			}

			if (fis.read(bytes) != -1)
				search = (ByteBuffer.wrap(bytes).getInt());
			else
				throw new RuntimeException("too less data");

			boolean precondition = RunRAC(search, array, flag);
		
			if (precondition) {
				flag = true;
				boolean postcondition = RunRAC(search, array, flag);
				LinearSearch l = new LinearSearch();
        	        	l.linearSearch(search, array);	
				flag = false;	
	        	}	 
	
		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}
		

	 }
	 public static boolean RunRAC(int search, int[] arr, boolean flag) {
	     
		String _search = String.valueOf(search);
		String[] strs = new String[arr.length];	
		String arrArg = "";

        	for (int i = 0; i < arr.length; i++) {
            		strs[i] = String.valueOf(arr[i]);
       		}

		if (arr.length != 0) {
			arrArg = strs[0];
        		for (int i = 1; i < strs.length; i++) {
            			arrArg += " " + strs[i];
       			}
		}


		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		//driverArg = "JMLDriver " + _size + " >log.txt 2>&1";
		driverArg = "JMLDriver " + arrArg + " " + _search;
		
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
			System.err.println("Error in the RunRac method!");
		}
		return result;
     	}
      }
