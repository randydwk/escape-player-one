package escapegame;

import java.util.Scanner;

public class Keyboard{
    private final static Scanner sc = new Scanner(System.in);
    private final static String INVALID_MSG = "Invalid input"; 
    
    // readString
    public static String readString(String str){
        System.out.print(str);
        return sc.nextLine();
    }

    public static String readString(){
        return readString("");
    }
}