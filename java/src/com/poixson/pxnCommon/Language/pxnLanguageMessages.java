package com.poixson.pxnCommon.Language;


public class pxnLanguageMessages {


	public static pxnLanguageMessages factory() {
		return new pxnLanguageMessages();
	}


	public pxnLanguageMessages(String language) {
		
	}
	// default english
	public pxnLanguageMessages() {
		this("en");
	}


}