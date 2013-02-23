package com.ottamotta.goodsinstocksimulator.threads;

import android.util.Log;

/**
 * 
 * Singleton class accumulating info about deliveries, sales, lost sales
 *
 */
public class Store {

	//TODO timer реализовать в этом классе вместо InfoPrinter
	private static long time=System.currentTimeMillis();
		
	public static void resetTime() {
		time = System.currentTimeMillis();
	}
	
	public static long getDaysGone() {
		return System.currentTimeMillis()-time;
	}
	
	
	private final static String LOGTAG = "STORE";
	
	private static Store instance;
	
	private int amount;
	private int totalSell, totalDelivery;
	private int lostAmount;
	
	private Store() {		
	};
	
	
	public static synchronized Store getInstance() {
		if (null==instance) {			
			instance = new Store();
		}		
		return instance;
	}
	
	/** 
	 * Returns quantity of goods available
	 */
	public synchronized int available() {
		return amount;		
	}
	
	
	/**
	 * Decreases stock to amount buyed;
	 */
	public synchronized int buy(int amount) {
		
		//check whether available this amount
		if (amount<=this.amount) {
			doBuy(amount);
			return amount;
		} else {
			//insufficient amount
			//make client wait
		}
		return 0;
				
	}
	
	private synchronized void doBuy(int amount) {
		this.amount = this.amount - amount;
		totalSell = totalSell+amount;
		//TODO fix time and other info needed about agreement
	}
	
	
	public synchronized void deliver(int amount) {
		//if financial logic will be added, check whether have we money for this
		//delivery or not
		
		doDelivery(amount);
		
	}
	
	private synchronized void doDelivery(int amount) {
		this.amount = this.amount + amount;
		totalDelivery = totalDelivery + amount;	
	}


	public synchronized int getTotalSell() {
		return totalSell;
	}


	public synchronized int getTotalDelivery() {
		return totalDelivery;
	}


	public synchronized void lostAmount(int amount) { 
		this.lostAmount = this.lostAmount + amount;
	}
	
	public synchronized int getLostAmount() {
		return lostAmount;
	}


	public static void clear() {
		// TODO Auto-generated method stub
		Log.d(LOGTAG, "Store cleared");
		instance=null;	
	}

	
	
	
}
