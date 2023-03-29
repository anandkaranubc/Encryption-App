package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.swing.*;

import static model.Decryption.passDecryption;

public class EncryptionApp extends JFrame {

    /**

     The EncryptionApp class implements a GUI-based user interface for a password management system.
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
    private JButton encryptionButton;
    private JButton decryptionButton;
    private JButton encryptionListButton;
    private JButton decryptionListButton;
    private JButton exitWithoutSavingButton;
    private JButton saveAndExitButton;
    private JButton exitButton;

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

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        setTitle("Login or Signup");
        setSize(300, 150);
        setLocationRelativeTo(null);

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
        signupFrame.setLocationRelativeTo(null);
        signupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new GridLayout(3, 1));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton signupButton = new JButton("Signup");

        signUpButtonPressed(signupFrame, usernameField, passwordField, signupButton);

        exitButton = new JButton("Exit");
        exitButtonFirstPage(signupFrame);

        signupPanel.add(new JLabel("Username:"));
        signupPanel.add(usernameField);
        signupPanel.add(new JLabel("Password:"));
        signupPanel.add(passwordField);
        signupPanel.add(signupButton);
        signupPanel.add(exitButton);
        signupFrame.setContentPane(signupPanel);
        signupFrame.setVisible(true);
    }

    private void signUpButtonPressed(JFrame signupFrame, JTextField usernameField, JPasswordField passwordField,
                                     JButton signupButton) {
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // add user to database
                signupFrame.dispose();
                JOptionPane.showMessageDialog(signupFrame, "Sign Up Successful",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                username = usernameField.getText();
                password = passwordField.getText();
                EncryptionApp.setVariables(username, password);
                loginJFrame();
            }
        });
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
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 1));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButtonPressed(loginFrame, usernameField, passwordField, loginButton);

        exitButton = new JButton("Exit");
        exitButtonFirstPage(loginFrame);

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(exitButton);
        loginFrame.setContentPane(loginPanel);
        loginFrame.setVisible(true);
    }

    private void exitButtonFirstPage(JFrame loginFrame) {
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginFrame.dispose();
                try {
                    new EncryptionApp();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void loginButtonPressed(JFrame loginFrame, JTextField usernameField,
                                    JPasswordField passwordField, JButton loginButton) {
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // authenticate user with database
                loginFrame.dispose();
                if (checkUserCredentials(usernameField.getText(), passwordField.getText())) {
                    JOptionPane.showMessageDialog(loginFrame, "Login Successful",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    mainMenuJFrame();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Incorrect credentials",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

//    private void passwordAuthenticationJFrame() {
//        JFrame loginFrame = new JFrame("Login");
//        loginFrame.setSize(300, 150);
//        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        JPanel loginPanel = new JPanel();
//        loginPanel.setLayout(new GridLayout(3, 1));
//        JTextField usernameField = new JTextField();
//        JPasswordField passwordField = new JPasswordField();
//        JButton loginButton = new JButton("Login");
//        loginButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                // authenticate user with database
//                loginFrame.dispose();
//                if(checkUserCredentials(usernameField.getText(), passwordField.getText())) {
//                    JOptionPane.showMessageDialog(loginFrame, "Login Successful",
//                            "Success", JOptionPane.INFORMATION_MESSAGE);
//                    DecryptionJFrame();
//                } else {
//                    JOptionPane.showMessageDialog(loginFrame, "Incorrect credentials",
//                            "Error", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });
//        loginPanel.add(new JLabel("Username:"));
//        loginPanel.add(usernameField);
//        loginPanel.add(new JLabel("Password:"));
//        loginPanel.add(passwordField);
//        loginPanel.add(loginButton);
//        loginFrame.setContentPane(loginPanel);
//        loginFrame.setVisible(true);
//    }

    private void mainMenuJFrame() {
        setTitle("Main Menu");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(5, 1));

        encryptionButtonDisplay();
        decryptionButtonDisplay();
        encryptionListButtonDisplay();
        decryptionListButtonDisplay();
        saveAndExitButtonDisplay();
        exitWithoutSavingButtonDisplay();

        setContentPane(contentPane);
        setVisible(true);
    }

    private void exitWithoutSavingButtonDisplay() {
        exitWithoutSavingButton = new JButton("Exit Without Saving");
        exitWithoutSavingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                exitWithoutSavingJFrame();
            }
        });
        contentPane.add(exitWithoutSavingButton);
    }

    private void exitWithoutSavingJFrame() {
        JOptionPane.showMessageDialog(this, "Exited without Saving!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void saveAndExitButtonDisplay() {
        saveAndExitButton = new JButton("Save & Exit");
        saveAndExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                saveAndExitJFrame();
            }
        });
        contentPane.add(saveAndExitButton);
    }

    private void saveAndExitJFrame() {
        saveProgress();
        JOptionPane.showMessageDialog(this, "Progress Saved!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    private void decryptionListButtonDisplay() {
        decryptionListButton = new JButton("Decryption List");
        decryptionListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                try {
                    decryptionListJFrame();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        contentPane.add(decryptionListButton);
    }

    private void decryptionListJFrame() throws Exception {
        JFrame listFrame = new JFrame("Decryption List");
        listFrame.setSize(400, 300);
        listFrame.setLocationRelativeTo(null);
        listFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultListModel<String> model = new DefaultListModel<String>();

        for (int i = 0; i < EncryptionList.getDataNames().size(); i++) {
            byte[] cipher = EncryptionList.getEncryptedCiphers().get(i);
            String password = passDecryption(cipher, Encryption.getPair());
            model.addElement(EncryptionList.getDataNames().get(i) + ": "
                    + password);
        }

        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        listFrame.setContentPane(scrollPane);

        listFrame.setVisible(true);
        windowClosingButton(listFrame);
    }

    private void encryptionListButtonDisplay() {
        encryptionListButton = new JButton("Encryption List");
        encryptionListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                encryptionListJFrame();
            }
        });
        contentPane.add(encryptionListButton);
    }

    private void encryptionListJFrame() {
        JFrame listFrame = new JFrame("Encryption List");
        listFrame.setSize(400, 300);
        listFrame.setLocationRelativeTo(null);
        listFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultListModel<String> model = new DefaultListModel<String>();
        for (int i = 0; i < EncryptionList.getDataNames().size(); i++) {
            model.addElement(EncryptionList.getDataNames().get(i) + ": "
                    + EncryptionList.getEncryptedCiphers().get(i));
        }

        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        listFrame.setContentPane(scrollPane);

        listFrame.setVisible(true);
        windowClosingButton(listFrame);
    }

    private void windowClosingButton(JFrame listFrame) {
        listFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                mainMenuJFrame();
            }
        });
    }

    private void decryptionButtonDisplay() {
        decryptionButton = new JButton("Password Decryption");
        decryptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                decryptionJFrame();
            }
        });
        contentPane.add(decryptionButton);
    }

    private void decryptionJFrame() {
        JFrame decryptionFrame = new JFrame("Decryption");
        decryptionFrame.setSize(300, 150);
        decryptionFrame.setLocationRelativeTo(null);
        decryptionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel decryptionPanel = new JPanel();
        decryptionPanel.setLayout(new GridLayout(3, 1));
        JTextField dataNameField = new JTextField();
        JButton decryptButton = new JButton("Decrypt");

        decryptionButtonPressed(decryptionFrame, dataNameField, decryptButton);

        exitButtonDisplay(decryptionFrame);

        decryptionPanel.add(new JLabel("Data Name:"));
        decryptionPanel.add(dataNameField);
        decryptionPanel.add(decryptButton);
        decryptionPanel.add(exitButton);
        decryptionFrame.setContentPane(decryptionPanel);
        decryptionFrame.setVisible(true);
    }

    private void decryptionButtonPressed(JFrame decryptionFrame, JTextField dataNameField, JButton decryptButton) {
        decryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // authenticate user with database
                decryptionFrame.dispose();
                String dataName = dataNameField.getText();
                DecryptionList decryptionList = new DecryptionList();
                int index = decryptionList.getDataNameIndex(dataName);
                if (index == -1) {
                    JOptionPane.showMessageDialog(decryptionFrame, "No data name found!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    mainMenuJFrame();
                } else {
                    try {
                        String answer = decryptionList.getDecryptedStringAtIndex(index);
                        JOptionPane.showMessageDialog(decryptionFrame, "Your password for " + dataName
                                        + " is: " + answer,
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        decryptionJFrame();
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }

    private void encryptionButtonDisplay() {
        encryptionButton = new JButton("Password Encryption");
        encryptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                encryptionJFrame();
            }
        });
        contentPane.add(encryptionButton);
    }

    private void encryptionJFrame() {
        JFrame encryptionFrame = new JFrame("Encryption");
        encryptionFrame.setSize(300, 150);
        encryptionFrame.setLocationRelativeTo(null);
        encryptionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel encryptionPanel = new JPanel();
        encryptionPanel.setLayout(new GridLayout(3, 1));
        JTextField dataNameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton encryptButton = new JButton("Encrypt");

        encryptionButtonPressed(encryptionFrame, dataNameField, passwordField, encryptButton);

        exitButtonDisplay(encryptionFrame);

        encryptionPanel.add(new JLabel("Data Name:"));
        encryptionPanel.add(dataNameField);
        encryptionPanel.add(new JLabel("Password:"));
        encryptionPanel.add(passwordField);
        encryptionPanel.add(encryptButton);
        encryptionPanel.add(exitButton);
        encryptionFrame.setContentPane(encryptionPanel);
        encryptionFrame.setVisible(true);
    }

    private void encryptionButtonPressed(JFrame encryptionFrame, JTextField dataNameField,
                                         JPasswordField passwordField, JButton encryptButton) {
        encryptButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // authenticate user with database
                encryptionFrame.dispose();
                try {
                    encrypt.passEncryption(passwordField.getText(),
                            dataNameField.getText());
                    JOptionPane.showMessageDialog(encryptionFrame, "Encryption Successful",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    encryptionJFrame();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void exitButtonDisplay(JFrame enteredJFrame) {
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enteredJFrame.dispose();
                mainMenuJFrame();
            }
        });
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
            System.exit(0);
        } catch (Exception e) {
//            throw new RuntimeException(e);
            JOptionPane.showMessageDialog(this, "Program Failure", "Error",
                    JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
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
