     import java.io.*;
     import java.nio.ByteBuffer;
     public class FuzzDriver {	
     	private static String address ="/home/amirfarhad/Desktop/ProgramAnalysis/codes/GSoC_Fuzzer_RAC_GitHub/HyDiff/OddEven/jml";
	private static String addressPost ="/home/amirfarhad/Desktop/ProgramAnalysis/codes/GSoC_Fuzzer_RAC_GitHub/HyDiff/OddEven/jmlPost";
     	public static void main(String args[]) {

	 	if (args.length != 1) {
           		System.out.println("Expects file name as parameter");
           		return;
		}

		int num = 0;
		boolean flag = false;

		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1)
				num = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");				
			

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

		boolean precondition = RunRAC(num, flag);
		
		if (precondition) {
			flag = true;
			boolean postcondition = RunRAC(num, flag);
			OddEven obj = new OddEven();
                	obj.isEven(num);
			obj.isOdd(num);
			flag = false;
	         }
	}
	public static boolean RunRAC(int num, boolean flag) {
	     
		String _num = String.valueOf(num);

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		//driverArg = "JMLDriver " + _size + " >log.txt 2>&1";
		driverArg = "JMLDriver " + _num;
		
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
