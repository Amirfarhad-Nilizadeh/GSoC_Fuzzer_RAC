     import java.io.FileInputStream;
     import java.io.File;
     import java.io.IOException;
     import java.io.FileWriter;
     import java.io.PrintWriter;  
     import java.nio.ByteBuffer;
     import edu.cmu.sv.kelinci.Kelinci;
     import edu.cmu.sv.kelinci.Mem;

     public class FuzzDriver {	 
    	 private static String address ="/home/amirfarhad/Desktop/ProgramAnalysis/codes/GSoC_Fuzzer_RAC_GitHub/HyDiff/Absolute/jml";
	 private static String addressPost ="/home/amirfarhad/Desktop/ProgramAnalysis/codes/GSoC_Fuzzer_RAC_GitHub/HyDiff/Absolute/jmlPost";
    	 public static void main(String args[]) {

	 	if (args.length != 1) {
           		System.out.println("Expects file name as parameter");
           		return;
		}

		short shortNum = 0;
		int intNum = 0;
		long longNum = 0;
		boolean flag = false;
		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] shortBytes = new byte[Short.BYTES];
			byte[] intBytes = new byte[Integer.BYTES];
			byte[] longBytes = new byte[Long.BYTES];

			if (fis.read(shortBytes) != -1)
				shortNum = ByteBuffer.wrap(shortBytes).getShort();
			else
				throw new RuntimeException("too less data");


			if (fis.read(intBytes) != -1) 
				intNum = ByteBuffer.wrap(intBytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(longBytes) != -1) 
				longNum = ByteBuffer.wrap(longBytes).getLong();
			else
				throw new RuntimeException("too less data");
			

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

		
		boolean precondition = RunRAC(shortNum, intNum, longNum, flag);
		

		if (precondition) {
			flag = true;
			boolean postcondition = RunRAC(shortNum, intNum, longNum, flag);
			AbsoluteDriver d = new AbsoluteDriver(shortNum, intNum, longNum);
			d.driver();
			flag = false;	
		}
      }

      public static boolean RunRAC(short shortNum, int intNum, long longNum, boolean flag) {
	     
		String _shortNum = String.valueOf(shortNum);
		String _intNum = String.valueOf(intNum);
		String _longNum = String.valueOf(longNum);

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";
		driverArg = "JMLDriver " + _shortNum + " " + _intNum + " " + _longNum;

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
