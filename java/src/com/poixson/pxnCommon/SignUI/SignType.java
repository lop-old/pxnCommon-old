package com.poixson.pxnCommon.SignUI;


public abstract class SignType extends SignFunctions {

	// sign type
	public abstract String getType();

//	// sign clicked
//	public abstract void onClick(PlayerInteractEvent event);
//	// create sign
//	public abstract String onCreateSign(SignChangeEvent event);


	// sign type equals
	public boolean typeEquals(SignDAO sign) {
		return typeEquals(sign.getType());
	}
	public boolean typeEquals(String type) {
		if(type == null)
			return false;
		return type.equalsIgnoreCase(this.getType());
	}


}