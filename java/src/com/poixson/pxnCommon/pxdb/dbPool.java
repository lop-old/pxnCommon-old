package com.poixson.pxnCommon.pxdb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.poixson.pxnCommon.pxnUtils;
import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;
import com.poixson.pxnCommon.Logger.pxnLogger;


public class dbPool
implements com.poixson.pxnCommon.pxdb.interfaces.dbPool {

	// plugin
	protected final pxnPlugin plugin;
	// logger
	protected pxnLogger log;

	// db config
	private final dbConfig config;
	// connections
	protected List<dbPoolConn> pool = new ArrayList<dbPoolConn> (1);
	// pools
	protected static List<dbPool> staticPools = new ArrayList<dbPool>();


	public static dbPool factory(pxnPlugin plugin, dbConfig config) {
		if(plugin.okEquals(false)) return null;
		synchronized(staticPools) {
			dbPool pool = null;
			// check existing pools
			for(dbPool p : staticPools) {
				if(p.config.equals(config)) {
					pool = p;
					// shared logger
					if(pool.log == null)
						pool.log = new pxnLogger("pxnCommon");
					pool.log.info("db", "Using an existing db pool :-)");
					//pool.log.debug("db", "Database pool size: "+Integer.toString(staticPools.size()));
					break;
				}
			}
			// new pool
			if(pool == null) {
				plugin.getLog().info("db", "Creating a new db pool..");
				pool = new dbPool(plugin, config);
				staticPools.add(pool);
				pool.log.debug("db", "Database pool size: "+Integer.toString(staticPools.size()));
			}
			return pool;
		}
	}


	private dbPool(pxnPlugin plugin, dbConfig config) {
		if(plugin == null) throw new NullPointerException("plugin can't be null!");
		this.plugin = plugin;
		this.log = plugin.getLog();
		if(config == null) throw new NullPointerException("dbConfig can't be null!");
		this.config = config;
		// force first connection
		dbPoolConn db = getDB();
		if(db == null) {
			plugin.errorMsg("Failed to get a database connection!");
			log.warning(config.dump());
			return;
		}
		db.Release();
	}


	@Override
	public dbPoolConn getDB() {
		// plugin disabled
		if(plugin.okEquals(false)) return null;
		synchronized(pool) {
			dbPoolConn db = null;
			// check existing connections
			Iterator<dbPoolConn> it = this.pool.iterator();
			while(it.hasNext()) {
				dbPoolConn poolConn = it.next();
				// connection reset or errored
				if(poolConn.hasError()) {
					log.severe("db", "Connection [ "+Integer.toString(poolConn.getId())+" ] dropped!!");
					it.remove();
					continue;
				}
				if(!poolConn.inUse()) {
					poolConn.inUse = true;
					log.debug("db", "Connection pool size: "+Integer.toString(pool.size()));
					db = poolConn;
					break;
				}
			}
			// new connection
			if(db == null) {
				// try 5 times max
				for(int i=0; i<5; i++) {
					db = new dbPoolConn(this, config);
					if(!db.hasError())
						break;
					db = null;
					log.severe("db", "Failed to connect to database! Trying again in 1 second..");
					pxnUtils.Sleep(1000);
				}
				// failed to connect to db, disable plugin
				if(db == null || db.hasError()) {
					log.severe("db", "Failed to connect to database! Disabling plugin..");
					plugin.onDisable();
					return null;
				}
				// new connection successful
				this.pool.add(db);
				log.debug("db", "Connection pool size: "+Integer.toString(pool.size()));
			}
			return db;
		}
	}


//TODO: use or remove this
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