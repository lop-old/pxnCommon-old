package com.poixson.pxnCommon.Task;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;
import com.poixson.pxnCommon.Logger.pxnLogger;


public abstract class pxnTask implements Runnable {

	// plugin
	protected final pxnPlugin plugin;
	// logger
	protected final pxnLogger log;
	// tasks map
	protected HashMap<String, pxnTask> tasks;
	// bukkit scheduler
	protected final static BukkitScheduler bukkitScheduler = Bukkit.getServer().getScheduler();
	protected BukkitTask bukkitTask = null;

	// state booleans
	protected boolean isActive   = false;
	protected int     isRunning  = 0;
	protected boolean isThreaded = false;
	protected boolean isLockable = false;
	protected boolean isLocked   = false;

	protected String taskName;
	protected long delay  = -1;
	protected long period = -1;
	protected int taskRunCount = 0;

	protected final Object lock = new Object();


	// new scheduled task
	public pxnTask(pxnPlugin plugin, String taskName) {
		this(plugin, taskName, null, null);
	}
	public pxnTask(pxnPlugin plugin, String taskName, Boolean isThreaded, Boolean isLockable) {
		if(taskName == null) throw new NullPointerException("taskName can't be null!");
		if(plugin   == null) throw new NullPointerException("plugin can't be null!");
		this.plugin = plugin;
		this.log = plugin.getLog();
		this.tasks = plugin.getTaskMap();
		if(this.tasks == null)
			this.tasks = new HashMap<String, pxnTask>();
		if(isThreaded != null)
			this.isThreaded = isThreaded;
		if(isLockable != null)
			this.isLockable = isLockable;
		synchronized(lock) {
			synchronized(this.tasks) {
				if(this.tasks.containsKey(taskName)) {
					// find a unique name
					int i = 1;
					while(true) {
						i++;
						if(!this.tasks.containsKey( taskName+Integer.toString(i) )) {
							taskName = taskName+Integer.toString(i);
							break;
						}
					}
				}
				this.taskName = taskName;
				this.tasks.put(taskName, this);
			}
		}
	}


	// start scheduled task
	public pxnTask Start() {
		if(bukkitTask != null) {
			log.severe("This task has already been started! "+taskName);
			return null;
		}
		if(delay < 1) throw new IllegalArgumentException("delay must be set! task: "+taskName);
		synchronized(lock) {
			log.info("Starting scheduled tasks..");
			isActive = true;
			bukkitTask = newScheduler(plugin, isActive, this, delay, period);
		}
		return this;
	}
	// stop scheduled task
	public void Stop() {
		synchronized(lock) {
			bukkitTask.cancel();
			isActive = false;
		}
	}
	// stop all tasks
	public static void StopAll(pxnPlugin plugin) {
		HashMap<String, pxnTask> tasks = plugin.getTaskMap();
		synchronized(tasks) {
			plugin.getLog().info("Stopping task scheduler.. [ "+Integer.toString(tasks.size())+" ]");
			// stop scheduler
			bukkitScheduler.cancelTasks(plugin);
			// stop tasks
			for(pxnTask task : tasks.values())
				task.Stop();
			tasks.clear();
		}
	}


	@Override
	public void run() {
		if(isLockable && isRunning > 0) {
			SkipTaskMessage(); return;}
		synchronized(lock) {
			if(isLockable && isRunning > 0) {
				SkipTaskMessage(); return;}
			isRunning++;
			taskRunCount++;
			runTask();
			isRunning--;
		}
	}
	protected abstract void runTask();


	public static BukkitTask newSchedulerOnce(JavaPlugin plugin, boolean isThreaded, Runnable runnable, long delay) {
		return newScheduler(plugin, isThreaded, runnable, delay, -1);
	}
	public static BukkitTask newScheduler(JavaPlugin plugin, boolean isThreaded, Runnable runnable, long delay, long period) {
		if(delay < 1) throw new IllegalArgumentException("delay must be set!");
		// run once
		if(period < 1) {
			// threaded once
			if(isThreaded)
				return bukkitScheduler.runTaskLaterAsynchronously(
					plugin,
					runnable,
					delay
				);
			// blocking once
			else
				return bukkitScheduler.runTaskLater(
					plugin,
					runnable,
					delay
				);
		// run repeated
		} else {
			// threaded repeated
			if(isThreaded)
				return bukkitScheduler.runTaskTimerAsynchronously(
					plugin,
					runnable,
					delay,
					period
				);
			// blocking repeated
			else
				return bukkitScheduler.runTaskTimer(
					plugin,
					runnable,
					delay,
					period
				);
		}
	}


	// run once / repeated
	public pxnTask setRunRepeated() {
		setRunOnce(false);
		return this;
	}
	public pxnTask setRunOnce() {
		setRunOnce(true);
		return this;
	}
	public pxnTask setRunOnce(boolean runOnce) {
		if(runOnce)
			this.period = -1;
		else
			this.period = 20;
		return this;
	}

	// threaded / blocking
	public pxnTask setBlocking() {
		setThreaded(false);
		return this;
	}
	public pxnTask setThreaded() {
		setThreaded(true);
		return this;
	}
	public pxnTask setThreaded(boolean isThreaded) {
		this.isThreaded = isThreaded;
		return this;
	}

	// lockable
	public pxnTask setLockable() {
		setLockable(true);
		return this;
	}
	public pxnTask setLockable(boolean isLockable) {
		this.isLockable = isLockable;
		return this;
	}

	// delay ms
	public pxnTask setDelay(long delay) {
		this.delay = delay;
		return this;
	}
	public pxnTask setPeriod(long period) {
		this.period = period;
		return this;
	}


	// skip task message
	protected void SkipTaskMessage() {
		log.warning("Skipping task - The "+taskName+" task is taking longer to complete than your repeat frequency. Please adjust your config!");
	}


}