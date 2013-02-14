package com.poixson.pxnCommon;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.poixson.pxnCommon.BukkitPlugin.pxnPlugin;
import com.poixson.pxnCommon.Logger.pxnLogger;


public class pxnVault implements Listener {

	private static Permission vaultPermission = null;
	private static Economy vaultEconomy = null;
	private static Chat vaultChat = null;

	// logger
	protected static pxnLogger log;


	public pxnVault(pxnPlugin plugin) {
		pxnVault.log = plugin.getLog();
	}


	// permissions
	public static Permission getPermission() {
		synchronized(vaultPermission) {
			if(vaultPermission == null) {
				RegisteredServiceProvider<Permission> provider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
				if(provider == null) {
					log.severe("Failed to load a permission plugin!"); return null;}
				vaultPermission = provider.getProvider();
				if(vaultPermission == null) {
					log.severe("Failed to load a permission plugin!"); return null;}
				log.info("Permission plugin loaded: "+vaultPermission.getName());
			}
		}
		return vaultPermission;
	}


	// economy
	public static Economy getEconomy() {
		synchronized(vaultEconomy) {
			if(vaultEconomy == null) {
				RegisteredServiceProvider<Economy> provider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
				if(provider == null) {
					log.severe("Failed to load a economy plugin!"); return null;}
				vaultEconomy = provider.getProvider();
				if(vaultEconomy == null) {
					log.severe("Failed to load a economy plugin!"); return null;}
				log.info("Economy plugin loaded: "+vaultEconomy.getName());
			}
		}
		return vaultEconomy;
	}


	// chat
	public static Chat getChat() {
		synchronized(vaultChat) {
			if(vaultChat == null) {
				RegisteredServiceProvider<Chat> provider = Bukkit.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
				if(provider == null) {
					log.severe("Failed to load a chat plugin!"); return null;}
				vaultChat = provider.getProvider();
				if(vaultChat == null) {
					log.severe("Failed to load a chat plugin!"); return null;}
				log.info("Chat plugin loaded: "+vaultChat.getName());
			}
		}
		return vaultChat;
	}


}