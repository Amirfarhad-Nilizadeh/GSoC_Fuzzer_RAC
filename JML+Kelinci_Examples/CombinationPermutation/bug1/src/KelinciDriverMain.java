     import java.io.FileInputStream;
     import java.io.File;
     import java.io.IOException;
     import java.nio.ByteBuffer;
     import java.io.FileWriter;
     import java.io.PrintWriter;
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

		int n = 0;
		int r = 0;
		int changeToBoolean = 0;
		boolean post = false;
		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				n = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				r = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				changeToBoolean = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");
			

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

		boolean flag = (changeToBoolean%2 == 0) ? true : false; 
		
		boolean precondition = RunRAC(n, r, flag, post);
		

		if (precondition) {
			post = true;
			boolean postcondition = RunRAC(n, r, flag, post);
			CombinationPermutation p = new CombinationPermutation();
			p.select(n, r, flag);	
			post = false;
		}
      }

      public static boolean RunRAC(int n, int r, boolean flag, boolean post) {
	     
		String _n = String.valueOf(n);
		String _r = String.valueOf(r);
		String _flag = String.valueOf(flag);

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		//driverArg = "JMLDriver " + _size + " >log.txt 2>&1";
		driverArg = "JMLDriver " + _n + " " + _r + " " + _flag;

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
