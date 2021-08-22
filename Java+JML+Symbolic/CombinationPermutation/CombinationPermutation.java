import gov.nasa.jpf.symbc.Debug;

public class CombinationPermutation {
    //@ requires 0 <= n && n <= 20 && 0 <= r && r <= n;
    //@ old Factorial fac_spec = new Factorial();
    //@ ensures \result == fac_spec.spec_factorial(n)/(fac_spec.spec_factorial(r) * fac_spec.spec_factorial(n-r));
        private /* pure @*/ long combination(int n, int r) {
        Factorial fac = new Factorial();
        long combin;
        combin = fac.factorial(n) / (fac.factorial(r) * fac.factorial(n-r));
        return combin;
    }

    //@ requires 0 <= n && n <= 20 && 0 <= r && r <= n;
    //@ old Factorial fac_spec = new Factorial();
    //@ ensures \result == fac_spec.spec_factorial(n)/fac_spec.spec_factorial(n-r);
    private /* pure @*/ long permutation(int n, int r) {
        Factorial fac = new Factorial();
        long permut;
        permut = fac.factorial(n) / fac.factorial(n-r);
        return permut;
    }

    /*@ old Factorial fac_spec = new Factorial();
      @ requires 0 <= n && n <= 20 && 0 <= r && r <= n;     
      @ {|      
      @ requires flag;  
      @     ensures \result == fac_spec.spec_factorial(n)/(fac_spec.spec_factorial(r) * fac_spec.spec_factorial(n-r));
      @ also
      @ requires !flag;
      @ ensures \result == fac_spec.spec_factorial(n)/fac_spec.spec_factorial(n-r); |} @*/
    public /* pure @*/ long select(int n, int r, boolean flag) {
        return flag ? combination(n, r) : permutation(n, r);
    }
    
    public static void main(String[] args) {
        int x = 15; 
        int y = 4;
	int tmp = 0;
        boolean z = true;
        CombinationPermutation Obj = new CombinationPermutation();
        x = Debug.addSymbolicInt(x, "sym_x");
        y = Debug.addSymbolicInt(y, "sym_y");
        tmp = Debug.addSymbolicInt(tmp, "sym_tmp");
	z = (tmp != 0);
        Obj.select(x,y,z);
        Debug.printPC("PC: ");
    }
}
