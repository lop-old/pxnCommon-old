package com.poixson.pxnCommon;

import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;


public class pxnCommon extends pxnPlugin {

	private static pxnCommon instance = null;


	// pxnCommon instance
	public pxnCommon() {
		super();
		// only one instance allowed
		instance = (pxnCommon) SingleInstance(instance, this);
	}
	// plugin name
	@Override
	public String getPluginName() {
		return "pxnCommon";
	}


	// load plugin
	@Override
	protected void StartPlugin() {
		// already loaded
		if(okEquals(true)) {
			log.info(getPluginName()+" already loaded");
			return;
		}
		// starting plugin
		getLog().info("Starting library..");

		setOk(true);
	}


	// unload plugin
	@Override
	protected void StopPlugin() {
	}


//	public static boolean hasPermission(CommandSender sender, String permissionNode) {
//		if(pxnListenerServer.getPermission() == null)
//			return sender.hasPermission(permissionNode);
//		else
//			return pxnListenerServer.getPermission().has(sender, permissionNode);
//	}


}