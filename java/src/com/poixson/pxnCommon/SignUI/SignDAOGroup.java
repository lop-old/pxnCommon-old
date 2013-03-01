package com.poixson.pxnCommon.SignUI;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.block.Block;

import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;
import com.poixson.pxnCommon.Logger.pxnLogger;
import com.poixson.pxnCommon.dbPool.dbPool;
import com.poixson.pxnCommon.dbPool.dbPoolConn;


public class SignDAOGroup {

	private boolean CheckOwnerEnabled = false;

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
	protected SignDAO getSignDAO(Block block) {
		return getSignDAO( SignDAO.BlockLocationToString(block) );
	}
	protected SignDAO getSignDAO(String location) {
		return getSignDAO(location, null, null);
	}
	protected SignDAO getSignDAO(String location, String type, String owner) {
		if(location == null) throw new NullPointerException("location can't be null");
		SignDAO sign = null;
		synchronized(signCache) {
			// get from cache
			if(signCache.containsKey(location)) {
				sign = signCache.get(location);
				// check sign type
				if(type != null && !type.equalsIgnoreCase(sign.getType()) ) {
log.warning("Sign type changed! sign is [ "+type+" ] but is set to [ "+sign.getType()+" ] in the cache!");
					return null;
				}
			}
			// get from db
			if(sign == null) {
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
					sign = new SignDAO(
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
				}
				db.releaseLock();
			}
		}
		// sign not found
		if(sign == null)
			return null;
		// check sign owner
		if(CheckOwnerEnabled && owner != null
		&& !owner.equalsIgnoreCase(sign.getOwner()) ) {
log.warning("Sign not owned by you! owned by [ "+sign.getOwner()+" ] but is being changed by [ "+owner+" ]");
			return null;
		}
		return sign;
	}
	// get/create sign dao
	protected SignDAO getNewSignDAO(Block block, String type, String owner) {
		return getNewSignDAO(SignDAO.BlockLocationToString(block), type, owner);
	}
	protected SignDAO getNewSignDAO(String location, String type, String owner) {
		if(location == null) throw new NullPointerException("location can't be null");
		SignDAO sign = getSignDAO(location, type, owner);
		if(sign == null)
			return createSignDAO(location, type, owner);
		return sign;
	}
	// create new sign dao
	private SignDAO createSignDAO(String location, String type, String owner) {
		if(location == null) throw new NullPointerException("location can't be null");
		dbPoolConn db = pool.getConnLock();
		db.Cleanup();
		db.Prepare("INSERT INTO `pxn_Signs` (`location`, `sign_type`, `line1`, `line2`, `line3`, `line4`, `owner`) VALUES (?, ?, NULL, NULL, NULL, NULL, ?)");
		db.setString(1, location);
		db.setString(2, type);
		db.setString(3, owner);
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
	protected void saveSignDAO(SignDAO sign) {
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


	// remove sign
	protected boolean removeSign(Block block) {
		return removeSign(SignDAO.BlockLocationToString(block));
	}
	protected boolean removeSign(String location) {
		if(location == null) return false;
		dbPoolConn db = pool.getConnLock();
		db.Prepare("DELETE FROM `pxn_Signs` WHERE `location` = ? LIMIT 1");
		db.setString(1, location);
		db.Exec();
		int count = db.getAffectedRows();
		db.releaseLock();
		// remove from cache
		signCache.remove(location);
		return (count > 0);
	}


	// add to no-sign cache
	protected void AddNoSignCache(String location) {
		noSignCache.add(location);
		while(noSignCache.size() > 5)
			noSignCache.poll();
	}
	// remove from no-sign cache
	protected void RemoveNoSignCache2(String location) {
		if(noSignCache.contains(location))
			noSignCache.remove(location);
	}


}