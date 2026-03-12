# Library Management System (Java Swing + MySQL)

A desktop-based Library Management System built using **Java Swing** for the GUI and **MySQL** for data persistence. This application allows librarians to register, log in, view available books, and issue books to students.

## 🚀 Features
* **Librarian Authentication:** Secure (local) registration and login system.
* **Book Inventory:** Real-time view of available books, authors, and quantities.
* **Transaction Management:** Issue books to students with automatic stock updates.
* **Issuance Tracking:** View a comprehensive history of issued books including student details and dates.
* **Relational Database:** Uses SQL joins to connect books, students, and issuance records.

## 🛠️ Tech Stack
* **Language:** Java (JDK 8 or higher)
* **GUI Framework:** Java Swing
* **Database:** MySQL
* **Driver:** JDBC (MySQL Connector/J)

## 📋 Database Schema
The system requires a MySQL database named `library` with the following tables:
1. `users` - Librarian credentials.
2. `books` - Inventory of books.
3. `students` - Registered student data.
4. `issued_books` - Records of transactions.

## ⚙️ Setup Instructions
1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/yourusername/library-management-system.git](https://github.com/yourusername/library-management-system.git)
    ```
2.  **Database Setup:**
    * Import the `database.sql` file into your MySQL server.
    * Update the `DriverManager.getConnection` string in `LibraryManagementSystem.java` with your MySQL username and password.
3.  **Add JDBC Driver:**
    * Ensure the `mysql-connector-j-x.x.x.jar` is added to your project's build path.
4.  **Run the application:**
    * Compile and run `LibraryManagementSystem.java`.

## 📸 Screenshots
*(Tip: Add screenshots of your Login and Main windows here!)*

