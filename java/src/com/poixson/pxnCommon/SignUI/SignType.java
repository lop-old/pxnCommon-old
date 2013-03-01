package com.poixson.pxnCommon.SignUI;


public abstract class SignType extends SignFunctions {

	// sign type
	protected abstract String getType();

//	// sign clicked
//	public abstract void onClick(PlayerInteractEvent event);
//	// create sign
//	public abstract String onCreateSign(SignChangeEvent event);


	// sign type equals
	protected boolean typeEquals(SignDAO sign) {
		return typeEquals(sign.getType());
	}
	protected boolean typeEquals(String type) {
		if(type == null)
			return false;
		return type.equalsIgnoreCase(this.getType());
	}


}