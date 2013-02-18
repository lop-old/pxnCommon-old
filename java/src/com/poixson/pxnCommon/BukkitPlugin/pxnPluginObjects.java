package com.poixson.pxnCommon.BukkitPlugin;

import java.util.HashMap;

import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.pxnCommon.Language.pxnLanguageMessages;
import com.poixson.pxnCommon.Logger.FormatChat;
import com.poixson.pxnCommon.Logger.pxnLogger;
import com.poixson.pxnCommon.Task.pxnTask;
import com.poixson.pxnCommon.dbPool.dbPool;
import com.poixson.pxnCommon.dbPool.dbPoolConn;


public abstract class pxnPluginObjects extends JavaPlugin {


	// load/unload plugin
	protected abstract void StartPlugin();
	protected abstract void StopPlugin();

	// plugin name
	public abstract String getPluginName();
	public abstract String getPluginFullName();

	// plugin version
	public abstract String getPluginVersion();
	public abstract String getAvailableVersion();
	public abstract boolean isUpdateAvailable();

	// error messages
	protected abstract void Message_PluginAlreadyRunning();
	protected abstract void Message_PluginLoadingFailed();

	// is ok
//	public abstract boolean isOk();
	public abstract boolean okEquals(Boolean isOk);
	protected abstract void setOk(Boolean isOk);

	// error messages
	public abstract void errorMsg(String msg);
	public abstract String[] errorMsgs();

	// display single lined message
	protected abstract void ConsoleAlert(String alert);
	// display multi-line message
	protected abstract void ConsoleAlert(String[] alerts);


	public pxnPluginObjects() {
		super();
		getLog();
	}


//	@Override
//	public void onEnable() {
//		super.onEnable();
//	}


//	@Override
//	public void onDisable() {
//		super.onDisable();
//	}


	// single instance of plugin
	protected static pxnPlugin SingleInstance(pxnPlugin instanceHolder, pxnPlugin newInstance) {
		if(instanceHolder != null) {
			if(!instanceHolder.equals(newInstance)) {
				newInstance.Message_PluginAlreadyRunning();
				return null;
			}
		}
		instanceHolder = newInstance;
		return newInstance;
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
			log = new pxnLogger(getPluginName());
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
	public void registerListener(Listener listener) {
		if(pm == null)
			pm = getServer().getPluginManager();
		pm.registerEvents(listener, this);
	}


	// database pool
	protected dbPool db = null;
	public dbPool getDBPool() {
		return db;
	}
	public dbPoolConn getConnLock() {
		if(db == null)
			return null;
		return db.getConnLock();
	}


	// language
	protected pxnLanguageMessages language = null;
	public pxnLanguageMessages getLang() {
		if(language == null)
			language = pxnLanguageMessages.factory();
		return language;
	}


	// tasks
	protected HashMap<String, pxnTask> tasks = new HashMap<String, pxnTask>();
	public HashMap<String, pxnTask> getTaskMap() {
		return tasks;
	}


}