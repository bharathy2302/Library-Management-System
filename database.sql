-- 1. Create the Database
CREATE DATABASE IF NOT EXISTS library;
USE library;

-- 2. Create 'users' table (for Librarians)
CREATE TABLE IF NOT EXISTS users (
    email VARCHAR(255) PRIMARY KEY,
    password VARCHAR(255) NOT NULL
);

-- 3. Create 'books' table
CREATE TABLE IF NOT EXISTS books (
    id INT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255),
    quantity INT,
    available TINYINT(1)
);

-- 4. Create 'students' table
CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- 5. Create 'issued_books' table
CREATE TABLE IF NOT EXISTS issued_books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT,
    student_id INT,
    issue_date DATE,
    return_date DATE,
    FOREIGN KEY (book_id) REFERENCES books(id),
    FOREIGN KEY (student_id) REFERENCES students(id)
);

-- 6. Insert Initial Data from your screenshots
-- Insert Books
INSERT INTO books (id, title, author, quantity, available) VALUES
(1, 'The Great Gatsby', 'F. Scott Fitzgerald', 5, 1),
(2, '1984', 'George Orwell', 3, 1),
(3, 'Java Programming', 'James Gosling', 10, 1);

-- Insert Students
INSERT INTO students (id, name) VALUES
(101, 'Alice Johnson'),
(102, 'Bob Smith'),
(103, 'Charlie Davis');

-- Note: 'users' and 'issued_books' start empty 
-- unless you have already registered through the app.
