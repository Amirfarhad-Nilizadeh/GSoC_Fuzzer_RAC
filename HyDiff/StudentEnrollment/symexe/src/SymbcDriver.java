     import java.io.*;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
      public static void main(String args[]) {
		int passedCredits = 40, semesterCredits = 15, payment = 1000, initialBalance = 100, intLateRegistration = 0, intDebit = 0, intOption = 0;

	 	if (args.length == 1) {		
		
			try (FileInputStream fis = new FileInputStream(args[0])) {
				byte[] bytes = new byte[Integer.BYTES];

				
				if (fis.read(bytes) != -1) 
					passedCredits = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_0");
				else
					throw new RuntimeException("too less data");

				if (fis.read(bytes) != -1) 
					semesterCredits = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_1");
				else
					throw new RuntimeException("too less data");

				if (fis.read(bytes) != -1) 
					payment = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_2");
				else
					throw new RuntimeException("too less data");

				if (fis.read(bytes) != -1) 
					initialBalance = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_3");
				else
					throw new RuntimeException("too less data");

				if (fis.read(bytes) != -1) 
					intLateRegistration = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_4");
				else
					throw new RuntimeException("too less data");

				if (fis.read(bytes) != -1) 
					intDebit = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_5");
				else
					throw new RuntimeException("too less data");

				if (fis.read(bytes) != -1) 
					intOption = Debug.addSymbolicInt(ByteBuffer.wrap(bytes).getInt(), "sym_6");
				else
					throw new RuntimeException("too less data");
			

			} catch (IOException e) {
           			System.err.println("Error reading input");
            			e.printStackTrace();
            			return;
			}
		} else {
			passedCredits = Debug.makeSymbolicInteger("sym_0");
			semesterCredits = Debug.makeSymbolicInteger("sym_1");
			payment = Debug.makeSymbolicInteger("sym_2");
			initialBalance = Debug.makeSymbolicInteger("sym_3");
			intLateRegistration = Debug.makeSymbolicInteger("sym_4");
			intDebit = Debug.makeSymbolicInteger("sym_5");
			intOption = Debug.makeSymbolicInteger("sym_6");
               }


	       StudentEnrollment obj = new StudentEnrollment("firstName", "secondName");
	       boolean lateRegistration = (intLateRegistration == 0);
               boolean debit = (intDebit == 0);
               boolean option = (intOption == 0);
               obj.enrollmentProcess(passedCredits, semesterCredits, payment, initialBalance, lateRegistration, debit, option);
      }
}



    	
