package com.poixson.pxnCommon;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class uniqueHistoryRND {

	private int min;
	private int max;
	private int last;
	private BlockingQueue<Integer> history = new LinkedBlockingQueue<Integer>();
	private int historySize;


	public uniqueHistoryRND(int min, int max) {
		this(min, max, (max - min) / 2);
	}
	public uniqueHistoryRND(int min, int max, int historySize) {
		if(min > max) throw new IllegalArgumentException("min must be lower than max!");
		this.min = min;
		this.max = max;
		this.historySize = pxnUtils.MinMax(historySize, 2, (max-min)-1);
		this.last = min - 1;
	}


	// random number (unique history)
	public int RND() {
		if(min == max) return min;
		if((max - min) == 1) {
			if(last == min) {
				last = max;
				return max;
			} else {
				last = min;
				return min;
			}
		}
		int number = 0;
		int i = 10;
		while(true) {
			i--;
			number = pxnUtils.RND(min, max);
			if(!history.contains(number)) break;
//			if(i < 0 && number != last) break;
		}
		last = number;
		history.add(number);
		while (history.size() > historySize)
			history.remove();
		return number;
	}


}