package com.ottamotta.goodsinstocksimulator;


import com.ottamotta.goodsinstocksimulator.threads.SalesParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SetParamsActivity extends Activity {

	/**
	 * Parameters for simulation
	 */
	public static SalesParams salesParams;
	
	/**
	 * UI
	 */
	EditText etDeliveryDelay,
			 etDeliveryAmount,
			 etStartAmount,
	         etBuyPeriod,
	         etBuyAmount,
	         etBuyerWait;	
	Button btnShowFuture;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sales_params_input);
		
		initUI();
		
		
		btnShowFuture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				//save user input to static public variable that used by LauncherActivity
				saveParamsData();
				//launch simulation
				Intent intent = new Intent(SetParamsActivity.this, LaunchActivity.class);
				startActivity(intent);				
			
			}
		});
		
	}
	
	/**
	 * UI elements initialization
	 */
	private void initUI(){
		
		btnShowFuture = (Button) findViewById(R.id.btnShowFuture);
		etDeliveryDelay = (EditText) findViewById(R.id.etDeliveryInterval);
		etDeliveryAmount = (EditText) findViewById(R.id.etDeliveryAmount);
		etStartAmount = (EditText) findViewById(R.id.etStartAmount);
		etBuyPeriod = (EditText) findViewById(R.id.etBuyInteval);
		etBuyAmount = (EditText) findViewById(R.id.etAvgSale);
		etBuyerWait = (EditText) findViewById(R.id.etBuyerWait);
		
	}
	
	/**
	 * Save user input to SalesParams object
	 */
	private void saveParamsData() {
		
		salesParams = new SalesParams();
		
		salesParams.buyerWait = Integer.valueOf(etBuyerWait.getText().toString());
		salesParams.buyPeriod = Integer.valueOf(etBuyPeriod.getText().toString());
		salesParams.deliveryAmount = Integer.valueOf(etDeliveryAmount.getText().toString());
		salesParams.deliveryDelay = Integer.valueOf(etDeliveryDelay.getText().toString());
		salesParams.maxBuyAmount = Integer.valueOf(etBuyAmount.getText().toString())*2;
		salesParams.startAmount = Integer.valueOf(etStartAmount.getText().toString());
		
		
		//initialize UI elements
		//salesParams.
		
	}
}
