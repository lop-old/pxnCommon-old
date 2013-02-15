package com.poixson.pxnCommon.dbPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class dbConfig {

	private String host;
	private int    port;
	private String user;
	private String pass;
	private String database;


	public static dbConfig factory(String host, int port, String user, String pass, String database) {
		return new dbConfig(host, port, user, pass, database);
	}


	public dbConfig(String host, int port, String user, String pass, String database) {
		this.host = (host == null || host.isEmpty() || host.equals("localhost")) ? "127.0.0.1" : host;
		this.port = (port < 1) ? 3306 : port;
		if(user == null     || user.isEmpty())     throw new IllegalArgumentException("Database username not set!");
		if(pass == null     || pass.isEmpty())     throw new IllegalArgumentException("Database password not set!");
		if(database == null || database.isEmpty()) throw new IllegalArgumentException("database not set!");
		this.user = user;
		this.pass = pass;
		this.database = database;
	}


	public Connection Connect() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		return DriverManager.getConnection("jdbc:mysql://"+host+":"+Integer.toString(port)+"/"+database, user, pass);
	}


	public boolean equals(dbConfig config) {
		if(this.host     != config.host)     return false;
		if(this.port     != config.port)     return false;
		if(this.user     != config.user)     return false;
		if(this.pass     != config.pass)     return false;
		if(this.database != config.database) return false;
		return true;
	}


}