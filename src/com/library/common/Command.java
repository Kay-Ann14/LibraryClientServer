package com.library.common;

import java.io.Serializable;

public class Command implements Serializable {

    private static final long serialVersionUID = 1L;
    private String action;
    private Book book;

    // Constructor for commands like ADD, UPDATE, DELETE
    public Command(String action, Book book) {
        this.action = action;
        this.book = book;
    }

    // NEW: constructor for commands like VIEW
    public Command(String action) {
        this.action = action;
        this.book = null;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
