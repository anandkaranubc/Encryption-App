package ui;

import model.*;
import model.Event;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
    private JButton exitButton;

    private static final String JSON_STORE = "./data/user_data.json";
    private Progress progress;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;


    Encryption encrypt = Encryption.getInstance();
    byte[] cipherText = new byte[0];
    private static String username;
    private static String password;
    static int count = 0;

    Scanner sc = new Scanner(System.in);


    /**
     * Constructs a new instance of the EncryptionApp class.
     * REQUIRES: the JSON_STORE file is accessible and writable.
     * EFFECTS: initializes a new EncryptionApp object with a title, size, location, and content pane.
     * MODIFIES: this, jsonWriter, jsonReader, contentPane, loginButton, signUpButton, loadProgressButton
     */
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

    /**
     * REQUIRES: Nothing.
     * MODIFIES: Nothing.
     * EFFECTS: Initializes and adds a "Load Progress" button to the content pane. When clicked,
     *          it opens a load progress JFrame, disposes the current frame, and opens the login JFrame.
     */
    private void loadProgressButtonDisplay() {
        JButton loadProgressButton = new JButton("Load Progress");
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

//    Effects: Creates a new JButton and sets an ActionListener that opens the signup JFrame when clicked.
//    Adds the button to the contentPane of the current JFrame.
    private void signUpButtonDisplay() {
        JButton signupButton = new JButton("Signup");
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                signUpJFrame();
            }
        });
        contentPane.add(signupButton);
    }

    /**
     * EFFECTS: Displays the Signup JFrame to allow the user to create a new account
     * it also displays the password strength meter
     */
    private void signUpJFrame() {
        JFrame signupFrame = new JFrame("Signup");
        signupFrame.setSize(300, 200);
        signupFrame.setLocationRelativeTo(null);
        signupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel signupPanel = new JPanel();
        signupPanel.setLayout(new GridLayout(4, 1));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JButton signupButton = new JButton("Signup");
        signUpButtonPressed(signupFrame, usernameField, passwordField, signupButton);

        exitButton = new JButton("Exit");
        exitButtonFirstPage(signupFrame);

        usernameAndPasswordDisplay(signupPanel, "Username:", usernameField, passwordField);

        // Add a progress bar to show password strength
        JProgressBar strengthBar = new JProgressBar(0, 100);
        signupPanel.add(new JLabel("Password Strength:"));
        signupPanel.add(strengthBar);

        strengthBarDisplay(passwordField, strengthBar);

        signupPanel.add(signupButton);
        signupPanel.add(exitButton);
        signupFrame.setContentPane(signupPanel);
        signupFrame.setVisible(true);
    }

    private void usernameAndPasswordDisplay(JPanel enteredPanel, String text, JTextField usernameField,
                                            JPasswordField passwordField) {
        enteredPanel.add(new JLabel(text));
        enteredPanel.add(usernameField);
        enteredPanel.add(new JLabel("Password:"));
        enteredPanel.add(passwordField);
    }

    // EFFECTS: Add a DocumentListener to detect changes to the password field
    private void strengthBarDisplay(JPasswordField passwordField, JProgressBar strengthBar) {
        passwordField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateStrengthBar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateStrengthBar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateStrengthBar();
            }

            private void updateStrengthBar() {
                // Calculate the strength of the password based on its length, complexity, etc.
                String password = new String(passwordField.getPassword());
                int strength = calculatePasswordStrength(password);

                // Update the value of the progress bar
                strengthBar.setValue(strength);
            }
        });
    }

    private int calculatePasswordStrength(String password) {
        int strength = 0;

        // Check password length
        if (password.length() < 8) {
            strength += 10;
        } else if (password.length() >= 8 && password.length() < 12) {
            strength += 30;
        } else {
            strength += 50;
        }

        // Check for uppercase letters
        if (password.matches(".*[A-Z].*")) {
            strength += 10;
        }

        // Check for lowercase letters
        if (password.matches(".*[a-z].*")) {
            strength += 10;
        }

        // Check for digits
        if (password.matches(".*[0-9].*")) {
            strength += 10;
        }

        // Check for special characters
        if (password.matches(".*[!@#$%^&*()].*")) {
            strength += 20;
        }

        return strength;
    }



    //    Effects: This method sets an ActionListener to the sign-up button, which when clicked,
