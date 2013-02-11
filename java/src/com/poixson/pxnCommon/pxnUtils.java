package com.poixson.pxnCommon;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


public class pxnUtils {


	// sleep thread
	public static void Sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	// current time ms
	public static long getCurrentMillis() {
		return System.currentTimeMillis();
	}


	// min/max value
	public static int MinMax(int value, int min, int max) {
		if(value < min) value = min;
		if(value > max) value = max;
		return value;
	}
	public static long MinMax(long value, long min, long max) {
		if(value < min) value = min;
		if(value > max) value = max;
		return value;
	}
	public static double MinMax(double value, double min, double max) {
		if(value < min) value = min;
		if(value > max) value = max;
		return value;
	}
	// min/max by object
	public static boolean MinMax(Integer value, int min, int max) {
		if(value == null) throw new NullPointerException("value cannot be null");
		boolean changed = false;
		if(value < min) {value = min; changed = true;}
		if(value > max) {value = max; changed = true;}
		return changed;
	}
	public static boolean MinMax(Long value, long min, long max) {
		if(value == null) throw new NullPointerException("value cannot be null");
		boolean changed = false;
		if(value < min) {value = min; changed = true;}
		if(value > max) {value = max; changed = true;}
		return changed;
	}
	public static boolean MinMax(Double value, double min, double max) {
		if(value == null) throw new NullPointerException("value cannot be null");
		boolean changed = false;
		if(value < min) {value = min; changed = true;}
		if(value > max) {value = max; changed = true;}
		return changed;
	}


	// random number
	public static int RND(int min, int max) {
		Random randomGen = new Random(getCurrentMillis());
		return randomGen.nextInt(max) + min;
	}
	// random number (unique)
	public static int uniqueRND(int min, int max, int last) {
		if(min == max) return min;
		if((max - min) == 1) {
			if(last == min)
				return max;
			else
				return min;
		}
		int number = 0;
		for(int i=0; i<10; i++) {
			number = RND(min, max);
			if(number != last) break;
		}
		return number;
	}


	// cast a collection to list
	public static <T> List<T> castList(Class<? extends T> clss, Collection<?> c) {
	    List<T> result = new ArrayList<T>(c.size());
	    for(Object o: c)
	    	result.add(clss.cast(o));
	    return result;
	}


	// md5
	public static String MD5(String str) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(str.getBytes());
		byte[] byteData = md.digest();
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xFF & byteData[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}


	// add strings with delimiter
	public static String addStringSet(String baseString, String addThis, String delim) {
		if(addThis.isEmpty())    return baseString;
		if(baseString.isEmpty()) return addThis;
		return baseString + delim + addThis;
	}
	public static String addStringSet(String baseString, List<String> addThis, String delim) {
		if(baseString == null) baseString = "";
		for(String line : addThis) {
			if(!baseString.isEmpty()) baseString += delim;
			baseString += line;
		}
		return baseString;
	}


}