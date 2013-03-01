package com.poixson.pxnCommon.Task;

import java.util.HashMap;

import org.bukkit.scheduler.BukkitTask;

import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;


public abstract class pxnTaskThrottled extends pxnTask {

	protected BukkitTask bukkitTask = null;
	protected static HashMap<String, pxnTask> tasks = new HashMap<String, pxnTask>();
	protected int taskLoopCount = 0;


	public pxnTaskThrottled(pxnPlugin plugin, String taskName) {
		super(plugin, taskName);
	}
	public pxnTaskThrottled(pxnPlugin plugin, String taskName, boolean isThreaded, boolean isLockable) {
		super(plugin, taskName, isThreaded, isLockable);
	}


	@Override
	public pxnTask Start() {
//log.debug(CONTAINER_NAME, "Starting: "+this.taskName);
		return super.Start();
	}

	@Override
	protected void runTask() {
		taskLoopCount = 0;
		runTaskThrottled();
	}
	protected abstract void runTaskThrottled();


	protected void SleepTaskLoop() {

log.warning("task running!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


// sleep thread
//i++; if(i > 2) { i = 0;
//	pxnUtils.Sleep(100);
//}




	}


}