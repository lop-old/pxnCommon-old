package com.poixson.pxnCommon.BukkitPlugin;

import java.util.HashMap;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.pxnCommon.Language.pxnLanguageMessages;
import com.poixson.pxnCommon.Listeners.pxnListenerServer;
import com.poixson.pxnCommon.Logger.FormatChat;
import com.poixson.pxnCommon.Logger.pxnLogger;
import com.poixson.pxnCommon.Task.pxnTask;
import com.poixson.pxnCommon.dbPool.dbPool;
import com.poixson.pxnCommon.dbPool.dbPoolConn;


public abstract class pxnPluginObjects extends JavaPlugin {


	public pxnPluginObjects() {
		super();
	}


	// is debug mode
	protected boolean isDebug = false;
	public boolean isDebug() {
		return isDebug;
	}


	// logger
	protected pxnLogger log = null;
	public pxnLogger getLog() {
		if(log == null)
			log = new pxnLogger("PoiXson Plugin");
		return log;
	}


	// chat formatter
	public static final FormatChat chat = new FormatChat("{darkgreen}[{white}WebAuction{darkgreen}] ");


//	// config
//	protected pxnConfig config = null;
//	// get config
//	public pxnConfig getPxnConfig() {
//		if(config == null)
//			config = pxnConfig.factory();
//		return config;
//	}


	// listeners
	protected PluginManager pm = null;
	protected void registerListener(Listener listener) {
		if(pm == null)
			pm = getServer().getPluginManager();
		pm.registerEvents(listener, this);
	}


	// database pool
	protected dbPool db = null;
	public dbPoolConn getDB() {
		if(db == null)
			return null;
		return db.getLock();
	}


	// language
	protected pxnLanguageMessages language = null;
	public pxnLanguageMessages getLang() {
		if(language == null)
			language = pxnLanguageMessages.factory();
		return language;
	}


	public Economy getEconomy() {
		return pxnListenerServer.getEconomy();
	}


	// tasks
	protected HashMap<String, pxnTask> tasks = new HashMap<String, pxnTask>();
	public HashMap<String, pxnTask> getTaskMap() {
		return tasks;
	}


}