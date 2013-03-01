package com.poixson.pxnCommon.SignUI;

import org.bukkit.Location;
import org.bukkit.block.Block;


public class SignDAO {

	protected final int id;
	protected final String location;
	protected final String type;
	protected String line1 = null;
	protected String line2 = null;
	protected String line3 = null;
	protected String line4 = null;
	protected String owner = null;


	protected SignDAO(int id, String location, String type) {
		this.id = id;
		this.location = location;
		this.type = type;
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


	protected void setLine(int lineNumber, String line) {
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


	protected String getType() {
		return type;
	}


	// owner
	protected String getOwner() {
		return owner;
	}
	protected void setOwner(String owner) {
		this.owner = owner;
	}


	// location equals
	public boolean locationEquals(String location) {
		if(location == null)
			return false;
		return location.equals(this.location);
	}


}