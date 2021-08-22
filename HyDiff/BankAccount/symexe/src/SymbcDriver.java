     import java.io.*;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {
		int option = 9;
        	int amount = 1000, currentBalance = 30000, previousTransaction = 1200;
        	int sel = 1;
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				option = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_0");

			if (fis.read(bytes) != -1) 
				amount = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_1");

			if (fis.read(bytes) != -1) 
				currentBalance = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_2");

			if (fis.read(bytes) != -1) 
				previousTransaction = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_3");

			if (fis.read(bytes) != -1) 
				sel = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_4");

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

				 
	       } else {
            		option = Debug.makeSymbolicInteger("sym_0");
            		amount = Debug.makeSymbolicInteger("sym_1");
			currentBalance = Debug.makeSymbolicInteger("sym_2");
            		previousTransaction = Debug.makeSymbolicInteger("sym_3");
			sel = Debug.makeSymbolicInteger("sym_4");
              }
        	System.out.println("option = " + option);
        	System.out.println("amount = " + amount);
		System.out.println("currentBalance = " + currentBalance);
        	System.out.println("previousTransaction = " + previousTransaction);
		System.out.println("sel = " + sel);

		if (sel == 0) {
            		BankAccount obj = new BankAccount();
            		obj.menu(option, amount);
        	} else if (sel == 1) {
            		BankAccount obj = new BankAccount(currentBalance);
            		obj.menu(option, amount);
        	}
        	else {
            		BankAccount obj = new BankAccount(currentBalance, previousTransaction);
            		obj.menu(option, amount);
        	}
	}
}
