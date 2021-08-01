import gov.nasa.jpf.symbc.Debug;

public class Calculator {
    /*@ requires 37 <= (int)operator && (int)operator <= 47;
      @ {|
      @    requires operator == '+';
      @    requires num1 + num2 <= Integer.MAX_VALUE;
      @    requires Integer.MIN_VALUE <= num1 + num2;
      @    ensures \result == num1 + num2;
      @ also
      @    requires operator == '*'; 
      @    requires num1 * num2 <= Integer.MAX_VALUE;
      @    requires Integer.MIN_VALUE <= num1 * num2;
      @    ensures \result == num1 * num2;
      @ also
      @    requires operator == '-'; 
      @    requires num1 - num2 <= Integer.MAX_VALUE;
      @    requires Integer.MIN_VALUE <= num1 - num2;
      @    ensures \result == num1 - num2;
      @ also
      @    requires operator == '/'; 
      @    requires num2 != 0;
      @    requires num1 / num2 <= Integer.MAX_VALUE;
      @    requires num1 / num2 != Integer.MIN_VALUE;
      @    ensures \result == (num1 / num2);
      @ also
      @    requires operator == '%'; 
      @    requires num2 != 0;
      @    requires num1 % num2 != Integer.MIN_VALUE;
      @    ensures \result == (num1 % num2);
      @ also
      @    requires operator != '+' && operator != '*' && operator != '-' && operator != '/' && operator != '%';
      @    ensures \result == -1;
    |} @*/
    public /*@ pure @*/ int calculate(int num1, int num2, char operator) {

        int output;

        switch (operator)
        {
            case '+':
                output = num1 + num2;
                break;

            case '-':
                output = num1 - num2;
                break;

            case '*':
                output = num1 * num2;
                break;

            case '/':
                output = num1 / num2;
        break;

        case '%':
        output = num1 % num2;
                break;

            default:
                return -1;
        }
        return output;
    }
    
    public static void main(String[] args) {
        int x = 100; 
        int y = -12;
        char c = 'a';
        Calculator cal = new Calculator();
        Debug.addSymbolicInt(x, "sym_x");
        Debug.addSymbolicInt(y, "sym_y");
        Debug.addSymbolicChar(c, "sym_c");
        cal.calculate(x,y,c);
        Debug.printPC("PC: ");
    }
}