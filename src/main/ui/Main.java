package ui;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        System.out.println("\nSelect from:");
        System.out.println("\t0 -> Signup");
        System.out.println("\t1 -> Login");
        int choice =  sc.nextInt();
        new EncryptionApp(choice);
    }
}
