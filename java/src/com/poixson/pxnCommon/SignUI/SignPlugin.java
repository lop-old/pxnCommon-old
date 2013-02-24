package com.poixson.pxnCommon.SignUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;


public abstract class SignPlugin extends SignFunctions {

	// validate sign lines
	public abstract boolean ValidateSignFirst(SignChangeEvent event);
	public abstract void InvalidSign(SignChangeEvent event);

	// sign types
	protected final List<SignType> signTypes = new ArrayList<SignType>();


	// add sign handler
	public void addSign(SignType signType) {
		if(signType == null) throw new NullPointerException("signType can't be null!");
		signTypes.add(signType);
	}


	// validate sign lines
	@Override
	public String onCreateSign(SignChangeEvent event) {
		// plugin handles this sign
		if(!this.ValidateSignFirst(event))
			return null;
		String type = null;
		for(SignType sign : signTypes) {
			type = sign.onCreateSign(event);
			if(type != null)
				return type;
		}
		// invalid sign
		InvalidSign(event);
		return null;
	}


	// sign clicked
	@Override
	public void onClick(PlayerInteractEvent event, SignDAO sign) {
		if(event == null) throw new NullPointerException("event can't be null!");
		for(SignType type : signTypes)
			if(type.typeEquals(sign))
				type.onClick(event, sign);
	}


}