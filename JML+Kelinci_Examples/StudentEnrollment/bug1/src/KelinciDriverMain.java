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

		int costPerCredit = 200;  
  		int totalCredits = 120;
 		int maxSemesterCredits = 20;
		int passedCredits = 0;
		int enrollmentCredits = 0;
		int payment = 0;
		int initialBalance = 0;
		boolean lateRegistration = true;
		boolean debit = true;
		boolean option = true;
		int intLateRegistration = 0;
		int intDebit = 0;
		int intOption = 0;
		boolean flag = false;
		

		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				passedCredits = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				enrollmentCredits = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				payment = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				initialBalance = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				intLateRegistration = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				intDebit = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				intOption = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

		if (intLateRegistration%2 == 0)
			lateRegistration = false;

		if (intDebit%2 == 0)
			debit = false;

		if (intOption%2 == 0)
			option = false;

		boolean precondition = RunRAC(passedCredits%121, enrollmentCredits%21, payment, initialBalance/1000000, intLateRegistration, intDebit, intOption, flag);

		if (precondition) {
			flag = true;
			boolean postcondition = RunRAC(passedCredits%121, enrollmentCredits%21, payment, initialBalance/1000000, intLateRegistration, intDebit, intOption, flag);
			StudentEnrollment a = new StudentEnrollment("AnyFirstName", "AnyLastName");
			a.enrollmentProcess(passedCredits%121, enrollmentCredits%21, payment, initialBalance/1000000, lateRegistration, debit, option);	
			flag = false;
		}	
				 
      }

      public static boolean RunRAC(int passedCredits, int enrollmentCredits, int payment, int initialBalance, int intLateRegistration, int intDebit, int intOption, boolean flag) {
	     
		String _passedCredits = String.valueOf(passedCredits);
		String _enrollmentCredits = String.valueOf(enrollmentCredits);
		String _payment = String.valueOf(payment);
		String _initialBalance = String.valueOf(initialBalance);
		String _intLateRegistration = String.valueOf(intLateRegistration);
		String _intDebit = String.valueOf(intDebit);
		String _intOption = String.valueOf(intOption);

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		//driverArg = "JMLDriver " + _size + " >log.txt 2>&1";
		driverArg = "JMLDriver " + _passedCredits + " " + _enrollmentCredits + " " + _payment + " " + _initialBalance + " " + _intLateRegistration + " " + intDebit + " " + intOption;
		
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
