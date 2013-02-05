package com.poixson.pxnCommon.Logger;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;


public class FormatText implements FormatListener {

	// chat colors
	@SuppressWarnings("serial")
	private static final HashMap<String, ChatColor> colorsMap = new HashMap<String, ChatColor>() {{
		put("{black}",         ChatColor.BLACK);
		put("{darkblue}",      ChatColor.DARK_BLUE);
		put("{darkgreen}",     ChatColor.DARK_GREEN);
		put("{darkaqua}",      ChatColor.DARK_AQUA);
		put("{darkred}",       ChatColor.DARK_RED);
		put("{darkpurple}",    ChatColor.DARK_PURPLE);
		put("{gold}",          ChatColor.GOLD);
		put("{gray}",          ChatColor.GRAY);
		put("{darkgray}",      ChatColor.DARK_GRAY);
		put("{blue}",          ChatColor.BLUE);
		put("{green}",         ChatColor.GREEN);
		put("{aqua}",          ChatColor.AQUA);
		put("{red}",           ChatColor.RED);
		put("{lightpurple}",   ChatColor.LIGHT_PURPLE);
		put("{yellow}",        ChatColor.YELLOW);
		put("{white}",         ChatColor.WHITE);
		put("{magic}",         ChatColor.MAGIC);
		put("{bold}",          ChatColor.BOLD);
		put("{strikethrough}", ChatColor.STRIKETHROUGH);
		put("{underline}",     ChatColor.UNDERLINE);
		put("{italic}",        ChatColor.ITALIC);
		put("{reset}",         ChatColor.RESET);
	}};


	public static String formatConsole(String text) {
//TODO: is this different?
return formatChat(text);
	}


	public static String formatChat(String text) {
		if(text.contains("{") && text.contains("}")) {
			 for(Entry<String, ChatColor> entry : colorsMap.entrySet()) {
			 	String replaceWhat = entry.getKey();
			 	ChatColor withWhat = entry.getValue();
			 	text = text.replace(replaceWhat, withWhat.toString());
			 }
		}
		return text;
	}


	// class instance
	protected String text;
	public FormatText(String text) {
		this.text = text;
	}
	public FormatText() {
	}


	@Override
	public String getForConsole() {
		return formatConsole(text);
	}
	@Override
	public String getForConsole(String text) {
		return formatConsole(text);
	}


	@Override
	public String getForChat() {
		return formatChat(text);
	}
	@Override
	public String getForChat(String text) {
		return formatChat(text);
	}


}