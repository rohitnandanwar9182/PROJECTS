package REALPROJECTS;
import java.util.*;

public class unitcnverter {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the number");
        int number_in_celsius = sc.nextInt();
        System.out.println("Enter which unit to convert it the given number : f or k or r ");
         String tellunit=sc.next();
        if(tellunit.equals("f")){
            double fahrenheit=(number_in_celsius*9/5)+32;
             System.out.println(fahrenheit + " °F");
        }
       if(tellunit.equals("k")){
            double kelvin=(number_in_celsius+273.15);
            System.out.println(kelvin+ " K");
        }
         if(tellunit.equals("r")){
           double rankine=((number_in_celsius+273.15)*9/5);
            System.out.println(rankine+ " °R");
        }
    }
}         