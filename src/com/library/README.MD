# Library Management System

A client-server library management application built with Java. 
The system uses a multi-threaded server, a MySQL database backend, 
and a Swing-based GUI for the client.

## Features
- Add, view, update, and delete book records
- Client-server architecture over TCP sockets
- Multi-threaded server — handles multiple clients simultaneously
- Persistent storage using MySQL
- Graphical user interface built with Java Swing

## Tech Stack
- **Language:** Java
- **GUI:** Java Swing
- **Database:** MySQL
- **Communication:** Java Sockets + Object Serialization
- **Architecture:** Multi-threaded Client-Server (MVC-inspired)

## Project Structure
com.library.client    → GUI (LibraryGUI.java)
com.library.cilent    → Client networking (LibraryCilent.java)
com.library.server    → Server + ClientHandler
com.library.common    → Shared models (Book, Command)
com.library.db        → Database connection (DbLibrary.java)

## How to Run

### Prerequisites
- Java 11+
- MySQL running on port 3307
- A database named `library-db` with a `books` table:

```sql
CREATE TABLE books (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    title  VARCHAR(255),
    author VARCHAR(255),
    year   INT
);
```

### Steps
1. Start MySQL and ensure `library-db` exists
2. Run `LibraryServer.java` first (listens on port 5000)
3. Run `LibraryGUI.java` to launch the client
4. Use the GUI to add, view, update, or delete books

## Author
Kay-Ann Green — [github.com/Kay-Ann14](https://github.com/Kay-Ann14)
