package com.library.cilent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import com.library.common.Book;
import com.library.common.Command;

public class LibraryCilent {
    private final String host;
    private final int port;

    public LibraryCilent(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private Object send(Command cmd) throws Exception {
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(socket.getInputStream())) {

            os.writeObject(cmd);
            os.flush();

            return is.readObject();
        }
    }

    public String add(String title, String author, int year) throws Exception {
        Command cmd = new Command("ADD", new Book(0, title, author, year));
        Object resp = send(cmd);
        return (String) resp;
    }

    public String update(int id, String title, String author, int year) throws Exception {
        Command cmd = new Command("UPDATE", new Book(id, title, author, year));
        Object resp = send(cmd);
        return (String) resp;
    }

    public String delete(int id) throws Exception {
        Command cmd = new Command("DELETE", new Book(id, "", "", 0));
        Object resp = send(cmd);
        return (String) resp;
    }

    @SuppressWarnings("unchecked")
    public List<Book> view() throws Exception {
        Command cmd = new Command("VIEW");
        Object resp = send(cmd);

        if (resp instanceof List<?>) {
            return (List<Book>) resp;
        } else if (resp instanceof String) {
            String msg = (String) resp;
            throw new Exception("Server error: " + msg);
        } else {
            throw new Exception("Unexpected response type: " + resp.getClass().getName());
        }
    }
}
