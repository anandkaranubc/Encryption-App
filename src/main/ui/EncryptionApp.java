package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;

import static model.Decryption.passDecryption;

public class EncryptionApp extends JFrame {

    /**

     The EncryptionApp class implements a console-based user interface for a password management system.
     It provides functionality for user signup and login, encryption and decryption of passwords,
     and listing all encrypted passwords. It interacts with the model classes Encryption, DecryptionList,
     and EncryptionList to perform encryption and decryption operations and store encrypted passwords.
     This class is responsible for displaying the various menu options and processing user input to call the
     appropriate methods. It is also responsible for handling any exceptions that may be thrown during the
     execution of these methods.

     */

    private JPanel contentPane;
    private JButton loginButton;
    private JButton signupButton;
    private JButton loadProgressButton;
    private JButton EncryptionButton;
    private JButton DecryptionButton;
    private JButton EncryptionListButton;
    private JButton DecryptionListButton;

    private static final String JSON_STORE = "./data/user_data.json";
    private Progress progress;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    Encryption encrypt = Encryption.getInstance();
    byte[] cipherText = new byte[0];
    private static String username;
    private static String password;
    static int count = 0;

    Scanner sc = new Scanner(System.in);

    public EncryptionApp() throws Exception {
        setTitle("Login or Signup");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(3, 1));

        loginButtonDisplay();
        signUpButtonDisplay();
        loadProgressButtonDisplay();

        setContentPane(contentPane);
        setVisible(true);
    }

    private void loadProgressButtonDisplay() {
        loadProgressButton = new JButton("Load Progress");
        loadProgressButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open load progress JFrame
                dispose();
                loadProgress();
                loginJFrame();
            }
        });
        contentPane.add(loadProgressButton);
    }

    private void signUpButtonDisplay() {
        signupButton = new JButton("Signup");
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                signUpJFrame();
            }
        });
        contentPane.add(signupButton);
    }

    private void signUpJFrame() {
        JFrame signupFrame = new JFrame("Signup");
        signupFrame.setSize(300, 150);
        signupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new GridLayout(3, 1));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton signupButton = new JButton("Signup");
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // add user to database
                signupFrame.dispose();
                username = usernameField.getText();
                password = passwordField.getText();
                loginJFrame();
            }
        });
        signupPanel.add(new JLabel("Username:"));
        signupPanel.add(usernameField);
        signupPanel.add(new JLabel("Password:"));
        signupPanel.add(passwordField);
        signupPanel.add(signupButton);
        signupFrame.setContentPane(signupPanel);
        signupFrame.setVisible(true);
    }

    private void loginButtonDisplay() {
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open login JFrame
                dispose();
                loginJFrame();
            }
        });
        contentPane.add(loginButton);
    }

    private void loginJFrame() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 150);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 1));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // authenticate user with database
                loginFrame.dispose();
                if(checkUserCredentials(usernameField.getText(), passwordField.getText())) {
                    MainMenuJFrame();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Incorrect credentials",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginFrame.setContentPane(loginPanel);
        loginFrame.setVisible(true);
    }

    private void MainMenuJFrame() {
        setTitle("Main Menu");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(5, 1));

        EncryptionButtonDisplay();
        DecryptionButtonDisplay();
        EncryptionListButtonDisplay();
        DecryptionListButtonDisplay();
        SaveAndExitButtonDisplay();
        ExitWithoutSavingButtonDisplay();

        setContentPane(contentPane);
        setVisible(true);
    }

    private boolean checkUserCredentials(String enteredUsername, String enteredPassword) {
        return (username.equals(enteredUsername) && password.equals(enteredPassword));
    }

    //    Effects: Creates an instance of EncryptionApp based on the choice of the user
    public EncryptionApp(int n) throws Exception {

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

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
        } else if (n == 3) {
            loadProgress();
            runLogin(username, password);
        } else {
            System.out.println("Invalid Command! Please try again!");
            displayFirstMenu();
        }
    }

    public static void setVariables(String username, String password) {
        EncryptionApp.username = username;
        EncryptionApp.password = password;
    }

    //    Requires: The method requires a valid input scanner object "sc" to take input from the user.
//
//    Modifies: The method modifies the instance variables "username", "password" and "count" of the class.
//
//    Effects: The method displays a message to welcome the user to the sign-up page and prompts the user to enter a
//    username and password. It then stores the username and password in the instance variables of the class. The method
//    also increments the count variable to keep track of the number of sign-up attempts.
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
            System.out.println("Redirecting to End Page.....\n\n");
            displayFinalMenu();
        }
    }

