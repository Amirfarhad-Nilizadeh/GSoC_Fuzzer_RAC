     import java.io.File;
     import java.io.FileInputStream;
     import java.util.ArrayList;
     import java.util.List;
     import java.io.FileWriter;
     import java.io.PrintWriter;
     import java.io.IOException;
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

		try (FileInputStream fis = new FileInputStream(args[0])) {
		
			 byte[] bytes = new byte[Integer.BYTES];
			 int option = 0;
 			 int _char = 1000;
			 boolean flag = false;

			 if (fis.read(bytes) != -1) 
				option = ByteBuffer.wrap(bytes).getInt();  
			 else
				throw new RuntimeException("too less data");

			if (fis.read(bytes) != -1)
				_char = ByteBuffer.wrap(bytes).getInt();
			else
				throw new RuntimeException("too less data");

                	_char = _char % 65536;
			if (_char < 0)
				_char = -_char;
 				
 			boolean precondition = RunRAC(option, _char, flag);

			if (precondition) {
				flag = true;
				boolean postcondition = RunRAC(option, _char, flag);
				Alphabet a = new Alphabet((char)_char);
				a.driver(option);
				flag = false;	
			}	
			
		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}
	 }
	 public static boolean RunRAC(int option, int _char, boolean flag) {
	     
		String _option = String.valueOf(option);
		String _num = String.valueOf(_char);
	
		ProcessBuilder builderProg = new ProcessBuilder();
		String driverArg = "";

		//driverArg = "JMLDriver " + _size + " >log.txt 2>&1";
		driverArg = "JMLDriver " + _option + " " + _num;
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
