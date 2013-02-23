package com.ottamotta.goodsinstocksimulator.input;

/**
 * Class representing a single buy info
 * @author Rodion Altshuler
 *
 */
public class Buy {

	public int day;
	public int amount;
	
	
	@Override
	public String toString() {
		return "day: {" + day + "}; amount: {" + amount + "}"; 
	}
	
}

