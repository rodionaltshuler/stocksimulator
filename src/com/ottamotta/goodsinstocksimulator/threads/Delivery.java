package com.ottamotta.goodsinstocksimulator.threads;

public class Delivery implements Runnable {
	
	int amount;
	
	public Delivery(int amount) {
		this.amount=amount;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Store store = Store.getInstance();		
		store.deliver(amount);		
	}

}
