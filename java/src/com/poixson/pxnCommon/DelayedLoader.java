package com.poixson.pxnCommon;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;


public class DelayedLoader {

	// plugins to load later
	private static List<JavaPlugin> plugins = new ArrayList<JavaPlugin>();


	// add plugin to enable later
	public static void addPlugin(JavaPlugin plugin) {
		if(plugin == null) throw new NullPointerException("plugin can't be null!");
		synchronized(plugins) {
			plugins.add(plugin);
		}
	}


	// enable delayed plugins now
	public static void LoadPluginsNow() {
		synchronized(plugins) {
			for(JavaPlugin plugin : plugins)
				plugin.onEnable();
			plugins.clear();
		}
	}


}