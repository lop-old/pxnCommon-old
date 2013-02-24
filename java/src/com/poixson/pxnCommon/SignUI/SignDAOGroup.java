package com.poixson.pxnCommon.SignUI;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;
import com.poixson.pxnCommon.Logger.pxnLogger;
import com.poixson.pxnCommon.dbPool.dbPool;
import com.poixson.pxnCommon.dbPool.dbPoolConn;


public class SignDAOGroup {

	// signs cache
	private final HashMap<String, SignDAO> signCache = new HashMap<String, SignDAO>();
	// no sign cache
	protected final ConcurrentLinkedQueue<String> noSignCache = new ConcurrentLinkedQueue<String>();

	private final pxnLogger log;
	private final dbPool pool;


	protected SignDAOGroup(pxnPlugin plugin) {
		this.log  = plugin.getLog();
		this.pool = plugin.getDBPool();
	}


	// get sign dao
	public SignDAO getSignDAO(String location) {
		return getSignDAO(location, null);
	}
	public SignDAO getSignDAO(String location, String type) {
		if(location == null) throw new NullPointerException("location can't be null");
		// get from cache
		if(signCache.containsKey(location)) {
			SignDAO sign = signCache.get(location);
			// check sign type
			if(type != null) {
				if(!type.equalsIgnoreCase(sign.getType())) {
log.warning("Sign type changed! sign is [ "+type+" ] but is set to [ "+sign.getType()+" ] in the cache!");
					return null;
				}
			}
			return sign;
		}
		// get from db
		dbPoolConn db = pool.getConnLock();
		db.Prepare("SELECT `sign_id`, `location`, `sign_type`, `line1`, `line2`, `line3`, `line4`, `owner` FROM `pxn_Signs` WHERE `location` = ? LIMIT 1");
		db.setString(1, location);
		db.Exec();
		if(db.hasNext()) {
			// check sign type
			if(type != null) {
				if(!type.equalsIgnoreCase(db.getString("sign_type"))) {
log.warning("Sign type changed! sign is [ "+type+" ] but is set to [ "+db.getString("sign_type")+" ] in the database!");
					return null;
				}
			}
			SignDAO sign = new SignDAO(
				db.getInt("sign_id"),
				db.getString("location"),
				db.getString("sign_type")
			);
			sign.setLine(1, db.getString("line1") );
			sign.setLine(2, db.getString("line2") );
			sign.setLine(3, db.getString("line3") );
			sign.setLine(4, db.getString("line4") );
			sign.setOwner( db.getString("owner") );
			signCache.put(location, sign);
			db.releaseLock();
			return sign;
		}
		db.releaseLock();
		return null;
	}
	// get/create sign dao
	public SignDAO getNewSignDAO(String location, String type) {
		if(location == null) throw new NullPointerException("location can't be null");
		SignDAO sign = getSignDAO(location, type);
		if(sign == null)
			return createSignDAO(location, type);
		return sign;
	}
	// create new sign dao
	private SignDAO createSignDAO(String location, String type) {
		if(location == null) throw new NullPointerException("location can't be null");
		dbPoolConn db = pool.getConnLock();
		db.Cleanup();
		db.Prepare("INSERT INTO `pxn_Signs` (`location`, `sign_type`, `line1`, `line2`, `line3`, `line4`, `owner`) VALUES (?, ?, NULL, NULL, NULL, NULL, NULL)");
		db.setString(1, location);
		db.setString(2, type);
		db.Exec();
		SignDAO sign = new SignDAO(
			db.getInsertId(),
			location,
			type
		);
		signCache.put(location, sign);
		saveSignDAO(sign);
		db.releaseLock();
		return sign;
	}
	// save sign dao
	public void saveSignDAO(SignDAO sign) {
		if(sign == null) throw new NullPointerException("sign can't be null!");
		dbPoolConn db = pool.getConnLock();
		db.Prepare("UPDATE `pxn_Signs` SET `line1` = ?, `line2` = ?, `line3` = ?, `line4` = ?, `owner` = ? WHERE `location` = ? LIMIT 1");
		db.setString(1, sign.line1);
		db.setString(2, sign.line2);
		db.setString(3, sign.line3);
		db.setString(4, sign.line4);
		db.setString(5, sign.owner);
		db.setString(6, sign.location);
		db.releaseLock();
	}


	// add to no-sign cache
	public void AddNoSignCache(String location) {
		noSignCache.add(location);
		while(noSignCache.size() > 5)
			noSignCache.poll();
	}
	// remove from no-sign cache
	public void RemoveNoSignCache2(String location) {
		if(noSignCache.contains(location))
			noSignCache.remove(location);
	}


}