import gov.nasa.jpf.symbc.Debug;

class FindInArray {
    private /*@ spec_public @*/ int key;
    private /*@ spec_public @*/ int arr[];

    //@ ensures (\forall int i; 0 <= i && i < inputArr.length; inputArr[i] == arr[i]);
    //@ ensures key == 0;
    FindInArray(int inputArr[])
    {
        int size = inputArr.length;
        arr = new int[size];
        arr = inputArr.clone();
    } 

    //@ ensures this.key == key;
    //@ ensures (\forall int i; 0 <= i && i < inputArr.length; inputArr[i] == arr[i]);
    FindInArray(int inputArr[], int key)
    {
        int size = inputArr.length;
        arr = new int[size];
        arr = inputArr.clone();
        setKey(key);
    } 

    //@ assignable this.key;
    //@ ensures this.key == key;
    void setKey(int key) 
    {
        this.key = key;
    }

    //@ ensures \result == this.key;
    /*@ pure @*/ int getKey() 
    {
        return this.key;
    }
    //@ requires 0 <= i && i < arr.length;
    //@ ensures \result == this.arr[i]; 
    /*@ pure @*/ int getArr(int i) 
    {
        return this.arr[i];
    }

    //@ ensures \result == arr.length;  
    /*@ pure @*/ int size() 
    {
        return arr.length;
    }

    /*@ ensures 0 <= \result && \result < arr.length ==> (arr[\result] == key && 
      @         (\forall int i; \result < i && i < arr.length; arr[i] != key)); 
      @ ensures \result == -1 ==> (\forall int i; 0 <= i && i < arr.length; arr[i] != key); @*/
    /*@ pure @*/ int findLast() {
    int index = size() - 1;
    //@ maintaining -1 <= index && index < arr.length; 
    //@ maintaining (\forall int i; index < i && i < arr.length; arr[i] != key);
    while (0 <= index) {
        if (getArr(index) == getKey())
            return index;
        index--;
    }
    return -1;
    }

    /*@ ensures 0 <= \result && \result < arr.length ==> (arr[\result] == key && 
      @         (\forall int i; 0 <= i && i < \result; arr[i] != key)); 
      @ ensures \result == -1 ==> (\forall int i; 0 <= i && i < arr.length; arr[i] != key); @*/
    /*@ pure @*/ int findFirst() {  
    //@ maintaining 0 <= index && index <= arr.length;
    //@ maintaining (\forall int i; 0 <= i && i < index; arr[i] != key);
    for (int index = 0; index < size(); index++) {
        if (getArr(index) == getKey())
            return index;
    }
    return -1;
    }
    
    //@ ensures \result <==> findLast() != findFirst();
    /*@ pure @*/ boolean isMoreThanOneKey() {
        int first = findFirst();
        int last = findLast();
        return (first != last);
    }
    
    public static void main(String[] args) {
        int key = 90; 
        int sel = 1;
        int[] arr = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        sel = Debug.addSymbolicInt(sel, "sym_sel");
        key = Debug.addSymbolicInt(key, "sym_key");
        for (int i = 0; i < arr.length; i++) {
            arr[i] = Debug.addSymbolicInt(arr[i], "sym_"+i);   
        }
        if (sel == 1) {
            FindInArray obj = new FindInArray(arr);
            obj.isMoreThanOneKey();
        } else {
            FindInArray obj = new FindInArray(arr, key);
            obj.isMoreThanOneKey();
        }
        Debug.printPC("PC: ");
    }
}