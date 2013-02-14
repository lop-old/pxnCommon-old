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
			if(!okEquals(null)) return;
			// only one instance allowed
			if(pxnPlugins.containsKey(getPluginName())) {
				Message_PluginAlreadyRunning();
				return;
			}
			pxnPlugins.put(getPluginName(), this);
		}
	}
	// get plugin (by name)
	public static pxnPlugin getPlugin(String pluginName) {
		synchronized(pxnPlugins) {
			if(!pxnPlugins.containsKey(pluginName))
				return null;
			return pxnPlugins.get(pluginName);
		}
	}


	// load plugin
	@Override
	public void onEnable() {
		super.onEnable();
		// stop plugin (if needed)
		if(okEquals(false))
			StopPlugin();
		// start plugin
		StartPlugin();
		// failed to load
		if(!okEquals(true)){
			ConsoleAlert("Failed to load "+getPluginName()+"! Unloading to be safe..");
			onDisable();
			return;
		}
	}


	// unload plugin
	@Override
	public void onDisable() {
		super.onDisable();
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
	protected void Message_PluginAlreadyRunning() {
		errorMsg(getPluginName()+" is already running!!!\nCan't load a second copy of this plugin!!!");
		throw new IllegalStateException();
	}
	// failed to load plugin
	protected void Message_PluginLoadingFailed() {
		log.severe(getPluginName()+" failed to load the plugin!!!");
	}


	// loaded ok
	private Boolean isOk = null;
//	public boolean isOk() {
//		if(isOk == null)
//			return false;
//		return isOk;
//	}
	public boolean okEquals(Boolean isOk) {
		if(isOk == null)
			return (this.isOk == null);
		return isOk.equals(this.isOk);
	}
	// set loaded ok
	protected void setOk(Boolean isOk) {
		this.isOk = isOk;
		if(this.isOk== null)
			super.setEnabled(false);
		else
			super.setEnabled(isOk);
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
		getServer().getConsoleSender().sendMessage(ChatColor.RED+StringUtils.repeat("*", len+8));
		getServer().getConsoleSender().sendMessage(ChatColor.RED+"*** "+alert+" ***");
		getServer().getConsoleSender().sendMessage(ChatColor.RED+StringUtils.repeat("*", len+8));
	}
	// display multi-line message
	protected void ConsoleAlert(String[] alerts) {
		int len = 0;
		// find max length
		for(String alert : alerts)
			if(alert.length() > len)
				len = alert.length();
		// display message
		getServer().getConsoleSender().sendMessage(ChatColor.RED+StringUtils.repeat("*", len+8));
		for(String alert : alerts)
			getServer().getConsoleSender().sendMessage(ChatColor.RED+"*** "+alert+StringUtils.repeat(" ", len-alert.length() )+" ***");
		getServer().getConsoleSender().sendMessage(ChatColor.RED+StringUtils.repeat("*", len+8));
	}


}