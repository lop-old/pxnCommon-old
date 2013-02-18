package com.poixson.pxnCommon.pxnLinks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.poixson.pxnCommon.pxnVault;
import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;
import com.poixson.pxnCommon.Task.pxnTaskThrottled;
import com.poixson.pxnCommon.dbPool.dbPool;
import com.poixson.pxnCommon.dbPool.dbPoolConn;


public class pxnVaultLink extends pxnTaskThrottled {
	private static final String CONTAINER_NAME = "Vault Link";

	// instances
	private static List<pxnVaultLink> links = new ArrayList<pxnVaultLink>();

	// database
	private dbPool pool;
	private Economy economy;

	// run count
	private int runCountAdjust = 0;
	// balances cache
	private HashMap<String, Double> balCache = new HashMap<String, Double>();


	public static pxnVaultLink factory(pxnPlugin plugin) {
		synchronized(links) {
			pxnVaultLink vaultLink = null;
			// check existing link db's
			for(pxnVaultLink link : links) {
				//if(link.pool == null) continue;
				if(link.pool.equals( plugin.getDBPool() )) {
					vaultLink = link;
					break;
				}
			}
			if(vaultLink == null) {
				vaultLink = new pxnVaultLink(plugin);
				links.add(vaultLink);
			}
			return vaultLink;
		}
	}


	private pxnVaultLink(pxnPlugin plugin) {
		super(plugin, "Update Players");
		this.pool = plugin.getDBPool();
		this.economy = pxnVault.getEconomy(log);
//TODO:
setDelay(1);
setPeriod(20);
Start();
//set task interval
//set task sleep
		log.info(CONTAINER_NAME, "Loaded VaultLink");
	}


	@Override
	protected void runTaskThrottled() {
//System.out.println("running task");
		try {
			// process adjustments queue
			Update_BalanceAdjust();
		} catch (Exception e) {
			log.exception(e);
		}
		try {
			// update online player balances
			Update_PlayersOnline();
		} catch (Exception e) {
			log.exception(e);
		}
	}


	// process adjustments queue
	private void Update_BalanceAdjust() {
		Update_BalanceAdjust(false);
	}
	private void Update_BalanceAdjust(boolean processHolds) {
		String CONTAINER_NAME = "Balance Adjust";
		runCountAdjust++;
		if(runCountAdjust >= 10) {
			runCountAdjust = 0;
			processHolds = true;
		}
		synchronized(balCache) {
			dbPoolConn dbLoop = pool.getConnLock();
			dbPoolConn db     = pool.getConnLock();
			// query adjustments to make
			dbLoop.Prepare("SELECT "+
					"`adjust_id`, `username`, `amount` "+
					"FROM `PSM_BalanceAdjust` "+
					"WHERE `hold` = ? "+
					"ORDER BY `adjust_id` ASC "+
					"LIMIT 100");
			dbLoop.setBoolean(1, processHolds);
			dbLoop.Exec();
			int count = 0;
			// loop adjustments
			while(dbLoop.hasNext()) {
				int adjustId      = dbLoop.getInt("adjust_id");
				String playerName = dbLoop.getString("username");
				Double amount     = dbLoop.getDouble("amount");
				// make adjustment
				log.debug(CONTAINER_NAME, playerName+" "+Double.toString(amount) );
				if(pxnVault.MakePayment(log, CONTAINER_NAME, playerName, amount)) {
					// remove adjustment
					db.Prepare("DELETE FROM `PSM_BalanceAdjust` "+
						"WHERE `adjust_id` = ? "+
						"LIMIT 1");
					db.setInt(1, adjustId);
					db.Exec();
					count++;
				} else {
					// put on hold
					log.warning(CONTAINER_NAME, "Player "+playerName+" doesn't have enough money to cover "+
						Double.toString(amount)+" ! This is being put on hold for now.");
					db.Prepare("UPDATE `PSM_BalanceAdjust` SET "+
						"`hold` = 1 "+
						"WHERE `adjust_id` = ? "+
						"LIMIT 1");
					db.setInt(1, adjustId);
					db.Exec();
				}
				SleepTaskLoop();
			}
			// done
			db.releaseLock();
			dbLoop.releaseLock();
			if(count > 0)
				log.info(CONTAINER_NAME, "Updated [ "+Integer.toString(count)+" ] player balance"+(count>1 ? "s" : ""));
		}
	}


