package com.data.bridge.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider {
	private static Connection c;
	public static Connection getConnection() throws ClassNotFoundException, SQLException
	{
		
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/data_bridge", "<USERNAME>", "<PASSWORD>");
		c.setAutoCommit(false);
		return c;
	}
}
