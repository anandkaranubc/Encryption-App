package ui;

import java.util.Scanner;

public class Main {

    /**
     This class represents the entry point of the EncryptionApp program. It prompts the user to select either signup,
     login, or load progress options and based on the user's choice, it creates an object of the EncryptionApp class to
     perform the corresponding operation. The program utilizes the Scanner class to accept user input from the console.

     */

    public static void main(String[] args) throws Exception {

//        Scanner sc = new Scanner(System.in);
//
//        System.out.println("\nSelect from:");
//        System.out.println("\t0 -> Signup");
//        System.out.println("\t1 -> Login");
//        System.out.println("\t3 -> Load Progress");
//        int choice =  sc.nextInt();

        EncryptionApp gui = new EncryptionApp();

    }
}
