package bookrental.ui;

import bookrental.model.Book;
import bookrental.model.User;
import bookrental.service.BookRentalPlatform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PlatformUI extends JFrame {
    private BookRentalPlatform platform;
    
    private JTextArea displayArea;
    private JTextField nameField, emailField, phoneField, addressField;
    private JTextField bookNameField, authorField, summaryField;
    private JComboBox<User> userComboBox;          // For renting
    private JComboBox<Book> bookComboBox;
    private JComboBox<User> ownerComboBox;           // For adding a book (owner)
    private JLabel coinBalanceLabel;
    
    public PlatformUI(BookRentalPlatform platform) {
        this.platform = platform;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Book Rental Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 550);
        setLayout(new BorderLayout());
        
        JPanel registrationPanel = new JPanel(new GridLayout(5, 2));
        registrationPanel.setBorder(BorderFactory.createTitledBorder("User Registration"));
        
        registrationPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        registrationPanel.add(nameField);
        
        registrationPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        registrationPanel.add(emailField);
        
        registrationPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        registrationPanel.add(phoneField);
        
        registrationPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        registrationPanel.add(addressField);
        
        JButton registerButton = new JButton("Register");
        registrationPanel.add(registerButton);
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        
        // Book Listing Panel (now includes owner selection)
        JPanel bookPanel = new JPanel(new GridLayout(5, 2));
        bookPanel.setBorder(BorderFactory.createTitledBorder("Add Book"));
        
        bookPanel.add(new JLabel("Book Name:"));
        bookNameField = new JTextField();
        bookPanel.add(bookNameField);
        
        bookPanel.add(new JLabel("Author:"));
        authorField = new JTextField();
        bookPanel.add(authorField);
        
        bookPanel.add(new JLabel("Summary:"));
        summaryField = new JTextField();
        bookPanel.add(summaryField);
        
        bookPanel.add(new JLabel("Select Owner:"));
        ownerComboBox = new JComboBox<>();
        bookPanel.add(ownerComboBox);
        
        JButton addBookButton = new JButton("Add Book (+10 Coins to Owner)");
        bookPanel.add(addBookButton);
        addBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBook();
            }
        });
        
        // Renting Panel
        JPanel rentingPanel = new JPanel(new GridLayout(4, 2));
        rentingPanel.setBorder(BorderFactory.createTitledBorder("Rent a Book"));
        
        rentingPanel.add(new JLabel("Select User:"));
        userComboBox = new JComboBox<>();
        rentingPanel.add(userComboBox);
        
        rentingPanel.add(new JLabel("Select Book:"));
        bookComboBox = new JComboBox<>();
        rentingPanel.add(bookComboBox);
        
        JButton rentButton = new JButton("Rent Book (Cost: 10 ReadCoins)");
        rentingPanel.add(rentButton);
        rentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                rentBook();
            }
        });
        
        JButton checkCoinsButton = new JButton("Check Coins");
        rentingPanel.add(checkCoinsButton);
        checkCoinsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkUserCoins();
            }
        });
        
        coinBalanceLabel = new JLabel("Available Coins: N/A");
        rentingPanel.add(coinBalanceLabel);
        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(registrationPanel);
        leftPanel.add(bookPanel);
        leftPanel.add(rentingPanel);
        
        add(leftPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        
        updateUserComboBoxes();
        updateBookComboBox();
    }
    
    private void registerUser() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        
        if(name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please fill all user fields");
            return;
        }
        User user = new User(name, email, phone, address);
        platform.registerUser(user);
        updateUserComboBoxes();
        displayArea.append("Registered user: " + user + "\n");
        
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }
    
    private void addBook() {
        String bookName = bookNameField.getText().trim();
        String author = authorField.getText().trim();
        String summary = summaryField.getText().trim();
        
        if(bookName.isEmpty() || author.isEmpty() || summary.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please fill all book fields");
            return;
        }
        
        User owner = (User) ownerComboBox.getSelectedItem();
        if(owner == null) {
            JOptionPane.showMessageDialog(this, "Please select a book owner");
            return;
        }
        
        Book book = new Book(bookName, author, summary);
        platform.addBook(book);
        owner.addReadCoins(10); // Award 10 coins to the owner for adding a book
        displayArea.append("Added book: " + book + " by " + owner.getName() + " (+10 Coins awarded)\n");
        
        bookNameField.setText("");
        authorField.setText("");
        summaryField.setText("");
        updateBookComboBox();
        checkUserCoins(); // update coin display if affected user is selected for renting
    }
    
    private void rentBook() {
        User selectedUser = (User) userComboBox.getSelectedItem();
        Book selectedBook = (Book) bookComboBox.getSelectedItem();
        if(selectedUser == null || selectedBook == null) {
            JOptionPane.showMessageDialog(this, "Please select both user and book");
            return;
        }
        int rentalCost = 10;
        boolean success = platform.rentBook(selectedUser, selectedBook, rentalCost);
        if(success) {
            displayArea.append(selectedUser.getName() + " rented: " + selectedBook.getName() + "\n");
        } else {
            displayArea.append("Rental failed. Either insufficient coins or book is already rented.\n");
        }
        updateBookComboBox();
        checkUserCoins();
    }
    
    private void checkUserCoins() {
        User selectedUser = (User) userComboBox.getSelectedItem();
        if(selectedUser != null) {
            coinBalanceLabel.setText("Available Coins: " + selectedUser.getReadCoins());
        } else {
            coinBalanceLabel.setText("Available Coins: N/A");
        }
    }
    
    private void updateUserComboBoxes() {
        userComboBox.removeAllItems();
        ownerComboBox.removeAllItems();
        List<User> users = platform.getUsers();
        for(User u : users) {
            userComboBox.addItem(u);
            ownerComboBox.addItem(u);
        }
    }
    
    private void updateBookComboBox() {
        bookComboBox.removeAllItems();
        List<Book> availableBooks = platform.getAvailableBooks();
        for(Book b : availableBooks) {
            bookComboBox.addItem(b);
        }
    }
}
