# 📚 Library Management System (LMS)

A desktop-based management tool developed in **Java** using the **Swing** framework and **MySQL**. This application provides librarians with a streamlined interface to manage book inventory, register users, and handle book issuances to students.

---

## ✨ Features

* **Librarian Authentication:** Secure registration and login system for library staff.
* **Inventory Management:** View a real-time list of books, including titles, authors, and current stock levels.
* **Book Issuance:** Logic-driven issuance process that:
    * Verifies Student ID existence.
    * Checks if the book is currently in stock.
    * Automatically decrements the book quantity upon successful issue.
* **Transaction Tracking:** A "Issued Books" dashboard that joins data from multiple tables to show exactly who borrowed what and when.

---

## 🛠️ Tech Stack

* **Language:** Java (JDK 8 or higher)
* **GUI:** Java Swing & AWT
* **Database:** MySQL 8.0
* **Connector:** MySQL Connector/J (JDBC)

---

## 📂 Database Structure

The project uses a relational database named `library`. Below is the schema based on the project requirements:

| Table | Columns | Description |
| :--- | :--- | :--- |
| **`users`** | `email` (PK), `password` | Librarian login credentials. |
| **`books`** | `id` (PK), `title`, `author`, `quantity`, `available` | Book inventory and stock status. |
| **`students`** | `id` (PK), `name` | Registered students information. |
| **`issued_books`** | `id` (PK), `book_id`, `student_id`, `issue_date`, `return_date` | Records of all book transactions. |



---

## 🚀 Getting Started

### 1. Database Configuration
1.  Open your MySQL terminal or MySQL Workbench.
2.  Run the script provided in `database.sql` to create the database, tables, and seed data.
3.  **Note:** Ensure you have the `mysql-connector-j` JAR file added to your project's Build Path.

### 2. Update Connection Details
In `LibraryManagementSystem.java`, update the following line with your local MySQL credentials:

```java
conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/library", "YOUR_USERNAME", "YOUR_PASSWORD"
);
