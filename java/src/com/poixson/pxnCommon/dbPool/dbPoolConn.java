package com.poixson.pxnCommon.dbPool;

import com.poixson.pxnCommon.pxnUtils;


public class dbPoolConn {

	private long lockTime = -1;


	public dbPoolConn() {
	}


	/**
	 * Get the time connection has been locked for
	 *
	 * @return time in milliseconds
	 */
	public long getLockTime() {
		if(lockTime < 1)
			return -1;
		return pxnUtils.getCurrentMillis() - lockTime;
	}


}