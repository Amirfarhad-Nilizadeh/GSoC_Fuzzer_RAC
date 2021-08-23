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

		int size = 0;
		int constructor = 0;
		boolean flag = false;
		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				size = (ByteBuffer.wrap(bytes).getInt());
			else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1) 
				constructor = (ByteBuffer.wrap(bytes).getInt());
			else
				throw new RuntimeException("too less data");
		

		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}

		int select = constructor%5;
		
		boolean precondition = RunRAC(select, size, flag);
		
		if (precondition) {
			flag = true;
			boolean postcondition =RunRAC(select, size, flag);
			if (select == 0) {
				Fibonacci f = new Fibonacci();
				f.fibCompute();
                		f.getFib(1);	
			} else {
  				Fibonacci f = new Fibonacci(size);
				f.fibCompute();
				f.getFib(size-1);
	 		}
			flag = false;
		}
      }

      public static boolean RunRAC(int select, int size, boolean flag) {
	     
		String _select = String.valueOf(select);
		String _size = String.valueOf(size);

		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		//driverArg = "JMLDriver " + _size + " >log.txt 2>&1";
		driverArg = "JMLDriver " + _select + " " + _size;
		
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
