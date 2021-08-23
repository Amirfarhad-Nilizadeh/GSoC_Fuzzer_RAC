     import java.io.*;    
     import java.util.ArrayList;
     import java.util.List;
     import java.nio.ByteBuffer;
     public class KelinciDriverMain {	
     private static String address ="jml";
     private static String addressPost ="jmlPost";
     public static void main(String args[]) {

	 	if (args.length != 1) {
           		System.out.println("Expects file name as parameter");
           		return;
		}
		
		int range = 100;
		int StringSize = 0;
		char[] Array_inputString;
		String inputString;
		List<Character> values = new ArrayList<>();
		boolean flag = false;

		try (FileInputStream fis = new FileInputStream(args[0])) {
		
			 byte[] bytes = new byte[Integer.BYTES];

			 int i = 0;
			 int j = 0;
            		 int value;


			 if (fis.read(bytes) != -1) 
				StringSize = ByteBuffer.wrap(bytes).getInt();  //length of String
			 else
				throw new RuntimeException("too less data");

			 StringSize %= range;  // This is not for Specification. "StringSize %= range;" is for memory limitation of System.

            		 while (((value = fis.read(bytes)) != -1) && (i < StringSize)) {
				value = ByteBuffer.wrap(bytes).getInt();  
				value = value % 94; // Not for Specification. ASCII codes are visible for testing
				if (value < 0) 
					value = -value;

				value += 33;  //visible characters are between 33 to 126
               			char charValue = (char)value;
				values.add(charValue);
                    		i++;
			 }

		         if (i != StringSize) {
			    throw new RuntimeException("too less data");
		         }


			 Array_inputString = new char[values.size()];
            			for (i = 0; i < values.size(); i++) {
               				Array_inputString[i] = values.get(i);
            			}	
			inputString = new String(Array_inputString);

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

		boolean precondition = RunRAC(inputString, flag);
		

		if (precondition) {
			flag = true;
			boolean postcondition = RunRAC(inputString, flag);
			StrPalindrome p = new StrPalindrome();
                	p.isPalindrome(inputString);
			flag = false;	
		}
			 
	 }

	public static boolean RunRAC(String inputString, boolean flag) {
	     

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		//driverArg = "JMLDriver " + _size + " >log.txt 2>&1";
		driverArg = "JMLDriver " + inputString;
		
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
