package com.poixson.pxnCommon.SignUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;
import com.poixson.pxnCommon.Logger.pxnLogger;
import com.poixson.pxnCommon.dbPool.dbPool;
import com.poixson.pxnCommon.dbPool.dbPoolConn;


public class SignManager implements Listener {
	private static final String CONTAINER_NAME = "Sign Manager";

	// instances
	private static List<SignManager> managers = new ArrayList<SignManager>();

	// signs cache
//	protected List<SignDAO> dbSigns = new ArrayList<SignDAO>();

	protected String pluginName;
	protected pxnLogger log;

	// database
	private dbPool pool;


	public static SignManager factory(pxnPlugin plugin) {
		synchronized(managers) {
			SignManager manager = null;
			// check existing sign managers
			for(SignManager m : managers) {
				//if(m.pluginName == null || m.pool == null) continue;
				if(m.pluginName.equals( plugin.getPluginName() )
				&& m.pool.equals( plugin.getDBPool() )) {
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


	private SignManager(pxnPlugin plugin) {
		if(plugin == null) throw new NullPointerException("plugin can't be null!");
		this.pluginName = plugin.getPluginName();
		this.log = plugin.getLog();
		this.pool = plugin.getDBPool();
		// register listener
		plugin.registerListener(this);
		log.info(CONTAINER_NAME, "Loaded Sign Manager");
	}


	// block break event
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled()) return;
		Block block = event.getBlock();
		Player p = event.getPlayer();
		// block is sign
		if(block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST) {
//			Sign sign = (Sign) block.getState();
			// sign exists in db
			if(dbSignExists(block)) {
				// has permission
				if(p.hasPermission("wa.sign.remove")) {
					dbSignRemove(block);
p.sendMessage("sign removed");
log.info("sign removed");
				// no permission
				} else {
					event.setCancelled(true);
//p.sendMessage(WebAuctionPlus.chatPrefix + WebAuctionPlus.Lang.getString("no_permission"));
p.sendMessage("no permission to break sign");
				}
			}
			return;
		}
//		if() {
//TODO: if block face contains sign
//		}
	}


	// format location string
	protected static String LocationToString(Block block) {
		if(block == null) throw new NullPointerException("sign can't be null!");
		Location loc = block.getLocation();
		return
			loc.getWorld().getName()+":"+
			Integer.toString(loc.getBlockX())+":"+
			Integer.toString(loc.getBlockY())+":"+
			Integer.toString(loc.getBlockZ());
	}


	// sign exists in db
	protected boolean dbSignExists(Block block) {
		return dbSignExists(LocationToString(block));
	}
	protected boolean dbSignExists(String location) {
		if(location == null) throw new NullPointerException("location can't be null!");
		dbPoolConn db = pool.getConnLock();
		db.Prepare("SELECT COUNT(*) AS `count` FROM `pxn_Signs` WHERE `location` = ? LIMIT 1");
		db.setString(1, location);
		db.Exec();
		int count = -1;
		if(db.hasNext())
			count = db.getInt("count");
		db.releaseLock();
		return (count > 0);
	}


	// remove sign from db
	protected boolean dbSignRemove(Block block) {
		return dbSignRemove(LocationToString(block));
	}
	protected boolean dbSignRemove(String location) {
		if(location == null) throw new NullPointerException("location can't be null!");
		dbPoolConn db = pool.getConnLock();
		db.Prepare("DELETE FROM `pxn_Signs` WHERE `location` = ? LIMIT 1");
		db.setString(1, location);
		db.Exec();
		int count = db.getCount();
		db.releaseLock();
		return (count > 0);
	}


}