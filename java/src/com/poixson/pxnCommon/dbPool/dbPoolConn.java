package com.poixson.pxnCommon.dbPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.poixson.pxnCommon.pxnUtils;


public class dbPoolConn {

	protected dbPool parent;
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
		if(!parent.plugin.isOk()) return;
		// already connected
		try {
			if(conn != null)
				if(!conn.isClosed())
					return;
		} catch(SQLException ignore) {
			return;
		}
		try {
			parent.log.info("Making a new MySQL connection..");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://"+host+":"+Integer.toString(port)+"/"+database, user, pass);
			if(conn == null || conn.isClosed()) {
				parent.plugin.errorMsg("There was a problem getting the MySQL connection!!!");
			} else {
				// connection ok
				return;
			}
		} catch (ClassNotFoundException e) {
			parent.plugin.errorMsg("Unable to load database driver!");
			parent.log.exception(e);
			return;
		} catch (InstantiationException e) {
			parent.plugin.errorMsg("Unable to create database driver!");
			parent.log.exception(e);
		} catch (IllegalAccessException e) {
			parent.plugin.errorMsg("Unable to create database driver!");
			parent.log.exception(e);
		} catch (SQLException e) {
			parent.plugin.errorMsg("SQL Error!");
			parent.log.exception(e);
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


	// ******************************
	// prepared statement instance
	// ******************************


	// get connection
	protected Connection conn;
	public Connection getConn() {
		return conn;
	}


	// prepared statement
	protected PreparedStatement st = null;
	public dbPoolConn Prepare(String sql) {
		Cleanup();
		if(conn == null) return null;
		if(sql.startsWith("UPDATE") || sql.startsWith("DELETE"))
			isUpdateQuery = true;
		try {
			st = conn.prepareStatement(sql);
			return this;
		} catch (SQLException e) {
			e.printStackTrace();
			Cleanup();
			return null;
		}
	}


	// query parameters
	public dbPoolConn setString(int index, String value) {
		if(st == null) return null;
		try {
			st.setString(index, value);
			return this;
		} catch (SQLException e) {
			e.printStackTrace();
			Cleanup();
			return null;
		}
	}
	public dbPoolConn setInt(int index, int value) {
		if(st == null) return null;
		try {
			st.setInt(index, value);
			return this;
		} catch (SQLException e) {
			e.printStackTrace();
			Cleanup();
			return null;
		}
	}
	public dbPoolConn setDouble(int index, double value) {
		if(st == null) return null;
		try {
			st.setDouble(index, value);
			return this;
		} catch (SQLException e) {
			e.printStackTrace();
			Cleanup();
			return null;
		}
	}
	public dbPoolConn setLong(int index, long value) {
		if(st == null) return null;
		try {
			st.setLong(index, value);
			return this;
		} catch (SQLException e) {
			e.printStackTrace();
			Cleanup();
			return null;
		}
	}
	public dbPoolConn setBoolean(int index, boolean value) {
		if(st == null) return null;
		try {
			st.setBoolean(index, value);
			return this;
		} catch (SQLException e) {
			e.printStackTrace();
			Cleanup();
			return null;
		}
	}


	// execute query
	private boolean isUpdateQuery = false;
	public dbPoolConn Exec() {
		if(st == null) return null;
		try {
			if(isUpdateQuery)
				st.executeUpdate();
			else
				rs = st.executeQuery();
			return this;
		} catch (SQLException e) {
			e.printStackTrace();
			Cleanup();
			return null;
		}
	}


	// result set
	protected ResultSet rs = null;
	public boolean Next() {
		if(rs == null) return false;
		try {
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	public String getString(String label) {
		try {
			if(rs != null)
				return rs.getString(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Integer getInt(String label) {
		try {
			if(rs != null)
				return rs.getInt(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Double getDouble(String label) {
		try {
			if(rs != null)
				return rs.getDouble(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Long getLong(String label) {
		try {
			if(rs != null)
				return rs.getLong(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Boolean getBoolean(String label) {
		try {
			if(rs != null)
				return rs.getBoolean(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	// clean up
	public void Cleanup() {
		st = null;
		rs = null;
		isUpdateQuery = false;
	}


}