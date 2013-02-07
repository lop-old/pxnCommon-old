package com.poixson.pxnCommon.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.pxnCommon.Language.pxnLanguageMessages;
import com.poixson.pxnCommon.Logger.pxnLogger;
import com.poixson.pxnCommon.dbPool.dbPool;
import com.poixson.pxnCommon.dbPool.dbPoolConn;


public abstract class pxnJavaPlugin extends JavaPlugin {

	// plugin state
//	protected boolean isOk    = false;
	protected boolean isDebug = false;


	// plugin name
	public abstract String getPluginName();
//	public abstract String getPluginFullName();


	// plugin instance
	protected static pxnJavaPlugin pxnPlugin = null;
	public pxnJavaPlugin() {
		// only one instance allowed
		if(pxnPlugin != null) {
			PluginAlreadyRunningMessage();
			return;
		}
		pxnPlugin = this;
	}
	public static pxnJavaPlugin getPlugin() {
		return pxnPlugin;
	}


	// is debug mode
	public boolean isDebug() {
		return isDebug;
	}


	// plugin version
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
//	public abstract String getRunningVersion();


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


	// error messages
	protected List<String> errorMsgs = new ArrayList<String>();
	public boolean isOk() {
		return errorMessages.isOksize();
	}
	protected void addErrorMsg(String msg) {
		getLog().severe(msg);
		synchronized(errorMessages) {
			errorMsgs.add(msg);
		}
	}


	protected void PluginAlreadyRunningMessage() {
		getServer().getConsoleSender().sendMessage(ChatColor.RED+"********************************************");
		getServer().getConsoleSender().sendMessage(ChatColor.RED+"*** WebAuctionPlus is already running!!! ***");
		getServer().getConsoleSender().sendMessage(ChatColor.RED+"********************************************");
		addErrorMsg("Plugin is already loaded!! Can't load a second copy of this plugin!");
	}


}