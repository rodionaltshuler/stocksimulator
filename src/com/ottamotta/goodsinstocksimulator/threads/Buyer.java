package com.ottamotta.goodsinstocksimulator.threads;

import java.util.Random;

public class Buyer implements Runnable {
	
	int amount;
	Random random;
	
	int waitTime; // = new SalesParams().buyerWait;
	
	public Buyer(int amount, int waitTime) {
		this.waitTime=waitTime;		
		this.amount=amount;
		random = new Random();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		int buyAmount;
		Store store = Store.getInstance();		
		buyAmount = random.nextInt(amount);
		int result=0;
				
		for (int i=0; i<waitTime; i++) {
			result = store.buy(buyAmount);
			if (result>0) { 
				break; 
			} else {
				//out of stock
				
			}
			try {
				Thread.sleep(1);
				//wait(waitTime/20);			
			} catch (InterruptedException e) {				
				e.printStackTrace();
			}			
			
			
		}
		
		if (0==result) {
			store.lostAmount(buyAmount);
		}
		
		//printer.print("Trying to buy " + buyAmount + ": bought " + result);
		
	}

}