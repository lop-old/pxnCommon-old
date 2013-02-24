package com.poixson.pxnCommon.SignUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;
import com.poixson.pxnCommon.Logger.pxnLogger;
import com.poixson.pxnCommon.dbPool.dbPool;


public class SignManager implements Listener {
	protected static final String CONTAINER_NAME = "Sign-Manager";

	// instances
	private static final List<SignManager> managers = new ArrayList<SignManager>();

	// logger
	protected final pxnLogger log;
	protected final String pluginName;
	// database
	protected final dbPool pool;

	// sign handlers
	protected final List<SignPlugin> handlers = new ArrayList<SignPlugin>();
	// signs cache
	protected final SignDAOGroup signCache;


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
		// signs cache
		signCache = new SignDAOGroup(plugin);
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
		String type = null;
		// validate sign
		for(SignPlugin handler : handlers) {
			// create new sign
			type = handler.onCreateSign(event);
			if(event.isCancelled()) return;
			// sign created
			if(type != null) break;
		}
		String location = SignDAO.BlockLocationToString(event.getBlock());
		// add to db / cache
		signCache.getNewSignDAO(location, type, event.getPlayer().getName());
	}


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


	// player interact
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		// right click only
		if( event.getAction() != Action.RIGHT_CLICK_BLOCK &&
			event.getAction() != Action.RIGHT_CLICK_AIR)
				return;
		Block block = event.getClickedBlock();
		// not a sign
		if(block == null) return;
		if( block.getType() != Material.SIGN_POST &&
			block.getType() != Material.WALL_SIGN)
				return;
		// is valid sign
		String location = SignDAO.BlockLocationToString(block);
		SignDAO sign = signCache.getSignDAO(location);
		if(sign == null) return;
		// it's our sign
		event.setCancelled(true);
		for(SignPlugin signPlugin : handlers)
			signPlugin.onClick(event, sign);
//		Sign signBlock = (Sign) block.getState();
//		String location = SignDAO.BlockLocationToString(block);
//		SignDAO sign = signCache.getSignDAO(location);
//		String[] lines = sign.getLines();
//		if(!lines[0].equals("[WebAuction+]")) return;
//		// get player info
//		Player p = event.getPlayer();
//		String player = p.getName();
//
//		// prevent click spamming signs
//		if(plugin.lastSignUse.containsKey(player))
//			if( plugin.lastSignUse.get(player)+(long)plugin.signDelay > WebAuctionPlus.getCurrentMilli() ) {
//				p.sendMessage(WebAuctionPlus.chatPrefix + WebAuctionPlus.Lang.getString("please_wait"));
//				return;
//			}
//		plugin.lastSignUse.put(player, WebAuctionPlus.getCurrentMilli());
//
//		// Shout sign
//		if(lines[1].equals("Shout")) {
//			clickSignShout(block.getLocation());
//			return;
//		}
//
//		// Mailbox (items)
//		if(lines[1].equals("MailBox")) {
//			if(!p.hasPermission("wa.use.mailbox")) {
//				p.sendMessage(WebAuctionPlus.chatPrefix + WebAuctionPlus.Lang.getString("no_permission"));
//				return;
//			}
//			// disallow creative
//			if(p.getGameMode() != GameMode.SURVIVAL && !p.isOp()) {
//				p.sendMessage(WebAuctionPlus.chatPrefix + WebAuctionPlus.Lang.getString("no_cheating"));
//				return;
//			}
//			// load virtual chest
//			WebInventory.onInventoryOpen(p);
//			return;
//		}

	}


}