//    Requires: The method requires a valid input scanner object "sc" to take input from the user. It also requires the
//    input parameters "username" and "password" to check if the entered credentials match the stored values.
//
//    Modifies: The method does not modify any instance variables of the class.
//
//    Effects: The method displays a message to welcome the user to the login page and prompts the user to enter
//    their username. If the entered username matches the stored username, the method prompts the user to enter their
//    password. If the entered password matches the stored password, the method displays a message to congratulate the
//    user for logging in and redirects the user to the second menu. If the entered password does not match the stored
//    password, the method displays a message to prompt the user to enter the password again and calls the runLogin
//    method recursively. If the entered username does not match the stored username, the method displays a message to
//    prompt the user to enter the username again and calls the displayFirstMenu method.
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

    // Effects: displays the signup and login page
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

    // Effects: displays the menu page which shows different options for
    // encryption, decryption, encryption list and decryption list
    private void displaySecondMenu() throws Exception {
        System.out.println("\nSelect from:");
        System.out.println("\t1 -> Encryption");
        System.out.println("\t2 -> Decryption");
        System.out.println("\t3 -> Encryption List");
        System.out.println("\t4 -> Decryption List");

        int choice = sc.nextInt();

        if (choice == 1) {
            getEncryptionPage();
        } else if (choice == 2) {
            getDecryptionPage();
        } else if (choice == 3) {
            getEncryptionListPage();
        } else if (choice == 4) {
            getDecryptionListPage();
        } else {
            System.out.println("Invalid choice. Please try again.");
            displaySecondMenu();
        }
    }

    // Effects: redirects to the encryption page where the user can encrypt their passwords
    private void getEncryptionPage() throws Exception {
        System.out.println("Redirecting to Encryption Page....\n\n");
        System.out.println("Enter the Data name that you want to encrypt");
        String dataName = sc.next();
        System.out.println("Enter the password you want to encrypt: ");
        String pass = sc.next();
        this.encrypt = Encryption.getInstance();
        this.cipherText = encrypt.passEncryption(pass, dataName);

        continueOrNot();
    }

    // Effects: redirects to the decryption page where the user can decrypt their passwords
    private void getDecryptionPage() throws Exception {
        System.out.println("Redirecting to Decryption Page....\n\n");
        boolean valid = checkUser(username, password);

        if (valid) {
            System.out.print("What data name's password do you want to decrypt?: ");
            String dataName = sc.next();
            DecryptionList decryptionList = new DecryptionList();
            int index = decryptionList.getDataNameIndex(dataName);
            if (index == -1) {
                System.out.println("Sorry, data name not found.");
            } else {
                String answer = decryptionList.getDecryptedStringAtIndex(index);
                System.out.println("Your password for " + dataName + " is: " + answer);
            }

        } else {
            System.out.println("Wrong credentials!!!");
        }
        continueOrNot();
    }

    // Effects: redirects to the encryption list page where the user can see the encrypted list of their passwords
    private void getEncryptionListPage() throws Exception {
        System.out.println("Redirecting to Encryption List Page....\n\n");
        List<String> encryptedStringsList = EncryptionList.encryptedStringList();

        if (encryptedStringsList.isEmpty()) {
            System.out.println("The list is empty");
        } else {
            for (int i = 0; i < encryptedStringsList.size(); i++) {
                System.out.println(EncryptionList.getDataNames().get(i) + ": " + encryptedStringsList.get(i));
            }
        }
        continueOrNot();
    }

    // Effects: redirects to the decryption list page where the user can see the decrypted list of their passwords
    private void getDecryptionListPage() throws Exception {
        System.out.println("Redirecting to Decryption List Page....\n\n");
        boolean valid = checkUser(username, password);

        if (valid) {

            List<byte []> encryptedCiphersList = EncryptionList.getEncryptedCiphers();

            if (encryptedCiphersList.isEmpty()) {
                System.out.println("The list is empty");
            } else {
                for (int i = 0; i < encryptedCiphersList.size(); i++) {
                    byte[] cipher = EncryptionList.getEncryptedCiphers().get(i);
                    String password = passDecryption(cipher, model.Encryption.getPair());
                    System.out.println(EncryptionList.getDataNames().get(i) + ": " + password);
                }
            }
        } else {
            System.out.println("Wrong credentials!!");
        }
        continueOrNot();
    }

    // Effects: ask the user if they want to continue to the features page or not
    private void continueOrNot() throws Exception {
        System.out.println("Do you want to continue? Y/N: ");
        String choice1 = sc.next();

        if (choice1.equals("Y") || choice1.equals("y")) {
            System.out.println("Redirecting to Features Page.....\n\n");
            displaySecondMenu();
        } else {
            System.out.println("Redirecting to End Page.....\n\n");
            displayFinalMenu();
        }
    }

    private void displayFinalMenu() {
        saveOrNot();
    }

    private void saveOrNot() {
        System.out.print("Do you want to save your progress? Y/N: ");
        String choice = sc.next();

        if (choice.equals("Y") || choice.equals("y")) {
            saveProgress();
            System.exit(0);
        } else {
            System.out.println("File not saved.....");
        }
    }

    private void saveProgress() {
        try {
            progress = new Progress();
            jsonWriter.open();
            jsonWriter.write(progress);
            jsonWriter.close();
            System.out.println("Saved " + username + "'s progress" + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads progress from file
    private void loadProgress() {
        try {
            progress = jsonReader.read();
//          System.out.println("Loaded " + username + "'s progress from " + JSON_STORE);
            JOptionPane.showMessageDialog(this, "Progress loaded", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
//            System.out.println("Unable to read from file: " + JSON_STORE);
            JOptionPane.showMessageDialog(this, "Unable to read from file: " + JSON_STORE,
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
//            throw new RuntimeException(e);
            JOptionPane.showMessageDialog(this, "Program Failure", "Error",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Requires: username and password are non-null Strings
    // Effects: checks if username and passwords are correct
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

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

}
