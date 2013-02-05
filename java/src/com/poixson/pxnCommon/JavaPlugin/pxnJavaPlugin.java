package com.poixson.pxnCommon.JavaPlugin;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.pxnCommon.Logger.pxnLogger;


public abstract class pxnJavaPlugin extends JavaPlugin {

	// plugin state
	protected boolean isOk    = false;
	protected boolean isDebug = false;

	// plugin name
	public abstract String getPluginName();
	public abstract String getPluginFullName();

	// logger
	public abstract pxnLogger getLog();

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


	// is plugin is loaded
	public boolean isOk() {
		return isOk;
	}
	// is debug mode
	public boolean isDebug() {
		return isDebug;
	}


	protected void PluginAlreadyRunningMessage() {
		getServer().getConsoleSender().sendMessage(ChatColor.RED+"********************************************");
		getServer().getConsoleSender().sendMessage(ChatColor.RED+"*** WebAuctionPlus is already running!!! ***");
		getServer().getConsoleSender().sendMessage(ChatColor.RED+"********************************************");
		getLog().severe("Plugin is already loaded!! Can't load a second copy of this plugin.");
	}


}