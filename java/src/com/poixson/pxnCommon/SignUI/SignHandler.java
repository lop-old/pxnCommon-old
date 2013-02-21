package com.poixson.pxnCommon.SignUI;

import org.bukkit.event.block.SignChangeEvent;


public interface SignHandler {

	// validate sign lines
	public abstract boolean ValidateSign(SignChangeEvent event);

	// sign clicked
	public abstract void onClick();

}