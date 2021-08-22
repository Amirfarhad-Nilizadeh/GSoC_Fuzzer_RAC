     import java.io.*;
     import java.nio.ByteBuffer;
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
		
		int num1 = 0;
		int num2 = 0;
		boolean flag = false;
		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				num1 = ByteBuffer.wrap(bytes).getInt();

			if (fis.read(bytes) != -1) 
				num2 = ByteBuffer.wrap(bytes).getInt();
		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

		boolean precondition = RunRAC(num1, num2, flag);

		if (precondition) {
			flag = true;
			boolean postcondition = RunRAC(num1, num2, flag);
			AddLoop add = new AddLoop();
			add.AddLoop(num1, num2);
			flag = false;	
		}	 
	 }
	 public static boolean RunRAC(int num1, int num2, boolean flag) {
	     
		String _num1 = String.valueOf(num1);
		String _num2 = String.valueOf(num2);

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		//driverArg = "JMLDriver " + _size + " >log.txt 2>&1";
		driverArg = "JMLDriver " + _num1 + " " + _num2;
		
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
