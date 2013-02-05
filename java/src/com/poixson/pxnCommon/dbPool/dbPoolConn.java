package com.poixson.pxnCommon.dbPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.poixson.pxnCommon.pxnUtils;
import com.poixson.pxnCommon.Logger.pxnLogger;


public class dbPoolConn {

	protected dbPool parent;
	protected Connection conn;
	protected Boolean inUse = false;
	protected long lockTime = -1;

	private final String host;
	private final int    port;
	private final String user;
	private final String pass;
	private final String database;


	public dbPoolConn(dbPool parent, String host, int port, String user, String pass, String database) {
		if(parent == null) throw new NullPointerException("parent can't be null!");
		this.parent = parent;
		if(host == null || host.isEmpty()) host = "localhost";
		if(port < 1) port = 3306;
		if(user == null || user.isEmpty()) throw new IllegalArgumentException("Database username not set!");
		if(pass == null || pass.isEmpty()) throw new IllegalArgumentException("Database password not set!");
		if(database == null || database.isEmpty()) throw new IllegalArgumentException("database not set!");
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		this.database = database;
		// connect to database
		_Connect();
	}


	public void releaseLock() {
		inUse = false;
	}


	public boolean inUse() {
		return inUse;
	}


	protected void _Connect() {
		if(!parent.isOk()) return;
		// already connected
		try {
			if(conn != null)
				if(!conn.isClosed())
					return;
		} catch(SQLException ignore) {}
		try {
			getLog().info("Making a new MySQL connection..");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+Integer.toString(port)+"/"+database, user, pass);
			if(conn == null || conn.isClosed()) {
				parent.addErrorMessage("There was a problem getting the MySQL connection!!!");
			} else {
				// connection ok
				return;
			}
		} catch (ClassNotFoundException e) {
			parent.addErrorMessage("Unable to load database driver!");
			getLog().exception(e);
			return;
		} catch (InstantiationException e) {
			parent.addErrorMessage("Unable to create database driver!");
			getLog().exception(e);
		} catch (IllegalAccessException e) {
			parent.addErrorMessage("Unable to create database driver!");
			getLog().exception(e);
		} catch (SQLException e) {
			parent.addErrorMessage("SQL Error!");
			getLog().exception(e);
		}
		conn = null;
	}


	/**
	 * Get the time connection has been locked for
	 *
	 * @return time in milliseconds
	 */
	public long getLockTime() {
		if(lockTime < 1)
			return -1;
		return pxnUtils.getCurrentMillis() - lockTime;
	}


	protected pxnLogger getLog() {
		return parent.getLog();
	}


}