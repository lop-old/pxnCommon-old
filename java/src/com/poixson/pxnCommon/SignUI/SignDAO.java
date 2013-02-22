package com.poixson.pxnCommon.SignUI;

import org.bukkit.Location;
import org.bukkit.block.Block;


public class SignDAO {

	protected final int id;
	protected final String location;
	protected String line1 = null;
	protected String line2 = null;
	protected String line3 = null;
	protected String line4 = null;
	protected String owner = null;


	protected SignDAO(int id, String location) {
		this.id = id;
		this.location = location;
	}


	// format location string
	protected static String BlockLocationToString(Block block) {
		if(block == null) throw new NullPointerException("sign can't be null!");
		Location loc = block.getLocation();
		return LocationToString(
			loc.getWorld().getName(),
			loc.getBlockX(),
			loc.getBlockY(),
			loc.getBlockZ()
		);
	}
	protected static String LocationToString(String world, int x, int y, int z) {
		return
			world+":"+
			Integer.toString(x)+":"+
			Integer.toString(y)+":"+
			Integer.toString(z);
	}


	public void setLine(int lineNumber, String line) {
		if(lineNumber == 1)
			this.line1 = line;
		else
		if(lineNumber == 2)
			this.line2 = line;
		else
		if(lineNumber == 3)
			this.line3 = line;
		else
		if(lineNumber == 4)
			this.line4 = line;
		else
			throw new IllegalArgumentException("Line number "+Integer.toString(lineNumber)+" is out of bounds! 1-4 only.");
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}


}