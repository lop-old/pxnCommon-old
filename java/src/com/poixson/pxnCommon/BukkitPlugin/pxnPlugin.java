package com.poixson.pxnCommon.BukkitPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;


public abstract class pxnPlugin extends pxnPluginObjects {


	// plugin instance
	protected static HashMap<String, pxnPlugin> pxnPlugins = new HashMap<String, pxnPlugin>();
	public pxnPlugin() {
		super();
		synchronized(pxnPlugins) {
			if(!isOk()) return;
			// only one instance allowed
			if(pxnPlugins.containsKey(getPluginName())) {
				PluginAlreadyRunningMessage();
				return;
			}
			pxnPlugins.put(getPluginName(), this);
		}
	}
	public static pxnPlugin getPlugin(String pluginName) {
		return pxnPlugins.get(pluginName);
	}


//	@Override
//	public void onEnable() {
//	}


//	@Override
//	public void onDisable() {
//	}


	// plugin name
	public String getPluginName() {
		return getDescription().getName();
	}
	// plugin name & version
	public String getPluginFullName() {
		return getPluginName()+" "+getPluginVersion();
	}


	// plugin version
	public String getPluginVersion() {
		return getDescription().getVersion();
	}
	// available version
	protected String availableVersion = null;
	public String getAvailableVersion() {
//TODO:
availableVersion = "1.0";
		return availableVersion;
	}
	public boolean isUpdateAvailable() {
		String available = getAvailableVersion();
		if(available == null) return false;
//TODO: compare versions
		return false;
	}


	// severe error messages
	protected void PluginAlreadyRunningMessage() {
		errorMsg("WebAuctionPlus is already running!!!\nCan't load a second copy of this plugin!!!");
	}


	// is ok
	protected Boolean isOk = null;
	public boolean isOk() {
		if(isOk == null)
			return false;
		return isOk;
	}
	protected void setOk() {
		isOk = true;
	}
	protected void setNotOk() {
		isOk = false;
	}


	// error messages
	protected List<String> errorMsgs = new ArrayList<String>();
	public void errorMsg(String msg) {
		isOk = false;
		synchronized(errorMsgs) {
			errorMsgs.add(msg);
		}
		ConsoleAlert(msg);
	}
	public String[] errorMsgs() {
		synchronized(errorMsgs) {
			return (String[]) errorMsgs.toArray();
		}
	}


	// display single lined message
	protected void ConsoleAlert(String alert) {
		// multi-line message
		if(alert.contains("\n"))
			ConsoleAlert(alert.split("\n"));
		// single lined message
		int len = alert.length();
		getServer().getConsoleSender().sendMessage(ChatColor.RED+StringUtils.repeat("8", len + 8));
		getServer().getConsoleSender().sendMessage(ChatColor.RED+"*** "+alert+" ***");
		getServer().getConsoleSender().sendMessage(ChatColor.RED+StringUtils.repeat("8", len + 8));
	}
	// display multi-line message
	protected void ConsoleAlert(String[] alerts) {
		int len = 0;
		// find max length
		for(String alert : alerts)
			if(alert.length() > len)
				len = alert.length();
		// display message
		getServer().getConsoleSender().sendMessage(ChatColor.RED+StringUtils.repeat("8", len + 8));
		for(String alert : alerts)
			getServer().getConsoleSender().sendMessage(ChatColor.RED+"*** "+alert+StringUtils.repeat(" ", len-alert.length() )+" ***");
		getServer().getConsoleSender().sendMessage(ChatColor.RED+StringUtils.repeat("8", len + 8));
	}


}