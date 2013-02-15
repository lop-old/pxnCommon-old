package com.poixson.pxnCommon.dbPool;

import java.sql.Connection;
import java.sql.SQLException;

import com.poixson.pxnCommon.pxnUtils;


public class dbPoolConn extends dbPrepared {

	protected dbPool parent;
	protected Boolean inUse = false;
	protected long lockTime = -1;

	private dbConfig config;


	public dbPoolConn(dbPool parent, dbConfig config) {
		if(parent == null) throw new NullPointerException("parent can't be null!");
		if(config == null) throw new NullPointerException("dbConfig can't be null!");
		this.parent = parent;
		this.config = config;
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
		if(parent.hasFailed()) return;
		// already connected
		try {
			if(conn != null)
				if(!conn.isClosed())
					return;
		} catch(SQLException ignore) {
			return;
		}
		try {
			parent.log.info("db", "Making a new MySQL connection..");
			conn = config.Connect();
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


	/**
	 *
	 *
	 * @return Connection
	 */
	public Connection getConn() {
		return conn;
	}


}