package com.poixson.pxnCommon.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class pxnLogger {

	public static final Logger log = Logger.getLogger("minecraft");

	private final String pluginName;


	public pxnLogger(String pluginName) {
		this.pluginName = pluginName;
		formatListeners.add(new FormatText());
	}
	public static pxnLogger clone(pxnLogger logCloning) {
		pxnLogger newLog = new pxnLogger(logCloning.pluginName);
		return newLog;
	}


	/**
	 * @param msg
	 */
	public void debug(String msg) {
		log.info("["+pluginName+"] "+runFormatListeners(msg));
	}
	/**
	 * @param container
	 * @param msg
	 */
	public void debug(String container, String msg) {
		debug("["+container+"] "+runFormatListeners(msg));
	}


	/**
	 * @param msg
	 */
	public void info(String msg) {
		log.info("["+pluginName+"] "+runFormatListeners(msg));
	}
	/**
	 * @param container
	 * @param msg
	 */
	public void info(String container, String msg) {
		info("["+container+"] "+runFormatListeners(msg));
	}


	/**
	 * @param msg
	 */
	public void warning(String msg) {
		log.warning("["+pluginName+"] "+runFormatListeners(msg));
	}
	/**
	 * @param container
	 * @param msg
	 */
	public void warning(String container, String msg) {
		warning("["+container+"] "+runFormatListeners(msg));
	}


	/**
	 * @param msg
	 */
	public void severe(String msg) {
		log.severe("["+pluginName+"] "+runFormatListeners(msg));
	}
	/**
	 * @param container
	 * @param msg
	 */
	public void severe(String container, String msg) {
		severe("["+container+"] "+runFormatListeners(msg));
	}


	/**
	 * Sends an exception to the logger.
	 * @param e Exception
	 */
	public void exception(Exception e) {
		severe(e.getMessage());
		e.printStackTrace();
	}


	// format listeners
	protected List<FormatListener> formatListeners = new ArrayList<FormatListener>();
	protected String runFormatListeners(String text) {
		for(FormatListener listener : formatListeners)
			text = listener.getForConsole(text);
		return text;
	}


}