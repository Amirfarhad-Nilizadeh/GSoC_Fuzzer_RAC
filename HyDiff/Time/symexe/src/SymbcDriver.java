     import java.io.File;
     import java.io.FileInputStream;
     import java.io.IOException;
     import java.nio.ByteBuffer;
     import gov.nasa.jpf.symbc.Debug;
     public class SymbcDriver {	
     public static void main(String args[]) {
		int sel = 100; 
        	int x = 0, y = 0;
        	int s1 = 0, s2 = 12, m1 = 0, m2 = 12, h1 = 0, h2 = 12;
        	
	 	if (args.length == 1) {
           		
		try (FileInputStream fis = new FileInputStream(args[0])) {
			byte[] bytes = new byte[Integer.BYTES];

			if (fis.read(bytes) != -1) 
				sel = Debug.addSymbolicInt(sel, "sym_0");
			if (fis.read(bytes) != -1)
        			x = Debug.addSymbolicInt(x, "sym_1");
			if (fis.read(bytes) != -1)
 			       	y = Debug.addSymbolicInt(y, "sym_2");
			if (fis.read(bytes) != -1)
        			h1 = Debug.addSymbolicInt(h1, "sym_3");
			if (fis.read(bytes) != -1)
        			h2 = Debug.addSymbolicInt(h2, "sym_4");
			if (fis.read(bytes) != -1)
        			m1 = Debug.addSymbolicInt(m1, "sym_5");
			if (fis.read(bytes) != -1)
        			m2 = Debug.addSymbolicInt(m2, "sym_6");
			if (fis.read(bytes) != -1)
        			s1 = Debug.addSymbolicInt(s1, "sym_7");
			if (fis.read(bytes) != -1)
        			s2 = Debug.addSymbolicInt(s2, "sym_8");
		} catch (IOException e) {
           		System.err.println("Error reading input");
            		e.printStackTrace();
            		return;
		}
				 
	       } else {
			sel = Debug.makeSymbolicInteger("sym_0");
			x = Debug.makeSymbolicInteger("sym_1");
			y = Debug.makeSymbolicInteger("sym_2");
			h1 = Debug.makeSymbolicInteger("sym_3");
			h2 = Debug.makeSymbolicInteger("sym_4");
			m1 = Debug.makeSymbolicInteger("sym_5");
			m2 = Debug.makeSymbolicInteger("sym_6");
			s1 = Debug.makeSymbolicInteger("sym_7");
			s2 = Debug.makeSymbolicInteger("sym_8");
              }

		Time time = new Time();
        	if (x == 0 && y == 0) {
            		Time timeA = new Time();
            		Time timeB = new Time();
            		time.timeOptions(timeA, timeB, sel);
        	} else if (x == 1 && y == 0) {
            		Time timeA = new Time(h1, m1, s1);
            		Time timeB = new Time();
            		time.timeOptions(timeA, timeB, sel);
        	} else if (x == 0 && y == 1) {
            		Time timeA = new Time();
            		Time timeB = new Time(h2, m2, s2);
            		time.timeOptions(timeB, timeA, sel); // TimeA is later than TimeB. Thus, TimeB is considered as a start.
        	} else {
            		Time timeA = new Time(h1, m1, s1);
            		Time timeB = new Time(h2, m2, s2);
            		if (timeA.later_than(timeB)) 
                  		time.timeOptions(timeB, timeA, sel); 
            		else
                  		time.timeOptions(timeA, timeB, sel);
        	}
	}
}
