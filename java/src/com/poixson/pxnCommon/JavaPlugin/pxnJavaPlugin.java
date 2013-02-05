package com.poixson.pxnCommon.JavaPlugin;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.pxnCommon.Language.pxnLanguageMessages;
import com.poixson.pxnCommon.Logger.pxnLogger;
import com.poixson.pxnCommon.dbPool.dbPool;
import com.poixson.pxnCommon.dbPool.dbPoolConn;


public abstract class pxnJavaPlugin extends JavaPlugin {

	// plugin state
	protected boolean isOk    = false;
	protected boolean isDebug = false;

	// plugin name
	public abstract String getPluginName();
	public abstract String getPluginFullName();


	public pxnJavaPlugin() {
		plugin = this;
	}
	protected static pxnJavaPlugin plugin = null;
	public static pxnJavaPlugin getPlugin() {
		return plugin;
	}


	// is plugin is loaded
	public boolean isOk() {
		return isOk;
	}
	// is debug mode
	public boolean isDebug() {
		return isDebug;
	}


	// plugin version
	public abstract String getRunningVersion();
	protected String availableVersion = null;
	public String getAvailableVersion() {
		return availableVersion;
	}
	public boolean isUpdateAvailable() {
		if(availableVersion == null)
			return false;
//TODO: compare versions
		return false;
	}


	// logger
	protected pxnLogger log = null;
	// get logger
	public pxnLogger getLog() {
		if(log == null)
			log = new pxnLogger("PoiXson Plugin");
		return log;
	}


	// config
	protected pxnConfig config = null;
	// get config
	public pxnConfig getPxnConfig() {
		return config;
	}


	// database pool
	protected dbPool db = null;
	// get db lock from pool
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


	protected void PluginAlreadyRunningMessage() {
		getServer().getConsoleSender().sendMessage(ChatColor.RED+"********************************************");
		getServer().getConsoleSender().sendMessage(ChatColor.RED+"*** WebAuctionPlus is already running!!! ***");
		getServer().getConsoleSender().sendMessage(ChatColor.RED+"********************************************");
		getLog().severe("Plugin is already loaded!! Can't load a second copy of this plugin.");
	}


}