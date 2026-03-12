import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class LibraryManagementSystem {

    private Connection conn;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new LibraryManagementSystem().showLoginWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public LibraryManagementSystem() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library", "root", "********"
        );
    }

    // LOGIN WINDOW WITH LOGIN & REGISTER
    private void showLoginWindow() {
        JFrame loginFrame = new JFrame("Librarian Login");
        loginFrame.setSize(350, 250);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField(20);
        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(20);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        gbc.gridx = 0; gbc.gridy = 0;
        loginFrame.add(emailLabel, gbc);
        gbc.gridx = 1;
        loginFrame.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        loginFrame.add(passLabel, gbc);
        gbc.gridx = 1;
        loginFrame.add(passField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        loginFrame.add(loginBtn, gbc);
        gbc.gridx = 1;
        loginFrame.add(registerBtn, gbc);

        loginFrame.setVisible(true);

        loginBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());
            if(email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Please fill email and password.");
                return;
            }
            if(authenticateLibrarian(email, password)) {
                JOptionPane.showMessageDialog(loginFrame, "Login Successful!");
                loginFrame.dispose();
                showMainWindow();
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Invalid email or password.");
            }
        });

        registerBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passField.getPassword());
            if(email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(loginFrame, "Please fill email and password to register.");
                return;
            }
            if(registerLibrarian(email, password)) {
                JOptionPane.showMessageDialog(loginFrame, "Registration successful! You can login now.");
            } else {
                JOptionPane.showMessageDialog(loginFrame, "Registration failed. Email may already be registered.");
            }
        });
    }

    private boolean authenticateLibrarian(String email, String password) {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT password FROM users WHERE email=?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                String storedPass = rs.getString(1);
                // For simplicity, plain text comparison; ideally hash passwords
                return storedPass.equals(password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean registerLibrarian(String email, String password) {
        try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (email, password) VALUES (?, ?)")) {
            stmt.setString(1, email);
            stmt.setString(2, password); // Plain text for demo only
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    // MAIN WINDOW AFTER LOGIN
    private void showMainWindow() {
        JFrame mainFrame = new JFrame("Library Management System - Issue Books");
        mainFrame.setSize(800,600);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // Table for books
        DefaultTableModel booksModel = new DefaultTableModel();
        JTable booksTable = new JTable(booksModel);
        booksModel.addColumn("Book ID");
        booksModel.addColumn("Title");
        booksModel.addColumn("Author");
        booksModel.addColumn("Quantity");
        booksModel.addColumn("Available");

        JScrollPane booksScroll = new JScrollPane(booksTable);
        mainFrame.add(booksScroll, BorderLayout.CENTER);

        // Bottom panel with issue book controls
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        JLabel studentIdLabel = new JLabel("Enter Student ID:");
        JTextField studentIdField = new JTextField(10);
        JButton issueBtn = new JButton("Issue Book");
        JButton refreshBtn = new JButton("Refresh Books");
        JButton showIssuedBtn = new JButton("Show Issued Books");

        bottomPanel.add(studentIdLabel);
        bottomPanel.add(studentIdField);
        bottomPanel.add(issueBtn);
        bottomPanel.add(refreshBtn);
        bottomPanel.add(showIssuedBtn);

        mainFrame.add(bottomPanel, BorderLayout.SOUTH);

        // Load books into table
        refreshBooksTable(booksModel);

        // Issuing Book Action
        issueBtn.addActionListener(e -> {
            int selectedRow = booksTable.getSelectedRow();
            if(selectedRow == -1) {
                JOptionPane.showMessageDialog(mainFrame, "Select a book to issue.");
                return;
            }
            String studentIdText = studentIdField.getText().trim();
            if(studentIdText.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "Enter Student ID.");
                return;
            }
            try {
                int studentId = Integer.parseInt(studentIdText);
                int bookId = Integer.parseInt(booksModel.getValueAt(selectedRow, 0).toString());
                if(issueBook(bookId, studentId)) {
                    JOptionPane.showMessageDialog(mainFrame, "Book issued successfully.");
                    refreshBooksTable(booksModel);
                } else {
                    JOptionPane.showMessageDialog(mainFrame, "Failed to issue book.");
                }
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(mainFrame, "Invalid Student ID.");
            }
        });

        refreshBtn.addActionListener(e -> refreshBooksTable(booksModel));

        showIssuedBtn.addActionListener(e -> showIssuedBooksWindow());

        mainFrame.setVisible(true);
    }

    private void refreshBooksTable(DefaultTableModel model) {
        model.setRowCount(0);
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, title, author, quantity, available FROM books ORDER BY id");
            while(rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("title"));
                row.add(rs.getString("author"));
                row.add(rs.getInt("quantity"));
                row.add(rs.getBoolean("available"));
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading books: "+e.getMessage());
        }
    }

    private boolean issueBook(int bookId, int studentId) {
        try {
            // Check if student exists
            try (PreparedStatement stmt = conn.prepareStatement("SELECT id FROM students WHERE id=?")) {
                stmt.setInt(1, studentId);
                ResultSet rs = stmt.executeQuery();
                if(!rs.next()) {
                    JOptionPane.showMessageDialog(null, "Student ID does not exist.");
                    return false;
                }
            }

            // Check quantity
            int qty = 0;
            try (PreparedStatement stmt = conn.prepareStatement("SELECT quantity FROM books WHERE id=?")) {
                stmt.setInt(1, bookId);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    qty = rs.getInt("quantity");
                    if(qty <= 0) {
                        JOptionPane.showMessageDialog(null, "Book not available.");
                        return false;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Book ID does not exist.");
                    return false;
                }
            }

            // Insert into issued_books
            try(PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO issued_books (book_id, student_id, issue_date, return_date) VALUES (?, ?, CURDATE(), NULL)")) {
                insertStmt.setInt(1, bookId);
                insertStmt.setInt(2, studentId);
                insertStmt.executeUpdate();
            }

            // Update quantity and availability
            try (PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE books SET quantity = quantity - 1, available = (quantity - 1 > 0) WHERE id=?")) {
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();
            }
            return true;

        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error issuing book: "+ex.getMessage());
            return false;
        }
    }

    // Window showing all issued books
    private void showIssuedBooksWindow() {
        JFrame issuedFrame = new JFrame("Issued Books");
        issuedFrame.setSize(700, 400);
        issuedFrame.setLocationRelativeTo(null);

        DefaultTableModel issuedModel = new DefaultTableModel();
        JTable issuedTable = new JTable(issuedModel);

        issuedModel.addColumn("Issued ID");
        issuedModel.addColumn("Book ID");
        issuedModel.addColumn("Book Title");
        issuedModel.addColumn("Student ID");
        issuedModel.addColumn("Student Name");
        issuedModel.addColumn("Issue Date");
        issuedModel.addColumn("Return Date");

        JScrollPane scrollPane = new JScrollPane(issuedTable);
        issuedFrame.add(scrollPane);

        try (Statement stmt = conn.createStatement()) {
            String query = "SELECT ib.id, b.id as book_id, b.title, s.id as student_id, s.name, ib.issue_date, ib.return_date " +
                    "FROM issued_books ib " +
                    "JOIN books b ON ib.book_id = b.id " +
                    "JOIN students s ON ib.student_id = s.id " +
                    "ORDER BY ib.issue_date DESC";
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getInt("book_id"));
                row.add(rs.getString("title"));
                row.add(rs.getInt("student_id"));
                row.add(rs.getString("name"));
                row.add(rs.getDate("issue_date"));
                row.add(rs.getDate("return_date"));
                issuedModel.addRow(row);
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading issued books: "+ex.getMessage());
        }

        issuedFrame.setVisible(true);
    }
}
