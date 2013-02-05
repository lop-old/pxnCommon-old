package com.poixson.pxnCommon;

import com.poixson.pxnCommon.Logger.pxnLogger;


public abstract class pxnTask implements Runnable {

	protected Boolean running = false;
	protected String taskName;
	private pxnLogger log = null;


	public pxnTask(String taskName) {
		if(taskName == null) throw new IllegalArgumentException("taskName can't be null!");
		this.taskName = taskName;
	}


	// get a lock
	protected boolean getLock() {
		if(running) { SkipTaskMessage(); return false; }
		synchronized(running) {
			if(running) { SkipTaskMessage(); return false; }
			running = true;
			return true;
		}
	}
	// release the lock
	protected void releaseLock() {
		running = false;
	}


	// skip task message
	protected void SkipTaskMessage() {
		log.warning("Skipping task - The "+taskName+" task is taking longer to complete than your repeat frequency. Please adjust your config!");
	}


	// logger
	protected pxnLogger getLogger() {
		synchronized(log) {
			if(log == null)
				log = new pxnLogger(taskName);
			return log;
		}
	}
	public void setLogger(pxnLogger log) {
		synchronized(log) {
			this.log = log;
		}
	}


}