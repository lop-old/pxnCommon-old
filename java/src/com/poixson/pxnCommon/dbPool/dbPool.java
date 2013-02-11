package com.poixson.pxnCommon.dbPool;

import java.util.ArrayList;
import java.util.List;

import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;
import com.poixson.pxnCommon.Logger.pxnLogger;


public class dbPool {

	// plugin
	protected final pxnPlugin plugin;
	// logger
	protected final pxnLogger log;

	private final String host;
	private final int    port;
	private final String user;
	private final String pass;
	private final String database;

	protected List<dbPoolConn> pool = new ArrayList<dbPoolConn> (1);


	public dbPool(pxnPlugin plugin, String host, int port, String user, String pass, String database) {
		if(plugin == null) throw new NullPointerException("plugin can't be null!");
		this.plugin = plugin;
		log = plugin.getLog();
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
		// make first connection
		getLock().releaseLock();
	}


	public dbPoolConn getLock() {
		synchronized(pool) {
			for(dbPoolConn poolConn : pool) {
				if(!poolConn.inUse()) {
					poolConn.inUse = true;
					return poolConn;
				}
			}
			// new connection
			dbPoolConn db = new dbPoolConn(this, host, port, user, pass, database);
			pool.add(db);
			return db;
		}
	}


//	// get a lock from pool
//	public MySQLPoolConn getLock() {
//		// find an available connection
//		synchronized(pool) {
//			for(MySQLPoolConn poolConn : pool) {
//				if(poolConn.getLock()) return poolConn;
//			}
//			// check max pool size
//			if(pool.size() >= ConnPoolSize_Hard) {
//				log.severe(logPrefix+"DB connection pool is full! Hard limit reached!  Size: "+Integer.toString(pool.size()));
//				return null;
//			}
//			if(pool.size() >= ConnPoolSize_Warn)
//				log.warning(logPrefix+"DB connection pool is full! Warning limit reached.  Size: "+Integer.toString(pool.size()));
//			// make a new connection
//			MySQLPoolConn poolConn = new MySQLPoolConn(this, dbHost, dbPort, dbUser, dbPass, dbName, dbPrefix);
//			pool.add(poolConn);
//			poolConn.getLock();
//			return poolConn;
//		}
//	}


}