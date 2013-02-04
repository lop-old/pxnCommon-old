package com.poixson.pxnCommon;


public class pxnLoader {


	/**
	 * Check for pxnCommon library to be loaded, and delays onEnable() for the
	 * plugin if needed.
	 * 
	 * usage: if(onEnableDelayed) return;
	 * 
	 * @return boolean true If delay is needed; plugin should just return.
	 */
	public boolean onEnableDelayed() {
		if(isLoaded())
			return false;
		// delay loading
		return true;
	}


	public static boolean isLoaded() {
		try {
			Class.forName("com.poixson.pxnCommon.pxnCommon");
			return true;
		} catch (ClassNotFoundException ignore) {}
		return false;
	}


}