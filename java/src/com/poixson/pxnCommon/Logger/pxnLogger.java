package com.poixson.pxnCommon.Logger;

import java.util.logging.Logger;


public class pxnLogger {

	protected static final Logger sender = Logger.getLogger("Minecraft");
	protected FormatConsole console;


	public pxnLogger(String prefix) {
		if(prefix == null) throw new NullPointerException("prefix can't be null!");
		console = new FormatConsole(prefix);
	}
	public static pxnLogger clone(pxnLogger logCloning) {
		if(logCloning == null) throw new NullPointerException("logCloning can't be null!");
		pxnLogger newLog = new pxnLogger(logCloning.console.getPrefix());
		return newLog;
	}


	/**
	 * @param msg
	 */
	public void debug(String msg) {
		sender.info(console.sendMsg(msg));
	}
	/**
	 * @param container
	 * @param msg
	 */
	public void debug(String container, String msg) {
		sender.info(console.sendMsg(container, msg));
	}


	/**
	 * @param msg
	 */
	public void info(String msg) {
		sender.info(console.sendMsg(msg));
	}
	/**
	 * @param container
	 * @param msg
	 */
	public void info(String container, String msg) {
		sender.info(console.sendMsg(container, msg));
	}


	/**
	 * @param msg
	 */
	public void warning(String msg) {
		sender.warning(console.sendMsg(msg));
	}
	/**
	 * @param container
	 * @param msg
	 */
	public void warning(String container, String msg) {
		sender.warning(console.sendMsg(container, msg));
	}


	/**
	 * @param msg
	 */
	public void severe(String msg) {
		sender.severe(console.sendMsg(msg));
	}
	/**
	 * @param container
	 * @param msg
	 */
	public void severe(String container, String msg) {
		sender.severe(console.sendMsg(container, msg));
	}


	/**
	 * Sends an exception to the logger.
	 * @param e Exception
	 */
	public void exception(Exception e) {
		if(e == null) e = new NullPointerException("Null exception passed to logger! This isn't normal!");
		severe(e.getMessage());
		e.printStackTrace();
	}


}