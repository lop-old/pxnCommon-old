package com.poixson.pxnCommon.SignUI;

import org.bukkit.GameMode;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.poixson.pxnCommon.pxnUtils;


public abstract class SignFunctions {

	// sign clicked
	public abstract void onClick(PlayerInteractEvent event, SignDAO sign);
	// create sign
	public abstract String onCreateSign(SignChangeEvent event);


	// set sign line
	protected void setLine(int lineNumber, String line, SignChangeEvent event) {
		event.setLine(lineNumber, pxnUtils.ReplaceColors(line));
	}
	// clear unused lines
	protected void ClearSignAfter(int lineNumber, SignChangeEvent event) {
		for(int i=lineNumber+1; i<=3; i++)
			event.setLine(i, "");
	}


	// validate sign line
	public static boolean ValidLine(SignChangeEvent event, int lineNumber, String setLine, String setLineAliases[]) {
		String line = event.getLine(lineNumber);
		if(line == null)
			return false;
		// already good
		if(line.equalsIgnoreCase(setLine))
			return true;
		// line aliases
		for(String l : setLineAliases) {
			if(line.equalsIgnoreCase(l)) {
				event.setLine(lineNumber, pxnUtils.ReplaceColors(setLine));
				return true;
			}
		}
		// line not found
		return false;
	}


	// break sign
	protected void CancelSign(SignChangeEvent event) {
		event.setCancelled(true);
		if(event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			event.getBlock().setTypeId(0);
		else
			event.getBlock().breakNaturally();
	}


}