package com.poixson.pxnCommon.Metrics;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;


public class pxnMetrics extends com.poixson.pxnCommon.Metrics.Hidendra.Metrics {

	protected final Logger log;

	/**
	 * The name of the plugin. Leave this null to set automatically.
	 */
	protected String pluginName = null;


	public pxnMetrics(Plugin plugin) throws IOException {
		super(plugin);
		log = Logger.getLogger("Minecraft");
	}
	public pxnMetrics(Plugin plugin, String pluginName) throws IOException {
		this(plugin);
		this.pluginName = pluginName;
	}


	@Override
	protected String getBASE_URL() {
		return "http://metrics.poixson.com";
	}


	// logger
	@Override
	protected void logInfo(String msg) {
		log.info("["+pluginName+"] [Metrics] "+msg);
	}


}
