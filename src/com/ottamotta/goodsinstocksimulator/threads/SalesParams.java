package com.ottamotta.goodsinstocksimulator.threads;

/**
 * Contains info about sale and delivery conditions in certain company
 * @author Richie Blackmore
 *
 */
public class SalesParams {
	
	public static int MILLIS_IN_DAY = 10;
	
	public long deliveryDelay=120; //*MILLIS_IN_DAY; //how often delivery occurs
	public long initialDelay=0; //*MILLIS_IN_DAY; //first buy/sell delay
    public long buyPeriod=30; //*MILLIS_IN_DAY; //how often buyer comes

    public int buyerWait = 10; //*MILLIS_IN_DAY; //how much time buyer would wait before cancelling order
	
	public int startAmount = 10000; //starting stock
	public int deliveryAmount = 4000; //how many goods in one delivery
	public int maxBuyAmount = 300; //maximal buy amount of single buyer; 
						        	//average buy amount would be the half of it
	
}
