package com.poixson.pxnCommon.pxdb.interfaces;

import java.sql.Connection;


public interface dbPoolConn {

	public boolean inUse();
	public boolean getUse();
	public boolean hasError();
	public long getLockTime();
	public Connection getConn();
	public int getId();
	public void Release();

}