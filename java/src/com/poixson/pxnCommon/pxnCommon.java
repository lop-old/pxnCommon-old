package com.poixson.pxnCommon;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.poixson.pxnCommon.Listeners.pxnListenerServer;
import com.poixson.pxnCommon.Logger.pxnLogger;


public class pxnCommon extends JavaPlugin {

	private static pxnCommon instance = null;
	private static pxnLogger log = null;


	// pxnCommon instance
	public pxnCommon() {
	}
	public static pxnCommon getInstance() {
		if(instance == null)
			instance = new pxnCommon();
		return instance;
	}


	public void onLoad() {
	}


	public void onEnable() {
		// register server listener
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new pxnListenerServer(), this);
		// load delayed plugins now
		DelayedLoader.LoadPluginsNow();
		pxnUtils.Sleep(400);
		DelayedLoader.LoadPluginsNow();
	}


	public void onDisable() {
	}


	public static pxnLogger getLog() {
		if(log == null)
			log = new pxnLogger("pxnCommon");
		return log;
	}


	public static boolean hasPermission(CommandSender sender, String permissionNode) {
		if(pxnListenerServer.getPermission() == null)
			return sender.hasPermission(permissionNode);
		else
			return pxnListenerServer.getPermission().has(sender, permissionNode);
	}


}