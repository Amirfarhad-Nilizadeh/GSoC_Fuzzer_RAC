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
	
		int balance = 0;
		int option = 0;
		int amount = 0;
		int previousTransaction = 0;
		int choosingConstructor = 0;
		boolean post = false;

		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				balance = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				option = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				amount = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				previousTransaction = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				choosingConstructor = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

		int flag = choosingConstructor%3; 

		if (flag < 0)
			flag = -flag;
		
		boolean precondition = RunRAC(balance, option, amount, previousTransaction, flag, post);

		if (precondition) {
			post = true;
			boolean postcondition = RunRAC(balance, option, amount, previousTransaction, flag, post);
			if (flag == 0) {
				BankAccount a = new BankAccount();
				a.menu(option, amount);	
			} else if (flag == 1) {	
				BankAccount b = new BankAccount(balance);
				b.menu(option, amount);	
			} else {
				BankAccount c = new BankAccount(balance, previousTransaction);
				c.menu(option, amount);	
	    		}
			post = false;	
		}	 	

        }

	 public static boolean RunRAC(int balance, int option, int amount, int previousTransaction, int flag, boolean post) {
	     
		String _balance = String.valueOf(balance);
		String _option = String.valueOf(option);
		String _amount = String.valueOf(amount);
		String _previousTransaction = String.valueOf(previousTransaction);
		String _flag = String.valueOf(flag);

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		driverArg = "JMLDriver " + _balance + " " + _option + " " + _amount + " " + _previousTransaction + " " + _flag;
		if (!post) {
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
			if (!result && post) {
				File writer = new File("counterexamples.txt");
				if (writer.exists() == false) {
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
