package com.poixson.pxnCommon;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.poixson.pxnCommon.Logger.pxnLogger;


public class pxnVault {
	private static final String CONTAINER_NAME = "Vault";

	private static Permission vaultPermission = null;
	private static Economy vaultEconomy = null;
	private static Chat vaultChat = null;


	// permissions
	public static synchronized Permission getPermission(pxnLogger log) {
		if(vaultPermission == null) {
			RegisteredServiceProvider<Permission> provider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
			if(provider == null) {
				log.severe(CONTAINER_NAME, "Failed to load a permission plugin!"); return null;}
			vaultPermission = provider.getProvider();
			if(vaultPermission == null) {
				log.severe(CONTAINER_NAME, "Failed to load a permission plugin!"); return null;}
			log.info(CONTAINER_NAME, "Permission plugin loaded: "+vaultPermission.getName());
		}
		return vaultPermission;
	}
	public static boolean PreloadPermissions(pxnLogger log) {
		return (getPermission(log) != null);
	}


	// economy
	public static synchronized Economy getEconomy(pxnLogger log) {
		if(vaultEconomy == null) {
			RegisteredServiceProvider<Economy> provider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
			if(provider == null) {
			log.severe(CONTAINER_NAME, "Failed to load a economy plugin!"); return null;}
			vaultEconomy = provider.getProvider();
			if(vaultEconomy == null) {
				log.severe(CONTAINER_NAME, "Failed to load a economy plugin!"); return null;}
			log.info(CONTAINER_NAME, "Economy plugin loaded: "+vaultEconomy.getName());
		}
		return vaultEconomy;
	}
	public static boolean PreloadEconomy(pxnLogger log) {
		return (getEconomy(log) != null);
	}


	// chat
	public static synchronized Chat getChat(pxnLogger log) {
		if(vaultChat == null) {
			RegisteredServiceProvider<Chat> provider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
			if(provider == null) {
				log.severe(CONTAINER_NAME, "Failed to load a chat plugin!"); return null;}
			vaultChat = provider.getProvider();
			if(vaultChat == null) {
				log.severe(CONTAINER_NAME, "Failed to load a chat plugin!"); return null;}
			log.info(CONTAINER_NAME, "Chat plugin loaded: "+vaultChat.getName());
		}
		return vaultChat;
	}
	public static boolean PreloadChat(pxnLogger log) {
		return (getChat(log) != null);
	}


	// make payment
	public static boolean MakePayment(pxnLogger log, String playerName, Double amount) {
		return MakePayment(log, CONTAINER_NAME, playerName, amount);
	}
	public static boolean MakePayment(pxnLogger log, String container, String playerName, Double amount) {
		if(amount == 0.0)
			return true;
		EconomyResponse response = null;
		// deposit
		if(amount > 0.0)
			response = getEconomy(log).depositPlayer(playerName, amount);
		// withdraw
		else if(amount < 0.0)
			response = getEconomy(log).withdrawPlayer(playerName, amount * -1.0);
		if(response == null || !response.transactionSuccess())
			return false;
		// log action
		if(amount > 0.0)
			log.info(container, "Made payment of "+Double.toString(amount)+" to "+playerName);
		else if(amount < 0.0)
			log.info(container, "Took payment of "+Double.toString(amount * -1.0)+" from "+playerName);
		return true;
	}


}