//    adds the user's inputted username and password to the database and
//    opens a JOptionPane message dialog to indicate that the sign-up was successful.
//    The method also sets the username and password variables using the setVariables method from the EncryptionApp
//    class and opens the login JFrame.
//
//    Modifies: username, password
    private void signUpButtonPressed(JFrame signupFrame, JTextField usernameField, JPasswordField passwordField,
                                     JButton signupButton) {
        signupButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

    /**
     EFFECTS: Displays the Login button in the main menu JFrame
     */
    private void loginButtonDisplay() {
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open login JFrame
                dispose();
                loginJFrame();
            }
        });
        contentPane.add(loginButton);
    }

//    EFFECTS: This method creates a login JFrame with a username field, a password field, a login button,
//    and an exit button. The method sets the size of the JFrame to 300 by 150 pixels, centers the JFrame on the screen,
//    and sets the default close operation to DISPOSE_ON_CLOSE. The method also adds action listeners to the
//    login button and exit button. When the login button is pressed, the loginButtonPressed() method is called,
//    passing in the login JFrame, the username field, the password field, and the login button. When the exit
//    button is pressed,
//    the exitButtonFirstPage() method is called, passing in the login JFrame.
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

        usernameAndPasswordDisplay(loginPanel, "Username:", usernameField, passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(exitButton);
        loginFrame.setContentPane(loginPanel);
        loginFrame.setVisible(true);
    }

//    Disposes the current JFrame and creates a new instance of EncryptionApp
//    when the exit button is pressed.
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

//    sets the loginFrame visibility to false, and opens a new JFrame with the main menu if login is successful,
//    otherwise displays an error message
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

//    This method creates and sets up the main menu JFrame.
//    It sets the title, size, location, and close operation of the JFrame.
//    It creates a JPanel and sets its layout to a 3x1 grid.
//    It then calls several methods to display various buttons on the JPanel.
//    Finally, it sets the content pane of the JFrame to the JPanel and makes it visible.
    private void mainMenuJFrame() {
        setTitle("Main Menu");
        setSize(500, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(3, 1));

        encryptionButtonDisplay();
        decryptionButtonDisplay();
        encryptionListButtonDisplay();
        decryptionListButtonDisplay();
        saveAndExitButtonDisplay();
        exitWithoutSavingButtonDisplay();

        setContentPane(contentPane);
        setVisible(true);
    }


//    This method creates and adds an "Exit Without Saving" button to the main menu JFrame. When clicked,
//    it disposes of the main menu JFrame and opens a confirmation JFrame asking the user if they are sure
//    they want to exit without saving.
    private void exitWithoutSavingButtonDisplay() {
        JButton exitWithoutSavingButton = new JButton("Exit Without Saving");
        exitWithoutSavingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                exitWithoutSavingJFrame();
            }
        });
        contentPane.add(exitWithoutSavingButton);
    }

    public void printLog() {
        EventLog eventLog = EventLog.getInstance();
        for (Event e: eventLog) {
            System.out.println(e);
        }
    }

//    This method displays a message dialog indicating that the program has been exited without saving any changes,
//    and then terminates the program with a successful exit status code.
    private void exitWithoutSavingJFrame() {
        JOptionPane.showMessageDialog(this, "Exited without Saving!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        printLog();
        System.exit(0);
    }

//    This method creates a "Save & Exit" button and adds an ActionListener to it.
//    When the button is clicked, the current JFrame is disposed and the saveAndExitJFrame() method is called.
    private void saveAndExitButtonDisplay() {
        JButton saveAndExitButton = new JButton("Save & Exit");
        saveAndExitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                saveAndExitJFrame();
            }
        });
        contentPane.add(saveAndExitButton);
    }

