package com.ottamotta.goodsinstocksimulator;

import com.ottamotta.goodsinstocksimulator.threads.Store;
import com.ottamotta.goodsinstocksimulator.threads.ThreadLauncher;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LaunchActivity extends Activity {

	ThreadLauncher launcher;
	StoreSimulation simulator;
	
	/**
	 * Frequency (in millis) how often UI is updated; 1000 - every 1s
	 */
	private final int UI_REFRESH_INTERVAL=1000;
	
	TextView tvAmount;
	TextView tvDaysGone;
	TextView tvLostSales;
	TextView tvTotalSales;
	
	ProgressBar pbDays;

	static final String LOGTAG = "LAUNCH ACTIVITY";
	static final int MILLIS_IN_DAY = 10;
	
	static final int SIMULATION_DAYS=365*3;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		try {
			simulator.cancel(true);
			launcher.interrupt();			
		} catch (Exception e) { e.printStackTrace(); }
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_launch);
		setContentView(R.layout.simulation_layout);
		
		
		tvAmount = (TextView) findViewById(R.id.tvAmount);
		tvDaysGone = (TextView) findViewById(R.id.tvDaysGone);
		tvLostSales = (TextView) findViewById(R.id.tvLostAmount);
		tvTotalSales = (TextView) findViewById(R.id.tvTotalSales);
		
		pbDays = (ProgressBar) findViewById(R.id.pbDays);
		
		simulator = new StoreSimulation();
		simulator.execute(SIMULATION_DAYS*MILLIS_IN_DAY, UI_REFRESH_INTERVAL);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_launch, menu);
		return true;
	}

	class StoreSimulation extends AsyncTask<Integer, Integer, Integer> {

		@Override
		protected Integer doInBackground(Integer... params) {
			
			int daysGone;
			
			//get data from previous activity to initialize simultaion parameters
			launcher = new ThreadLauncher(SetParamsActivity.salesParams);
			launcher.run();
			
			do {
				try {
					Thread.currentThread().sleep(params[1]);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}								
				daysGone = (int)Store.getDaysGone();
				int amount = Store.getInstance().available();
				int lostSales = Store.getInstance().getLostAmount();
				int totalSales = Store.getInstance().getTotalSell();
				publishProgress(amount, daysGone, lostSales, totalSales);
				
		
				
			} while(params[0]>daysGone);
			
			return null;
		
			
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			Store.clear();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {			
			super.onProgressUpdate(values);
			//print выводить текущие значения здесь
			tvAmount.setText(String.valueOf(values[0]));
			tvDaysGone.setText(String.valueOf(values[1]/MILLIS_IN_DAY));	
			tvLostSales.setText(String.valueOf(values[2]));
			tvTotalSales.setText(String.valueOf(values[3]));
			Log.d(LOGTAG, "values[1] = " + values[1] + "; pbDays.getMax= " + pbDays.getMax() + "; SIM_DAYS= " + SIMULATION_DAYS);
			Log.d(LOGTAG, "progress value: " + SIMULATION_DAYS/pbDays.getMax()*values[1]/MILLIS_IN_DAY);
			//pbDays.setProgress(SIMULATION_DAYS/pbDays.getMax()*values[1]/MILLIS_IN_DAY);
			//TODO make for any value of pb.getMax()
			pbDays.setProgress(values[1]/MILLIS_IN_DAY);
			
			
		}
		
	@Override
	protected void onPostExecute(Integer result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		launcher.interrupt();
		Toast.makeText(LaunchActivity.this, "Simulation is completed", Toast.LENGTH_LONG).show();
	}
	
		
	}
}
