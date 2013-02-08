package com.poixson.pxnCommon.Logger;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;


public class FormatText implements FormatListener {

	// chat colors
	@SuppressWarnings("serial")
	private static final HashMap<String, ChatColor> chatColorsMap = new HashMap<String, ChatColor>() {{
		put("{black}",         ChatColor.BLACK         );
		put("{darkblue}",      ChatColor.DARK_BLUE     );
		put("{darkgreen}",     ChatColor.DARK_GREEN    );
		put("{darkaqua}",      ChatColor.DARK_AQUA     );
		put("{darkred}",       ChatColor.DARK_RED      );
		put("{darkpurple}",    ChatColor.DARK_PURPLE   );
		put("{gold}",          ChatColor.GOLD          );
		put("{gray}",          ChatColor.GRAY          );
		put("{darkgray}",      ChatColor.DARK_GRAY     );
		put("{blue}",          ChatColor.BLUE          );
		put("{green}",         ChatColor.GREEN         );
		put("{aqua}",          ChatColor.AQUA          );
		put("{red}",           ChatColor.RED           );
		put("{lightpurple}",   ChatColor.LIGHT_PURPLE  );
		put("{yellow}",        ChatColor.YELLOW        );
		put("{white}",         ChatColor.WHITE         );
		put("{magic}",         ChatColor.MAGIC         );
		put("{bold}",          ChatColor.BOLD          );
		put("{strikethrough}", ChatColor.STRIKETHROUGH );
		put("{underline}",     ChatColor.UNDERLINE     );
		put("{italic}",        ChatColor.ITALIC        );
		put("{reset}",         ChatColor.RESET         );
	}};
//	// console colors (requires jline library)
//	private static final HashMap<String, Ansi.Color> consoleColorsMap = new HashMap<String, Ansi.Color>() {{
//		put("{black}",         Ansi.ansi().fg(Ansi.Color.BLACK).boldOff().toString()            );
//		put("{darkblue}",      Ansi.ansi().fg(Ansi.Color.BLUE).boldOff().toString()             );
//		put("{darkgreen}",     Ansi.ansi().fg(Ansi.Color.GREEN).boldOff().toString()            );
//		put("{darkaqua}",      Ansi.ansi().fg(Ansi.Color.CYAN).boldOff().toString()             );
//		put("{darkred}",       Ansi.ansi().fg(Ansi.Color.RED).boldOff().toString()              );
//		put("{darkpurple}",    Ansi.ansi().fg(Ansi.Color.MAGENTA).boldOff().toString()          );
//		put("{gold}",          Ansi.ansi().fg(Ansi.Color.YELLOW).boldOff().toString()           );
//		put("{gray}",          Ansi.ansi().fg(Ansi.Color.WHITE).boldOff().toString()            );
//		put("{darkgray}",      Ansi.ansi().fg(Ansi.Color.BLACK).bold().toString()               );
//		put("{blue}",          Ansi.ansi().fg(Ansi.Color.BLUE).bold().toString()                );
//		put("{green}",         Ansi.ansi().fg(Ansi.Color.GREEN).bold().toString()               );
//		put("{aqua}",          Ansi.ansi().fg(Ansi.Color.CYAN).bold().toString()                );
//		put("{red}",           Ansi.ansi().fg(Ansi.Color.RED).bold().toString()                 );
//		put("{lightpurple}",   Ansi.ansi().fg(Ansi.Color.MAGENTA).bold().toString()             );
//		put("{yellow}",        Ansi.ansi().fg(Ansi.Color.YELLOW).bold().toString()              );
//		put("{white}",         Ansi.ansi().fg(Ansi.Color.WHITE).bold().toString()               );
//		put("{magic}",         Ansi.ansi().a(Attribute.BLINK_SLOW).toString()                   );
//		put("{bold}",          Ansi.ansi().a(Attribute.UNDERLINE_DOUBLE).toString()             );
//		put("{strikethrough}", Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString()             );
//		put("{underline}",     Ansi.ansi().a(Attribute.UNDERLINE).toString()                    );
//		put("{italic}",        Ansi.ansi().a(Attribute.ITALIC).toString()                       );
//		put("{reset}",         Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.DEFAULT).toString() );
//	}};


	public static String formatConsole(String text) {
		if(text == null) text = "<null>";
		if(text.contains("{") && text.contains("}")) {
HashMap<String, ChatColor> consoleColorsMap = chatColorsMap;
			for(Entry<String, ChatColor> entry : consoleColorsMap.entrySet()) {
				String replaceWhat = entry.getKey();
				ChatColor withWhat = entry.getValue();
				text = text.replace(replaceWhat, withWhat.toString());
			}
		}
		return text;
	}


	public static String formatChat(String text) {
		if(text == null) text = "<null>";
		if(text.contains("{") && text.contains("}")) {
			for(Entry<String, ChatColor> entry : chatColorsMap.entrySet()) {
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