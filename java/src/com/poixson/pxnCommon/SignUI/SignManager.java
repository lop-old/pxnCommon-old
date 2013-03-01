package com.poixson.pxnCommon.SignUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
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
	protected final SignDAOGroup signCacheGroup;


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
	public static SignManager factory(pxnPlugin plugin, SignPlugin signPlugin) {
		return factory(plugin).addHandler(signPlugin);
	}


	// new db instance
	private SignManager(pxnPlugin plugin) {
		if(plugin == null) throw new NullPointerException("plugin can't be null!");
		this.log = plugin.getLog();
		this.pluginName = plugin.getPluginName();
		this.pool = plugin.getDBPool();
		// signs cache
		signCacheGroup = new SignDAOGroup(plugin);
		// register listener
		plugin.registerListener(this);
		log.info(CONTAINER_NAME, "Loaded sign manager");
	}


	// add sign handler
	public SignManager addHandler(SignPlugin signPlugin) {
		if(signPlugin == null) throw new NullPointerException("signPlugin can't be null!");
		this.handlers.add(signPlugin);
		return this;
	}


	// player right-clicked sign
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
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
		String location = SignDAO.BlockLocationToString(block);
		// no sign cached
		if(signCacheGroup.hasNoSign(location))
			return;
		// is valid sign
		SignDAO sign = signCacheGroup.getSignDAO(location);
		if(sign == null) return;
		// it's our sign
		for(SignPlugin signPlugin : handlers) {
			if(signPlugin.onSignClick(event, sign))
				break;
			if(event.isCancelled())
				return;
		}
		event.setCancelled(true);

//TODO: add this
//		// prevent click spamming signs
//		if(plugin.lastSignUse.containsKey(player))
//			if( plugin.lastSignUse.get(player)+(long)plugin.signDelay > WebAuctionPlus.getCurrentMilli() ) {
//				p.sendMessage(WebAuctionPlus.chatPrefix + WebAuctionPlus.Lang.getString("please_wait"));
//				return;
//			}
//		plugin.lastSignUse.put(player, WebAuctionPlus.getCurrentMilli());

	}


	// new sign event
	@EventHandler(priority = EventPriority.NORMAL)
	public void onSignChange(SignChangeEvent event) {
		if(event.isCancelled()) return;
		String type = null;
		// validate sign
		for(SignPlugin signPlugin : handlers) {
			// create new sign
			type = signPlugin.onSignCreate(event);
			// sign created
			if(event.isCancelled())
				return;
			// success
			if(type != null) {
				// add to db / cache
				signCacheGroup.getNewSignDAO(
					event.getBlock(),
					type,
					event.getPlayer().getName()
				);
				return;
			}
		}
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


	// block break event
	@EventHandler(priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled()) return;
		Block block = event.getBlock();
		// not a sign
		if(block == null) return;
		if( block.getType() != Material.SIGN_POST &&
			block.getType() != Material.WALL_SIGN)
				return;
		// is valid sign
		SignDAO sign = signCacheGroup.getSignDAO(block);
		if(sign == null) return;
		// it's our sign
		for(SignPlugin signPlugin : handlers) {
			// try removing sign
			if(signPlugin.onSignRemove(event, sign))
				break;
			// cancelled
			if(event.isCancelled())
				return;
		}
		// remove sign from db/cache
		signCacheGroup.removeSign(event.getBlock());
	}


}