//    This method saves the progress and displays a message dialog box to notify the user
//    that their progress has been saved, then exits the application.
    private void saveAndExitJFrame() {
        saveProgress();
        JOptionPane.showMessageDialog(this, "Progress Saved!",
                "Success", JOptionPane.INFORMATION_MESSAGE);
        printLog();
        System.exit(0);
    }

//    This method creates and displays a JButton labeled "Decryption List" and adds an ActionListener to it.
//    When the button is clicked, it disposes the current JFrame and opens the decryption list JFrame.
//    If an exception occurs while opening the decryption list JFrame, it throws a RuntimeException
//    with the original exception as the cause.
    private void decryptionListButtonDisplay() {
        JButton decryptionListButton = new JButton("Decryption List");
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

//    This method displays a JFrame that shows a list of decrypted passwords.
//    The passwords are obtained from the EncryptionList class and decrypted using the passDecryption() method
//    with the Encryption class's key pair. The decrypted passwords are added to a DefaultListModel which is used
//    to create a JList. The JList is then added to a JScrollPane and set as the content pane of the listFrame.
//    The windowClosingButton() method is also called to add functionality to the JFrame's close button.
    private void decryptionListJFrame() throws Exception {
        JFrame listFrame = new JFrame("Decryption List");
        listFrame.setSize(400, 300);
        listFrame.setLocationRelativeTo(null);
        listFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultListModel<String> model = new DefaultListModel<>();

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

    /**
     * Displays a button for opening the Encryption List JFrame.
     * Sets an ActionListener for the button that disposes of the current JFrame
     * and opens the Encryption List JFrame.
     * MODIFIES: This method adds a JButton to the contentPane JPanel.
     * EFFECTS: Clicking the button opens the Encryption List JFrame.
     */
    private void encryptionListButtonDisplay() {
        JButton encryptionListButton = new JButton("Encryption List");
        encryptionListButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                encryptionListJFrame();
            }
        });
        contentPane.add(encryptionListButton);
    }

    // EFFECTS: displays a list of encrypted passwords with their respective data names
    private void encryptionListJFrame() {
        JFrame listFrame = new JFrame("Encryption List");
        listFrame.setSize(400, 300);
        listFrame.setLocationRelativeTo(null);
        listFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultListModel<String> model = new DefaultListModel<>();
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

    // EFFECTS: displays the main menu when the user closes the displayed window
    private void windowClosingButton(JFrame listFrame) {
        listFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                mainMenuJFrame();
            }
        });
    }

    // EFFECTS: when button clicked, disposes of teh current frame and displays the
//    decryption frame
    private void decryptionButtonDisplay() {
        JButton decryptionButton = new JButton("Password Decryption");
        decryptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                decryptionJFrame();
            }
        });
        contentPane.add(decryptionButton);
    }

    // EFFECTS: displays a menu which asks the user to add a data name and find its password
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

    // EFFECTS: displays a list of passwords with their respective data names
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

    // EFFECTS: when button pressed, loads the encryption menu
    private void encryptionButtonDisplay() {
        JButton encryptionButton = new JButton("Password Encryption");
        encryptionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // open signup JFrame
                dispose();
                encryptionJFrame();
            }
        });
        contentPane.add(encryptionButton);
    }

    // EFFECTS: displays a menu which allows the user to add a data name and password to encrypt
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

        usernameAndPasswordDisplay(encryptionPanel, "Data Name:", dataNameField, passwordField);
        encryptionPanel.add(encryptButton);
        encryptionPanel.add(exitButton);
        encryptionFrame.setContentPane(encryptionPanel);
        encryptionFrame.setVisible(true);
    }

    // EFFECTS: encrypts the password with the given data name
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

    // EFFECTS: displays the exit button which take the user to the main menu frame when pressed
    private void exitButtonDisplay(JFrame enteredJFrame) {
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enteredJFrame.dispose();
                mainMenuJFrame();
            }
        });
    }

    // EFFECTS: returns true if the entered username and password matches with the original ones
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
