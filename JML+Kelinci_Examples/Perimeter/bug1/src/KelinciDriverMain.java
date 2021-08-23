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

		int select = 0;
		int num1 = 0;
		int num2 = 0;
		int num3 = 0;
		int num4 = 0;
		short shortNum = 0;
		long longNum = 0;
		boolean flag = false;
		

		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];
			byte[] ShortBytes = new byte[Short.BYTES];
			byte[] LongBytes = new byte[Long.BYTES];

			if (fis.read(bytes) != -1) 
				select = ByteBuffer.wrap(bytes).getInt();

			if (fis.read(bytes) != -1) 
				num1 = ByteBuffer.wrap(bytes).getInt();

			if (fis.read(bytes) != -1) 
				num2 = ByteBuffer.wrap(bytes).getInt();
			
			if (fis.read(bytes) != -1) 
				num3 = ByteBuffer.wrap(bytes).getInt();

			if (fis.read(bytes) != -1) 
				num4 = ByteBuffer.wrap(bytes).getInt();


			if (fis.read(ShortBytes) != -1) 
				shortNum = ByteBuffer.wrap(ShortBytes).getShort();

			if (fis.read(LongBytes) != -1) 
				longNum = ByteBuffer.wrap(LongBytes).getLong();
			
			

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

		boolean precondition = RunRAC(select, num1, num2, num3, num4, shortNum, longNum, flag);

		if (precondition) {
			flag = true;
			boolean postcondition = RunRAC(select, num1, num2, num3, num4, shortNum, longNum, flag);
			PerimeterDriver p = new PerimeterDriver();
			p.driver(select, num1, num2, num3, num4, shortNum, longNum);
			flag = false;	
		}	 
	 }

	public static boolean RunRAC(int select, int num1, int num2, int num3, int num4, short shortNum, long longNum, boolean flag) {
	     
		String _select = String.valueOf(select);
		String _num1 = String.valueOf(num1);
		String _num2 = String.valueOf(num2);
		String _num3 = String.valueOf(num3);
		String _num4 = String.valueOf(num4);
		String _shortNum = String.valueOf(shortNum);
		String _longNum = String.valueOf(longNum);

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		driverArg = "JMLDriver " + _select + " " + _num1 + " " + _num2 + " " + _num3 + " " + _num4 + " " + _shortNum + " " + _longNum;
		
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