	// update online player balances
	private void Update_PlayersOnline() {
		String CONTAINER_NAME = "Update Balances";
		synchronized(balCache) {
			dbPoolConn dbLoop = pool.getConnLock();
			// loop online players
			List<Player> playersOnline = new ArrayList<Player>(Arrays.asList(Bukkit.getOnlinePlayers()));
			for(Player player : playersOnline) {
				if(!player.isOnline()) continue;
				// update a players balance
				String playerName = player.getName();
				double newBal = economy.getBalance(playerName);
				double oldBal = getBalance(playerName);
				// update balance
				if(oldBal != newBal) {
					log.info(CONTAINER_NAME, "Updating balance for player "+playerName+" to "+Double.toString(newBal));
					setBalance(playerName, newBal);
				}
				// sleep thread
				SleepTaskLoop();
			}
			// remove offline players from cache
			Iterator<String> it = balCache.keySet().iterator();
			while(it.hasNext()) {
				if(playersOnline.contains( it.next() ))
					it.remove();
			}
			// done
			dbLoop.releaseLock();
		}
	}


	// query database for balance
	private double getBalance(String playerName) {
		// cached balance
		if(balCache.containsKey(playerName))
			return balCache.get(playerName);
		// query for balance
		dbPoolConn db = pool.getConnLock();
		db.Prepare("SELECT `balance` FROM `PSM_Users` WHERE `username` = ? LIMIT 1");
		db.setString(1, playerName);
		db.Exec();
		if(db.hasNext()) {
			double bal = db.getDouble("balance");
			db.releaseLock();
			return bal;
		}
		db.releaseLock();
		// fall back to vault
		return economy.getBalance(playerName);
	}


	// set balance in database
	private void setBalance(String playerName, double bal) {
		dbPoolConn db = pool.getConnLock();
		db.Prepare("UPDATE `PSM_Users` SET `balance` = ? WHERE `username` = ? LIMIT 1");
		db.setDouble(1, bal);
		db.setString(2, playerName);
		db.Exec();
		db.releaseLock();
	}


//	// update a players balance
//	private void Update_PlayerBalance2(Player player) {
//		Economy economy = getEconomy();
//		String playerName = player.getName();
//		double newBalance = economy.getBalance(playerName);
//		double oldBalance = 0.0;
//		if(playerBalances.containsKey(playerName))
//			oldBalance = playerBalances.get(playerName);
//		else
//			oldBalance = _getPlayerBalance(playerName);
//		// no update needed
//		if(newBalance == oldBalance)
//			return;
//System.out.println("new balance for "+playerName+" old:"+Double.toString(oldBalance)+" new:"+newBalance );
////table: PSM_Users
//		_setPlayerBalance(playerName, newBalance);
//	}


//	private double _getPlayerBalanceCached(String playerName) {
//		if(playerBalances.containsKey(playerName))
//			return playerBalances.get(playerName);
//		return 0.0;
//	}


//	private double _getPlayerBalance(String playerName) {
//		db.Prepare("SELECT `balance` FROM `PSM_Users` WHERE `username` = ? LIMIT 1")
//			.setString(1, playerName)
//			.Exec();
//		if(!db.Next())
//return 0.0;
//		playerBalances.put(playerName, db.getDouble("balance"));
//		return db.getDouble("balance");
//	}
//	private boolean _setPlayerBalance(String playerName, double balance) {
//		playerBalances.put(playerName, balance);
//		db.Prepare("UPDATE `PSM_Users` SET `balance` = ? WHERE `username` = ? LIMIT 1")
//			.setDouble(1, balance)
//			.setString(2, playerName)
//			.Exec();
//return true;
//	}


//	// database
//	protected dbPoolConn getDB() {
//		return pool.getConnLock();
//	}
//	// economy
//	protected Economy getEconomy() {
//		return pxnVault.getEconomy(plugin.getLog());
//	}


}