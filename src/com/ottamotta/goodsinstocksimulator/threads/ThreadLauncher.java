package com.ottamotta.goodsinstocksimulator.threads;

import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.ottamotta.goodsinstocksimulator.input.Buy;
import com.ottamotta.goodsinstocksimulator.input.BuyGetter;

import android.util.Log;

/**
 * Class responsible for starting simulation threads
 * @author Rodion Altshuler
 *
 */
public class ThreadLauncher  {

	
	private final static String LOGTAG = "THREADLAUNCHER";
    private static String urlToJson = "https://sites.google.com/site/coffeerides/contacts/json.txt?attredirects=0&d=1";
	
	//default simulation params - just for example
	long deliveryDelay=420;
	long initialDelay=100, buyPeriod=20;	
	int startAmount = 1500, deliveryAmount = 4000, maxBuyAmount = 400;		
	int buyerWait=20;
	
	//ouhh, main player of this class!
	ScheduledThreadPoolExecutor scheduler = 
			new ScheduledThreadPoolExecutor(1000);
	
	
	public ThreadLauncher() {
		//If you wanna use default simulation params, You could use simple constuctor		
	}
	
	
	public ThreadLauncher(String url) {	
		this();
		urlToJson = url;
		BuyGetter getter = BuyGetter.getInstance();
		ArrayList<Buy> buyStatistic = getter.getHttpBuys(urlToJson);
		SalesParams params = BuyGetter.calculateSalesParams(buyStatistic);	
		setSalesParams(params);
		
	}
	
	private void setSalesParams(SalesParams salesParams) {

		deliveryDelay=salesParams.deliveryDelay*SalesParams.MILLIS_IN_DAY;
		deliveryAmount = salesParams.deliveryAmount;
		startAmount=salesParams.startAmount;
		
		maxBuyAmount=salesParams.maxBuyAmount;
		buyPeriod=salesParams.buyPeriod*SalesParams.MILLIS_IN_DAY;
		buyerWait=salesParams.buyerWait*SalesParams.MILLIS_IN_DAY;		
	
	}
	
	public ThreadLauncher(SalesParams salesParams) {
		
		//Set simulation paramas from SalesParams object		
		setSalesParams(salesParams);
		
	}
		

	/**
	 * Shuts down all threads and clears Store data
	 */
	public void interrupt() {					
		scheduler.shutdown();
		Store.clear();		
	};
	
	
	public void run() {
		
		/*
		Log.d(LOGTAG, "RUN simultation!");		
		Log.d(LOGTAG, "startAmount is " + startAmount);	
		Log.d(LOGTAG, "deliveryAmount is " + deliveryAmount);
		Log.d(LOGTAG, "deliveryDelay is " + deliveryDelay);
				
		
		Log.d(LOGTAG, "maxBuyAmount is " + maxBuyAmount);
		Log.d(LOGTAG, "buyerWait is is " + buyerWait);
		Log.d(LOGTAG, "initialDelay is " + initialDelay);
		Log.d(LOGTAG, "buyPeriod is " + buyPeriod);
		*/
		
		//TEST
	
		//getter.getTestBuys(365*3, maxBuyAmount/2, (int) buyPeriod);
		
		
		//reset days counter before new simulation
		Store.resetTime();	
		
		//start amount delivery
		schedule(new Delivery(startAmount), 0);	
		
		//deliveries		
		repeat(new Delivery(deliveryAmount), 0, deliveryDelay);
		
		//buyer threads
		repeat(new Buyer(maxBuyAmount, buyerWait), initialDelay, buyPeriod);
		
	}
	
	/**
	 * For schedule single run thread
	 * @param event
	 * @param delay
	 */
	public void schedule(Runnable event, long delay) {
		scheduler.schedule(event,delay,TimeUnit.MILLISECONDS);
	}
	
	
	/**
	 * Shedule thread for repeat running
	 * 
	 * @param event
	 * @param initialDelay
	 * @param period
	 */
	public void repeat(Runnable event, long initialDelay, long period) {
		scheduler.scheduleAtFixedRate(
		event, initialDelay, period, TimeUnit.MILLISECONDS);
		
	
		
	}
	
		
	
	
}
