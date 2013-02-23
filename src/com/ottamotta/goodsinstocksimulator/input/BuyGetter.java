package com.ottamotta.goodsinstocksimulator.input;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ottamotta.goodsinstocksimulator.threads.SalesParams;

import android.util.Log;

/**
 * Handles buy data
 * 
 * @author Rodion Altshuler
 *
 */
public class BuyGetter {
	
	static int days_per_period=365;
	static double growth_koef=1.15; //15% annual growth
	
	static final String LOGTAG = "BuyGetter";
	
	ArrayList<Buy> buys;	
	static BuyGetter instance;
	
	private static int daysCounter;
	private static Random random = null;
	
	private  BuyGetter() {
		
	}
	
	public static synchronized BuyGetter getInstance(){						
			if (null==instance) {
				instance = new BuyGetter();
				random = new Random();
			}
			return instance;				
	}
	
	@Deprecated
	public ArrayList<Buy> getTestBuys(int days, int avgBuy, int buyIntervalDays){
		
		//работа с GSON, создание ArrayList  -> conver to GSON -> convert back and return		
		
		fillBuys(365*3, avgBuy, buyIntervalDays);
		
		//TODO а здесь надо рассчитать SalesParams на основе получившегося массива
		
		Gson gson = new Gson();
		String G = gson.toJson(buys);	
		
		Log.d(LOGTAG, "printing JSON");
		Log.d(LOGTAG, G);
		
		Log.d(LOGTAG, "printing JSON serialized from String");
		
		//deserialize		
		Type ArrayListType = new TypeToken<ArrayList<Buy>>() {}.getType();				
		ArrayList<Buy> buys2 = gson.fromJson(G, ArrayListType);
		
		for (Buy b2:buys2) {
			Log.d(LOGTAG, b2.toString());
		}
		
		//Buy b2 = gson.fromJson(json, classOfT)
		return buys;		
		
	}
	
	public ArrayList<Buy> getHttpBuys(final String url)  {
		// https://sites.google.com/site/coffeerides/contacts/json.txt?attredirects=0&d=1	
		
		HttpThread thread = new HttpThread(url);
		
		try {
			thread.start();
			thread.join();
			//JSONObject json = thread.json;
			//Log.d(LOGTAG, "Http json: ");
			//Log.d(LOGTAG, json.toString());
				
			Gson gson = new Gson();
			Type ArrayListType = new TypeToken<ArrayList<Buy>>() {}.getType();					
			ArrayList<Buy> buys2 = gson.fromJson(thread.jsonString, ArrayListType);			
			Log.d(LOGTAG, "printing JSON serialized from HTTP String!");
			for (Buy b2:buys2) {
				Log.d(LOGTAG, b2.toString());
			}
			return buys2;
			
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		return null;
	
		
	}
	
		
	
	private void fillBuys(int totalDays, int avgBuy, int buyIntervalDays) {
		
		//TODO пока что тут генерируются данные, но вообще-то они должны браться из какого-то
		//JSON
		
		buys = new ArrayList<Buy>();
		
		double koef; //=1; //initial growth koef
		
		while (daysCounter<totalDays) {
			//use sales grow parameters to calculate koef to multiply amount
			koef = Math.pow(growth_koef, (int)(daysCounter/days_per_period));
			Buy b = new Buy();
			daysCounter=daysCounter+random.nextInt(buyIntervalDays*2);
			//doesn't take into view season changes 
			b.amount=(int) (random.nextInt(avgBuy*2)*koef);		
			b.day=daysCounter;						
			buys.add(b);
			Log.d(LOGTAG, b.toString());
		}
		
	}
	

	class HttpThread extends Thread{
		
		public String jsonString;
		private String url; //url to JSON
		
		public HttpThread(String url) {
			this.url=url;
		}
		@Override
		public void run(){
			HttpsJsonGetter http = new HttpsJsonGetter();
			jsonString = http.getData(url);
		}
	}


	/**
	 * Self-explanatory name :)
	 * @param buyStatistic
	 * @return
	 */
	public static SalesParams calculateSalesParams(ArrayList<Buy> buyStatistic) {
		// TODO Auto-generated method stub
		
		/*
		 * 
		 *  Вставить расчет salesParams на основе статистики
		 * 
		 * 
		 */
		
		return new SalesParams(); //return default params
	}
}
