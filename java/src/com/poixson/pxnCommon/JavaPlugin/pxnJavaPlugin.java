package com.poixson.pxnCommon.JavaPlugin;

import org.bukkit.plugin.java.JavaPlugin;


public abstract class pxnJavaPlugin extends JavaPlugin {

	// plugin state
	protected boolean isOk    = false;
	protected boolean isDebug = false;

	// plugin name
	public abstract String getPluginName();
	public abstract String getPluginFullName();


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


}