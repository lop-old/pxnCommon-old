package com.poixson.pxnCommon.SignUI;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.event.block.SignChangeEvent;


public abstract class SignPlugin extends SignFunctions {

	// validate sign lines
	public abstract boolean ValidateSignFirst(SignChangeEvent event);
	public abstract void InvalidSign(SignChangeEvent event);

	// sign types
	protected List<SignType> signTypes = new ArrayList<SignType>();


	// add sign handler
	public void addSign(SignType signType) {
		if(signType == null) throw new NullPointerException("signType can't be null!");
		signTypes.add(signType);
	}


	// validate sign lines
	public boolean ValidateSign(SignChangeEvent event) {
		// plugin handles this sign
		if(this.ValidateSignFirst(event)) {
			for(SignType sign : signTypes) {
				if(sign.ValidateSign(event))
					return true;
			}
			// invalid sign
			InvalidSign(event);
		}
		return false;
	}


}