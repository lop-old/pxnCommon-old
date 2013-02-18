package com.poixson.pxnCommon.dbPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class dbPrepared {

	protected Connection conn;
	protected ResultSet rs = null;
	protected String sql = null;
	protected int count = -1;


	// prepared statement
	protected PreparedStatement st = null;
	public dbPoolConn Prepare(String sql) {
		if(sql == null) throw new NullPointerException("sql can't be null!");
		if(sql.isEmpty()) throw new IllegalArgumentException("sql can't be empty!");
		Cleanup();
		if(conn == null) return null;
		try {
			st = conn.prepareStatement(sql);
			this.sql = sql;
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
	public dbPoolConn Exec() {
		if(st == null) return null;
		if(sql == null || sql.isEmpty()) return null;
		try {
			if(sql.startsWith("UPDATE") || sql.startsWith("DELETE"))
				count = st.executeUpdate();
			else
				rs = st.executeQuery();
			return (dbPoolConn) this;
		} catch (SQLException e) {
			e.printStackTrace();
			Cleanup();
			return null;
		}
	}


	// has next row
	public boolean hasNext() {
		if(rs == null) return false;
		try {
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	// row count
	public int getCount() {
		if(rs == null) return -1;
		return count;
//		try {
//			return rs.getFetchSize();
//		} catch (SQLException e) {
//			e.printStackTrace();
//			return -1;
//		}
	}
	// get string
	public String getString(String label) {
		try {
			if(rs != null)
				return rs.getString(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	// get int
	public Integer getInt(String label) {
		try {
			if(rs != null)
				return rs.getInt(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	// get double
	public Double getDouble(String label) {
		try {
			if(rs != null)
				return rs.getDouble(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	// get long
	public Long getLong(String label) {
		try {
			if(rs != null)
				return rs.getLong(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	// get boolean
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
		sql = null;
		count = -1;
	}


}