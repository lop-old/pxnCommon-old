package com.poixson.pxnCommon.SignUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;
import com.poixson.pxnCommon.Logger.pxnLogger;
import com.poixson.pxnCommon.dbPool.dbPool;
import com.poixson.pxnCommon.dbPool.dbPoolConn;


public class SignManager implements Listener {
	protected static final String CONTAINER_NAME = "Sign-Manager";

	// instances
	private static List<SignManager> managers = new ArrayList<SignManager>();

	// logger
	protected final pxnLogger log;
	protected final String pluginName;
	// database
	private dbPool pool;

	// sign handlers
	protected List<SignPlugin> handlers = new ArrayList<SignPlugin>();
	// signs cache
	protected HashMap<String, SignDAO> signsCache = new HashMap<String, SignDAO>();


	// factory
	public static SignManager factory(pxnPlugin plugin) {
		synchronized(managers) {
			SignManager manager = null;
			dbPool pool = plugin.getDBPool();
			String pluginName = plugin.getPluginName();
			// check existing sign managers
			for(SignManager m : managers) {
				//if(m.pool == null) continue;
				// match db
				if( m.pool.equals(pool) && m.pluginName.equals(pluginName) ) {
					manager = m;
					break;
				}
			}
			if(manager == null) {
				manager = new SignManager(plugin);
				managers.add(manager);
			}
			return manager;
		}
	}
	public static SignManager factory(pxnPlugin plugin, SignPlugin handler) {
		return factory(plugin).addHandler(handler);
	}


	// new db instance
	private SignManager(pxnPlugin plugin) {
		if(plugin == null) throw new NullPointerException("plugin can't be null!");
		this.log = plugin.getLog();
		this.pluginName = plugin.getPluginName();
		this.pool = plugin.getDBPool();
		// register listener
		plugin.registerListener(this);
		log.info(CONTAINER_NAME, "Loaded sign manager");
	}


	// add sign handler
	public SignManager addHandler(SignPlugin handler) {
		if(handler == null) throw new NullPointerException("handler can't be null!");
		this.handlers.add(handler);
		return this;
	}


	// new sign event
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignChange(SignChangeEvent event) {
		if(event.isCancelled()) return;
		// validate sign
		for(SignPlugin handler : handlers) {
			// sign created
			if(handler.ValidateSign(event))
				break;
			if(event.isCancelled())
				return;
		}
//		if(handled) {
//@SuppressWarnings("unused")
//SignDAO sign = getSignDAO(
//				SignDAO.BlockLocationToString(event.getBlock())
//			);
//			saveSignDAO(sign);
//		}
	}


	// get/create sign dao
	protected SignDAO getSignDAO(String location) {
		if(location == null) throw new NullPointerException("location can't be null");
		SignDAO sign = null;
		// get from cache
		if(signsCache.containsKey(location))
			sign = signsCache.get(location);
		// get from db
		dbPoolConn db = pool.getConnLock();
		if(sign == null) {
			db.Prepare("SELECT `sign_id`, `location`, `line1`, `line2`, `line3`, `line4`, `owner` FROM `pxn_Signs` WHERE `location` = ? LIMIT 1");
			db.setString(1, location);
			db.Exec();
			if(db.hasNext()) {
				sign = new SignDAO(
					db.getInt("sign_id"),
					db.getString("location")
				);
				sign.setLine(1, db.getString("line1") );
				sign.setLine(2, db.getString("line2") );
				sign.setLine(3, db.getString("line3") );
				sign.setLine(4, db.getString("line4") );
				sign.setOwner( db.getString("owner") );
				signsCache.put(location, sign);
			}
		}
		// create new
		if(sign == null) {
			db.Cleanup();
			db.Prepare("INSERT INTO `pxn_Signs` (`location`, `line1`, `line2`, `line3`, `line4`, `owner`) VALUES (?, NULL, NULL, NULL, NULL, NULL)");
			db.setString(1, location);
			db.Exec();
			sign = new SignDAO(
				db.getInsertId(),
				location
			);
			signsCache.put(location, sign);
			saveSignDAO(sign);
		}
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


//		Block block = event.getBlock();
//		World world = block.getWorld();
//		Player p = event.getPlayer();
//		if(p == null) return;
//		if(!handler.ValidateLines(event)) {
//		}

//		if(!lines[0].equalsIgnoreCase("[WebAuction]") &&
//			!lines[0].equalsIgnoreCase("[WebAuction+]") &&
//			!lines[0].equalsIgnoreCase("[wa]") ) return;
//		event.setLine(0, "[WebAuction+]");
//
//		// Shout sign
//		if(lines[1].equalsIgnoreCase("Shout")) {
//			if(!p.hasPermission("wa.create.sign.shout")) {
//				NoPermission(event);
//				return;
//			}
//			event.setLine(1, "Shout");
//			// line 2: radius
//			int radius = 20;
//			try {
//				radius = Integer.parseInt(lines[2]);
//			} catch (NumberFormatException ignore) {}
//			event.setLine(2, Integer.toString(radius));
//			event.setLine(3, "");
//			plugin.shoutSigns.put(sign.getLocation(), radius);
//			WebAuctionPlus.dataQueries.createShoutSign(world, radius, sign.getX(), sign.getY(), sign.getZ());
//			p.sendMessage(WebAuctionPlus.chatPrefix + WebAuctionPlus.Lang.getString("created_shout_sign"));
//			return;
//		}


//	// block break event
//	@EventHandler(priority = EventPriority.LOWEST)
//	public void onBlockBreak(BlockBreakEvent event) {
//		if(event.isCancelled()) return;
//		Block block = event.getBlock();
//		Player p = event.getPlayer();
//		// block is sign
//		if(block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST) {
//			//Sign sign = (Sign) block.getState();
//			// sign exists in db
//			if(dbSignExists(block)) {
//				// has permission
//				if(p.hasPermission("wa.sign.remove")) {
//					dbSignRemove(block);
//p.sendMessage("sign removed");
//log.info("sign removed");
//				// no permission
//				} else {
//					event.setCancelled(true);
//p.sendMessage(WebAuctionPlus.chatPrefix + WebAuctionPlus.Lang.getString("no_permission"));
//p.sendMessage("no permission to break sign");
//				}
//			}
//			return;
//		}
//		if() {
//TODO: if block face contains sign
//		}
//	}


//	// sign exists in db
//	protected boolean dbSignExists(Block block) {
//		return dbSignExists(LocationToString(block));
//	}
//	protected boolean dbSignExists(String location) {
//		if(location == null) throw new NullPointerException("location can't be null!");
//		dbPoolConn db = pool.getConnLock();
//		db.Prepare("SELECT COUNT(*) AS `count` FROM `pxn_Signs` WHERE `location` = ? LIMIT 1");
//		db.setString(1, location);
//		db.Exec();
//		int count = -1;
//		if(db.hasNext())
//			count = db.getInt("count");
//		db.releaseLock();
//		return (count > 0);
//	}


//	// remove sign from db
//	protected boolean dbSignRemove(Block block) {
//		return dbSignRemove(LocationToString(block));
//	}
//	protected boolean dbSignRemove(String location) {
//		if(location == null) throw new NullPointerException("location can't be null!");
//		dbPoolConn db = pool.getConnLock();
//		db.Prepare("DELETE FROM `pxn_Signs` WHERE `location` = ? LIMIT 1");
//		db.setString(1, location);
//		db.Exec();
//		int count = db.getCount();
//		db.releaseLock();
//		return (count > 0);
//	}


}