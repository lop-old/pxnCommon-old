package com.poixson.pxnCommon.Listeners;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.poixson.pxnCommon.Logger.pxnLogger;


public class pxnListenerServer implements Listener {

	private static Economy vaultEconomy = null;
	private static Permission vaultPermission = null;

	// logger
	protected final pxnLogger log;


	public pxnListenerServer(pxnLogger log) {
		this.log = log;
	}


	@EventHandler(priority = EventPriority.NORMAL)
	public void onPluginEnable(PluginEnableEvent event) {
		// vault economy
		if(vaultEconomy == null) {
			RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if(economyProvider != null) {
				vaultEconomy = (Economy) economyProvider.getProvider();
				log.info("Economy plugin loaded: "+vaultEconomy.getName());
			}
		}
		if(vaultPermission == null) {
			RegisteredServiceProvider<Permission> permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			if(permissionProvider != null) {
				vaultPermission = (Permission) permissionProvider.getProvider();
				log.info("Permission plugin loaded: "+vaultPermission.getName());
			}
		}
	}


	public static Economy getEconomy() {
		return vaultEconomy;
	}
	public static Permission getPermission() {
		return vaultPermission;
	}


}