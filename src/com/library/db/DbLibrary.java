package com.library.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbLibrary {
	public static final String URL ="jdbc:mysql://localhost:3307/library-db";
	public static final String USER = "root";
	public static final String PASS ="usbw";
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASS);
	}
}


