package com.library.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.library.common.Book;
import com.library.common.Command;
import com.library.db.DbLibrary;

public class ClientHandler implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(socket.getInputStream())) {

            Object obj;
            while ((obj = is.readObject()) != null) {
                if (obj instanceof Command) {
                    Command cmd = (Command) obj;
                    Object response = handleCommand(cmd);
                    os.writeObject(response);
                    os.flush();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Client Disconnected: " +
                (e.getMessage() != null ? e.getMessage() : e.toString()));
        }
    }

    private Object handleCommand(Command cmd) {
        try {
            // FIX: make command case-insensitive
            String action = cmd.getAction().toUpperCase();

            switch (action) {
                case "ADD":
                    Book b = cmd.getBook();
                    addBook(b.getYear(), b.getTitle(), b.getAuthor());
                    return "OK|Book added";

                case "UPDATE":
                    Book u = cmd.getBook();
                    updateBook(u.getId(), u.getTitle(), u.getAuthor(), u.getYear());
                    return "OK|Book updated";

                case "VIEW":
                    return viewBooks();

                case "DELETE":
                    deleteBook(cmd.getBook().getId());
                    return "OK|Book deleted";

                default:
                    return "ERROR|Unknown command";
            }
        } catch (Exception e) {
            // FIX: return actual error instead of null
            e.printStackTrace();  
            return "ERROR|" + e.toString();
        }
    }

    private void addBook(int year, String title, String author) throws SQLException {
        try (Connection conn = DbLibrary.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO books(year,title,author) VALUES(?, ?, ?)")) {

            ps.setInt(1, year);
            ps.setString(2, title);
            ps.setString(3, author);
            ps.executeUpdate();
        }
    }

    private void updateBook(int id, String title, String author, int year) throws SQLException {
        try (Connection conn = DbLibrary.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE books SET title = ?, author = ?, year = ? WHERE id = ?")) {

            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, year);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    private List<Book> viewBooks() throws SQLException {
        List<Book> list = new ArrayList<>();

        try (Connection conn = DbLibrary.getConnection();
             Statement smt = conn.createStatement();
             ResultSet rs = smt.executeQuery("SELECT * FROM books")) {

            while (rs.next()) {
                list.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("year")
                ));
            }
        }
        return list;
    }

    private void deleteBook(int id) throws SQLException {
        try (Connection conn = DbLibrary.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM books WHERE id = ?")) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
