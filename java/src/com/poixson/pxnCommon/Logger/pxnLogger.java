package com.poixson.pxnCommon.Logger;

import java.util.logging.Logger;


public class pxnLogger {

	public static final Logger log = Logger.getLogger("minecraft");

	private final String pluginName;


	public pxnLogger(String pluginName) {
		this.pluginName = pluginName;
	}
	public static pxnLogger clone(pxnLogger logCloning) {
		pxnLogger newLog = new pxnLogger(logCloning.pluginName);
		return newLog;
	}


	public void info(String msg) {
		log.info("["+pluginName+"] "+msg);
	}
	public void info(String container, String msg) {
		info("["+container+"] "+msg);
	}


}