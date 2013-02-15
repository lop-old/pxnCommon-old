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

	// db config
	private final dbConfig config;
	// connections
	protected List<dbPoolConn> pool = new ArrayList<dbPoolConn> (1);
	// pools
	protected static List<dbPool> staticPools = new ArrayList<dbPool>();


	public static dbPool factory(pxnPlugin plugin, dbConfig config) {
		if(plugin.okEquals(false)) return null;
		// check existing pools
		for(dbPool pool : staticPools) {
			if(pool.config.equals(config)) {
				plugin.getLog().info("db", "Using an existing db pool :-)");
				plugin.getLog().debug("db", "database pool size: "+Integer.toString(staticPools.size()));
				return pool;
			}
		}
		// new pool
		plugin.getLog().info("db", "Creating a new db pool..");
		dbPool pool = new dbPool(plugin, config);
		staticPools.add(pool);
		plugin.getLog().debug("db", "database pool size: "+Integer.toString(staticPools.size()));
		return pool;
	}


	private dbPool(pxnPlugin plugin, dbConfig config) {
		if(plugin == null) throw new NullPointerException("plugin can't be null!");
		this.plugin = plugin;
		this.log = plugin.getLog();
		if(config == null) throw new NullPointerException("dbConfig can't be null!");
		this.config = config;
		// force first connection
		getConnLock().releaseLock();
	}


	public dbPoolConn getConnLock() {
		if(hasFailed()) return null;
		synchronized(pool) {
			for(dbPoolConn poolConn : pool) {
				if(!poolConn.inUse()) {
					poolConn.inUse = true;
					plugin.getLog().debug("db", "connection pool size: "+Integer.toString(pool.size()));
					return poolConn;
				}
			}
			// new connection
			dbPoolConn db = new dbPoolConn(this, config);
			pool.add(db);
			plugin.getLog().debug("db", "connection pool size: "+Integer.toString(pool.size()));
			return db;
		}
	}


	public boolean hasFailed() {
		return !plugin.okEquals(false);
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