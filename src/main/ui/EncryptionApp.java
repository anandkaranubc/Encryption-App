package ui;

import model.DecryptionList;
import model.Encryption;
import model.EncryptionList;

import java.util.Scanner;

import static model.DecryptionList.searchDataName;

public class EncryptionApp {
    Encryption encrypt = null;
    byte[] cipherText = new byte[0];
    private static String username;
    private static String password;
    static int count = 0;

    Scanner sc = new Scanner(System.in);

    public EncryptionApp(int n) throws Exception {

        if (n == 1) {
            if (count == 0) {
                System.out.println("Sorry, you are not signed up. Please sign up first to login.");
                count++;
                runSignUp();
            } else {
                runLogin(username, password);
            }
        } else if (n == 0) {
            runSignUp();
            runLogin(username, password);
        } else {
            System.out.println("Invalid Command! Please try again!");
            displayFirstMenu();
        }
    }

    private void runSignUp() throws Exception {
        System.out.println("Welcome to the Signup Page");
        System.out.println("Enter you username: ");
        username = sc.next();
        System.out.println("Enter your password: ");
        password = sc.next();
        System.out.println("\n Congratulations, you have been signed up.");
        count++;
        System.out.println("Do you want to login? Y/N: ");
        String choice = sc.next();

        if (choice.equals("Y") || choice.equals("y")) {
            System.out.println("Redirecting to Login Page.....\n\n");
            runLogin(username, password);
        } else {
            System.out.println("Redirecting to Main Page.....\n\n");
            displayFirstMenu();
        }
    }

    private void runLogin(String username, String password) throws Exception {
        System.out.println("Welcome to the Login Page");
        System.out.println("Enter you username: ");
        String enteredUsername = sc.next();
        if (username.equals(enteredUsername)) {
            System.out.println("Enter your password: ");
            String enteredPassword = sc.next();
            if (password.equals(enteredPassword)) {
                System.out.println("\n Congratulations, you have been logged in.");
                System.out.println("Redirecting to Features Page.....\n\n");
                displaySecondMenu();
            } else {
                System.out.println("Invalid password. Please try again.");
                System.out.println("Redirecting to Login Page again.....\n\n");
                runLogin(username, password);
            }
        } else {
            System.out.println("Username not found. Please try again.");
            System.out.println("Redirecting to Main Page again.....\n\n");
            displayFirstMenu();
        }
    }

    protected void displayFirstMenu() throws Exception {
        System.out.println("\nSelect from:");
        System.out.println("\t0 -> Signup");
        System.out.println("\t1 -> Login");
        int choice =  sc.nextInt();
        if (count == 0) {
            new EncryptionApp(choice);
        } else {
            runLogin(username, password);
        }
    }

    @SuppressWarnings("methodlength")
    private void displaySecondMenu() throws Exception {
        System.out.println("\nSelect from:");
        System.out.println("\t1 -> Encryption");
        System.out.println("\t2 -> Decryption");
        System.out.println("\t3 -> Encryption List");
        System.out.println("\t4 -> Decryption List");

        int choice = sc.nextInt();

        if (choice == 1) {
            System.out.println("Redirecting to Encryption Page....\n\n");
            System.out.println("Enter the Data name that you want to encrypt");
            String dataName = sc.next();
            System.out.println("Enter the password you want to encrypt: ");
            String pass = sc.next();
            this.encrypt = new Encryption();
            this.cipherText = encrypt.passEncryption(pass, dataName);

            continueOrNot();
        } else if (choice == 2) {
            System.out.println("Redirecting to Decryption Page....\n\n");
            boolean valid = checkUser(username, password);

            if (valid) {
                System.out.print("What data name's password do you want to decrypt?: ");
                String dataName = sc.next();
                searchDataName(dataName);
            } else {
                System.out.println("Wrong credentials!!!");
            }
            continueOrNot();
        } else if (choice == 3) {
            System.out.println("Redirecting to Encryption List Page....\n\n");
            EncryptionList.printEncryptedStrings();
            continueOrNot();

        } else if (choice == 4) {
            System.out.println("Redirecting to Decryption List Page....\n\n");
            boolean valid = checkUser(username, password);
            if (valid) {
                DecryptionList.printDecryptedStrings();
            } else {
                System.out.println("Wrong credentials!!");
            }
            continueOrNot();

        } else {
            System.out.println("Invalid choice. Please try again.");
            displaySecondMenu();
        }
    }

    private void continueOrNot() throws Exception {
        System.out.println("Do you want to continue? Y/N: ");
        String choice1 = sc.next();

        if (choice1.equals("Y") || choice1.equals("y")) {
            System.out.println("Redirecting to Features Page.....\n\n");
            displaySecondMenu();
        } else {
            System.out.println("Redirecting to Main Page.....\n\n");
            displayFirstMenu();
        }
    }

    public boolean checkUser(String username, String password) {
        System.out.println("Enter you username: ");
        String enteredUsername = sc.next();
        if (username.equals(enteredUsername)) {
            System.out.println("Enter your password: ");
            String enteredPassword = sc.next();
            return password.equals(enteredPassword);
        }
        return false;
    }
}
