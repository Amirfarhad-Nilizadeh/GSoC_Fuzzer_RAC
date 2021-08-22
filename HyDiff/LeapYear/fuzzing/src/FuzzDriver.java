     import java.io.*;
     import java.nio.ByteBuffer;
     public class FuzzDriver {	 
    	 private static String address ="/home/amirfarhad/Desktop/ProgramAnalysis/codes/GSoC_Fuzzer_RAC_GitHub/HyDiff/LeapYear/jml";
	 private static String addressPost ="/home/amirfarhad/Desktop/ProgramAnalysis/codes/GSoC_Fuzzer_RAC_GitHub/HyDiff/LeapYear/jmlPost";
    	 public static void main(String args[]) {

	 	if (args.length != 1) {
           		System.out.println("Expects file name as parameter");
           		return;
		}
		boolean flag = false;
		int year = 0;
		int constructor = 0;
		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				year = (ByteBuffer.wrap(bytes).getInt());
			else
				throw new RuntimeException("too less data");

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}
		
		boolean precondition = RunRAC(year, flag);
		

		if (precondition) {
			flag = true;
			boolean postcondition = RunRAC(year, flag);
			LeapYear y = new LeapYear();
			y.isLeapYear(year);
			flag = false;
		}
      }

      public static boolean RunRAC(int year, boolean flag) {
	     
		String _year = String.valueOf(year);

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		//driverArg = "JMLDriver " + _size + " >log.txt 2>&1";
		driverArg = "JMLDriver " + _year;
		
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
