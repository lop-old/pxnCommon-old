package com.poixson.pxnCommon.Logger;


public interface FormatListener {

	public String getForConsole();
	public String getForConsole(String text);

	public String getForChat();
	public String getForChat(String text);

}