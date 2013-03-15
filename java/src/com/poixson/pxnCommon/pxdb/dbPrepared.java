package com.poixson.pxnCommon.pxdb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.poixson.pxnCommon.Logger.pxnLogger;


public abstract class dbPrepared
implements com.poixson.pxnCommon.pxdb.interfaces.dbPrepared {

	protected PreparedStatement st = null;
	protected ResultSet rs = null;
	protected String sql = null;
	protected String args = "";
	protected boolean quiet = false;
	protected int resultInt = -1;


	public abstract Connection getConn();
	protected abstract pxnLogger getLog();


	// prepared statement
	public dbPoolConn Prepare(String sql) {
		if(sql == null) throw new NullPointerException("sql can't be null!");
		if(sql.isEmpty()) throw new IllegalArgumentException("sql can't be empty!");
		Clean();
		if(getConn() == null) return null;
		try {
			st = getConn().prepareStatement(sql);
			this.sql = sql;
			return (dbPoolConn) this;
		} catch (SQLException e) {
			e.printStackTrace();
			Clean();
			return null;
		}
	}


	// clean up
	@Override
	public void Clean() {
		st = null;
		rs = null;
		sql = null;
		args = "";
		quiet = false;
		resultInt = -1;
	}


	// execute query
	@Override
	public dbPoolConn Exec() {
		return Exec(null);
	}
	@Override
	public dbPoolConn Exec(String sql) {
		if(sql != null && !sql.isEmpty())
			if(Prepare(sql) == null)
				return null;
		if(st == null) return null;
		if(this.sql == null || this.sql.isEmpty()) return null;
		if(!quiet)
			getLog().debug("query", this.sql+(args.isEmpty() ? "" : "  ["+args+" ]") );
		try {
			int i = this.sql.indexOf(" ");
			String firstPart = (i==-1 ? this.sql : this.sql.substring(0, i) ).toUpperCase();
			if(firstPart.equals("INSERT") || firstPart.equals("UPDATE") || firstPart.equals("DELETE"))
				resultInt = st.executeUpdate();
			else
				rs = st.executeQuery();
			return (dbPoolConn) this;
		} catch (SQLException e) {
			e.printStackTrace();
			Clean();
			return null;
		}
	}


	// has next row
	@Override
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
	@Override
	public int getAffectedRows() {
		return getResultInt();
	}
	// insert id
	@Override
	public int getInsertId() {
		return getResultInt();
	}
	private int getResultInt() {
		if(rs == null) return -1;
		return resultInt;
	}


	// query parameters
	@Override
	public dbPoolConn setString(int index, String value) {
		if(st == null) return null;
		try {
			st.setString(index, value);
			args += " String: "+value;
			return (dbPoolConn) this;
		} catch (SQLException e) {
			e.printStackTrace();
			Clean();
			return null;
		}
	}
	@Override
	public dbPoolConn setInt(int index, int value) {
		if(st == null) return null;
		try {
			st.setInt(index, value);
			args += " Int: "+Integer.toString(value);
			return (dbPoolConn) this;
		} catch (SQLException e) {
			e.printStackTrace();
			Clean();
			return null;
		}
	}
	@Override
	public dbPoolConn setFloat(int index, double value) {
		if(st == null) return null;
		try {
			st.setDouble(index, value);
			args += " Double: "+Double.toString(value);
			return (dbPoolConn) this;
		} catch (SQLException e) {
			e.printStackTrace();
			Clean();
			return null;
		}
	}
	@Override
	public dbPoolConn setLong(int index, long value) {
		if(st == null) return null;
		try {
			st.setLong(index, value);
			args += " Long: "+Long.toString(value);
			return (dbPoolConn) this;
		} catch (SQLException e) {
			e.printStackTrace();
			Clean();
			return null;
		}
	}
	@Override
	public dbPoolConn setBool(int index, boolean value) {
		if(st == null) return null;
		try {
			st.setBoolean(index, value);
			args += " Bool: "+Boolean.toString(value);
			return (dbPoolConn) this;
		} catch (SQLException e) {
			e.printStackTrace();
			Clean();
			return null;
		}
	}


	// get string
	@Override
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
	@Override
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
	@Override
	public Double getFloat(String label) {
		try {
			if(rs != null)
				return rs.getDouble(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	// get long
	@Override
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
	@Override
	public Boolean getBool(String label) {
		try {
			if(rs != null)
				return rs.getBoolean(label);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	// set quiet
	public void setQuiet() {
		setQuiet(true);
	}
	public void setQuiet(boolean quiet) {
		this.quiet = quiet;
	}


}