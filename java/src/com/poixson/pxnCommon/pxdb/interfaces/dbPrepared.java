package com.poixson.pxnCommon.pxdb.interfaces;

import java.sql.Connection;


public interface dbPrepared {

	public abstract Connection getConn();
	public dbPoolConn Prepare(String sql);
	public void Clean();
	public dbPoolConn Exec();
	public dbPoolConn Exec(String sql);
	public boolean hasNext();
	public int getAffectedRows();
	public int getInsertId();
	public dbPoolConn setString(int index, String value);
	public dbPoolConn setInt(int index, int value);
	public dbPoolConn setFloat(int index, double value);
	public dbPoolConn setLong(int index, long value);
	public dbPoolConn setBool(int index, boolean value);
	public String getString(String label);
	public Integer getInt(String label);
	public Double getFloat(String label);
	public Long getLong(String label);
	public Boolean getBool(String label);
	public void setQuiet();
	public void setQuiet(boolean quiet);

}