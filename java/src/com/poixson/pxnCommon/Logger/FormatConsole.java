package com.poixson.pxnCommon.Logger;



public class FormatConsole {

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

	private String prefix;


	public FormatConsole(String prefix) {
		setPrefix(prefix);
	}


	// prefix
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		if(!prefix.startsWith("[")) prefix = "["+prefix+"] ";
		this.prefix = prefix;
	}


	public String sendMsg(String text) {
		return prefix+FormatText(text);
	}
	public String sendMsg(String container, String text) {
		if(!container.startsWith("[")) container = "["+container+"] ";
		return prefix+container+FormatText(text);
	}


	public static String FormatText(String text) {
		if(text == null) text = "<null>";
//		if(text.contains("{") && text.contains("}")) {
//			for(Entry<String, ChatColor> entry : chatColorsMap.entrySet()) {
//				String replaceWhat = entry.getKey();
//				ChatColor withWhat = entry.getValue();
//				text = text.replace(replaceWhat, withWhat.toString());
//			}
//		}
		return text;
	}


}