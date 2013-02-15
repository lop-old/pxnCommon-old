package com.poixson.pxnCommon.dbPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class dbPrepared {

	protected Connection conn;


	// prepared statement
	protected PreparedStatement st = null;
	public dbPoolConn Prepare(String sql) {
		Cleanup();
		if(conn == null) return null;
		if(sql.startsWith("UPDATE") || sql.startsWith("DELETE"))
			isUpdateQuery = true;
		try {
			st = conn.prepareStatement(sql);
			return (dbPoolConn) this;
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
			return (dbPoolConn) this;
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
			return (dbPoolConn) this;
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
			return (dbPoolConn) this;
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
			return (dbPoolConn) this;
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
			return (dbPoolConn) this;
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
			return (dbPoolConn) this;
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