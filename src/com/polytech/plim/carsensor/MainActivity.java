package com.polytech.plim.carsensor;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.Activity;
import android.content.Context;
import android.graphics.AvoidXfermode;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends Activity {

	private LocationManager vLocationManager;
	private mylocationlistener vLocationListener;
	private AudioManager vAudioManager;
	
	private int currentAudioMode;
	
	private CheckBox cb_silence,cb_avion,cb_off; 
	private Button bt_activate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		vLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		vLocationListener = new mylocationlistener();
		vLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, vLocationListener);

		vAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		currentAudioMode = vAudioManager.getMode();
		
		// Gestion CheckBox
		cb_silence = (CheckBox) findViewById(R.id.checkBox_Silence);
		cb_avion = (CheckBox) findViewById(R.id.checkBox_AirPlane);
		cb_off = (CheckBox) findViewById(R.id.checkBox_Off);
		
		cb_off.setChecked(true);
		cb_silence.setChecked(false);
		cb_avion.setChecked(false);
		
		cb_off.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(cb_off.isChecked()){
					cb_silence.setChecked(false);
					cb_avion.setChecked(false);
				}
				
			}
		});
		
		cb_avion.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(cb_avion.isChecked()){
					cb_off.setChecked(false);
					cb_silence.setChecked(false);
					
				}
				
			}
		});
		
		cb_silence.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if(cb_silence.isChecked()){
					cb_avion.setChecked(false);
					cb_off.setChecked(false);
				}
				
			}
		});

		//Gestion Bouton
		bt_activate = (Button) findViewById(R.id.button_activate);
		
		bt_activate.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				silenceMode(cb_silence.isChecked());
				AirPlaneMode(cb_avion.isChecked());
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	// Affichage et traitement de la vitesse instannée
	public void displaySpeed(double pCurrentSpeed)
	{
		// variable pour l'arrondi
		double vRounded =0;

		// Si vitesse compteur selectionnée on ajoute 7% à la vitesse indiquée par le GPS
		//        if (vCheckBoxVitesseCompteur.isChecked())
		//        {
		// arrondi à 1 chiffre après la virgule
		vRounded = roundDecimal(convertSpeed(pCurrentSpeed*1.07),1);
		//        }
		//        else
		//        {
		//            vRounded = roundDecimal(convertSpeed(pCurrentSpeed),1);
		//        }

		//        vTextViewSpeed.setText(String.valueOf(vRounded));
	}

	// fonction d'arrondie entree : le nombre a arrondir, et le nombre
	// de chiffres après la virgule désiré
	private double roundDecimal(double value, final int decimalPlace)
	{
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
		value = bd.doubleValue();
		return value;
	}

	// conversion de la vitesse GPS en km/h
	private double convertSpeed(double pSpeed){
		return pSpeed*3.6;
	}

	private class mylocationlistener implements LocationListener {

		// Ecouteur sur le GPS, appelé à chaque changement de position
		public void onLocationChanged(Location location)
		{
			// si une position est renvoyé par le GPS
			if (location != null)
			{

				// si une vitesse est détectée
				if (location.hasSpeed())
				{
					double vCurrentSpeed = location.getSpeed();

					// appel de la méthode displaySpeed en lui
					// passant la vitesse brute lu dans le GPS
					displaySpeed(vCurrentSpeed);
				}
			}
		}

		public void onProviderDisabled(String provider) {

		}

		public void onProviderEnabled(String provider) {

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

	} // fin class interne

	// Active/Desactive le mode avion
	public void AirPlaneMode(boolean state) {
		// turn airplane mode on :1 // turn airplane mode off : 0
		Settings.System.putInt(getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, (state)?1:0); 
	}
	
	//Active/Desactive le mode silencieux
	public void silenceMode(boolean state){
		if(state){
			vAudioManager.setMode(AudioManager.RINGER_MODE_SILENT);
		}
		else {
			vAudioManager.setMode(currentAudioMode);
		}
	}
}
