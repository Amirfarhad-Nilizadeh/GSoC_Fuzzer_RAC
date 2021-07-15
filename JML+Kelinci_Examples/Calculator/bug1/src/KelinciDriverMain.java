     import java.io.FileInputStream;
     import java.io.File;
     import java.io.FileInputStream;
     import java.io.IOException;
     import java.nio.ByteBuffer;
     import java.io.FileWriter;
     import java.io.PrintWriter;
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
		int intop = 0;
		char op = '+';
		boolean flag = false;

		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				num1 = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				num2 = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				intop = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			op = (char)intop;	
		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}
		boolean precondition = RunRAC(num1, num2, op, flag);
		
		if (precondition) {
			flag = true;
			boolean postcondition = RunRAC(num1, num2, op, flag);
			Calculator cal = new Calculator();
			cal.calculate(num1, num2, op);
			flag = false;
		}	
	 }

	public static boolean RunRAC(int num1, int num2, char op, boolean flag) {
	     
		String _num1 = String.valueOf(num1);
		String _num2 = String.valueOf(num2);

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		driverArg = "JMLDriver " + _num1 + " " + _num2 + " " + "\\"+ op;  //we add a backslash before the character. Some special characters like & and ' were making problems for running in the JMLDriver.
		
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
