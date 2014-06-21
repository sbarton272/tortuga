package org.turtles.tortuga;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity extends ActionBarActivity implements SensorEventListener {
	
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private AudioRecord mAudioRecorder;
    
    private static final int RECORDER_SAMPLE_RATE = 8000;
    private static final int AUDIO_BUFFER_SIZE = 4096;
    private byte[] audioBuffer = new byte[AUDIO_BUFFER_SIZE];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		mAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
	            RECORDER_SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
	            AudioFormat.ENCODING_PCM_16BIT, AUDIO_BUFFER_SIZE);
		
		mAudioRecorder.setRecordPositionUpdateListener(new PeriodicListener());
		mAudioRecorder.setPositionNotificationPeriod(100);
		mAudioRecorder.startRecording();
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
		
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
	    float y = event.values[1];
	    float z = event.values[2];
	    
	    //System.out.println(x + ", " + y + ", " + z);
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			final WebView webview = (WebView)rootView.findViewById(R.id.webView1);
			WebSettings websettings = webview.getSettings();
			websettings.setJavaScriptEnabled(true);
			
			webview.loadUrl("http://tkatzen.github.io/2048/");
			
			final Button button1 = (Button) rootView.findViewById(R.id.button1);
	         button1.setOnClickListener(new View.OnClickListener() {
	             @SuppressLint("NewApi")
				public void onClick(View v) {
	            	 webview.loadUrl("javascript:emuLeft();");
	                 // Perform action on click
	             }
	         });
	         final Button button2 = (Button) rootView.findViewById(R.id.button2);
	         button2.setOnClickListener(new View.OnClickListener() {
	             @SuppressLint("NewApi")
				public void onClick(View v) {
	            	 webview.loadUrl("javascript:emuUp();");
	                 // Perform action on click
	             }
	         });
	         final Button button3 = (Button) rootView.findViewById(R.id.button3);
	         button3.setOnClickListener(new View.OnClickListener() {
	             @SuppressLint("NewApi")
				public void onClick(View v) {
	            	 webview.loadUrl("javascript:emuDown();");
	                 // Perform action on click
	             }
	         });
	         final Button button4 = (Button) rootView.findViewById(R.id.button4);
	         button4.setOnClickListener(new View.OnClickListener() {
	             @SuppressLint("NewApi")
				public void onClick(View v) {
	            	 webview.loadUrl("javascript:emuRight();");
	                 // Perform action on click
	             }
	         });
			
			return rootView;
		}
	}
	
	public class PeriodicListener implements OnRecordPositionUpdateListener {

		@Override
		public void onMarkerReached(AudioRecord arg0) {
			// TODO Auto-generated method stub
			
			System.out.println("Hello");
			
		}

		@Override
		public void onPeriodicNotification(AudioRecord arg0) {
			
			System.out.println("Hello2");
			mAudioRecorder.read(audioBuffer, 0, AUDIO_BUFFER_SIZE);
			System.out.println(Arrays.toString(audioBuffer));
		}
	}
}
