package org.turtles.tortuga;

import java.util.Arrays;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements SensorEventListener {
	public enum Direction {
		RIGHT, DOWN, LEFT, UP
	}
	
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private AudioRecord mAudioRecorder;
    
    private static final int RECORDER_SAMPLE_RATE = 44100;
    private static final int AUDIO_BUFFER_SIZE = 4096;
    private byte[] audioBuffer = new byte[AUDIO_BUFFER_SIZE];
    private List<Double> zMeasurements = new ArrayList<Double>();
    private static Boolean shouldRecord = false;
    
    private static TextView tvXAxis;
	private static TextView tvYAxis;
	private static TextView tvZAxis;
	
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
	            AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
		
		mAudioRecorder.read(audioBuffer, 0, AUDIO_BUFFER_SIZE);
		mAudioRecorder.setNotificationMarkerPosition(10000);
		mAudioRecorder.setPositionNotificationPeriod(1000);
		mAudioRecorder.setRecordPositionUpdateListener(new PeriodicListener());
		mAudioRecorder.startRecording();
		
		new Thread(new Runnable() {
	        public void run() {
	            while(true) {
	            	mAudioRecorder.read(audioBuffer, 0, AUDIO_BUFFER_SIZE);
	            }
	        }
	    }).start();
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		if (shouldRecord) {
			float x = event.values[0];
		    float y = event.values[1];
		    float z = event.values[2];
		    
		    tvXAxis.setText("x: " + String.valueOf(x));
		    tvYAxis.setText("y: " + String.valueOf(y));
		    tvZAxis.setText("z: " + String.valueOf(z));
		    
		    zMeasurements.add((double)z);
		    System.out.println("captured sample #" + zMeasurements.size());
		    final int limit = 256;
		    if (zMeasurements.size() == limit) {
		    	shouldRecord = false;		    	
		    	FFT f = new FFT(limit);
		    	
		    	double[] imaginary = new double[limit]; 
		    	double[] real = new double[limit];
		    	Double[] measurements = new Double[0];
		    	measurements = zMeasurements.toArray(measurements);
		    	for (int i = 0; i < limit; i++) {
		    		real[i] = measurements[i];
		    	}
		    	
		    	System.out.println("starting fft");
		    	f.fft(real, imaginary);
		    	FeatureExtractor features = new FeatureExtractor(real, imaginary);
		    	
		    	System.out.println("finished fft, printing out results");
		    	//StringBuffer output = new StringBuffer();
		    	for (int i = 0; i < 256; i++) {
		    		System.out.println(Math.sqrt(real[i] * real[i] + imaginary[i] * imaginary[i]));
		    		//output.append(real[i] + " + " + imaginary[i] + "i\n");
		    		//System.out.println(real[i] + " + " + imaginary[i] + "i\n");
		    	}
		    	//System.out.println(output);
		    	System.out.println("finished printing out results");
		    }
		    //System.out.println(x + ", " + y + ", " + z);
		}
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

		View rootView;
		public PlaceholderFragment() {
		}
		
		public void emulateDirection(Direction d) {
			final WebView wv = (WebView)rootView.findViewById(R.id.webView1);
			switch (d) {
			case RIGHT:
				wv.loadUrl("javascript:emuRight();");
				break;
			case DOWN:
				wv.loadUrl("javascript:emuDown();");
				break;
			case LEFT:
				wv.loadUrl("javascript:emuLeft();");
				break;
			case UP:
				wv.loadUrl("javascript:emuUp();");
				break;
				default: break;
			}
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			final WebView webview = (WebView)rootView.findViewById(R.id.webView1);
			WebSettings websettings = webview.getSettings();
			websettings.setJavaScriptEnabled(true);
			
			webview.clearCache(true);
			webview.loadUrl("http://tkatzen.github.io/2048/");
			webview.setOnTouchListener(null);
			
			final Button button1 = (Button) rootView.findViewById(R.id.button1);
	         button1.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					emulateDirection(Direction.LEFT);
	             }
	         });
	         final Button button2 = (Button) rootView.findViewById(R.id.button2);
	         button2.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					emulateDirection(Direction.UP);
	             }
	         });
	         final Button button3 = (Button) rootView.findViewById(R.id.button3);
	         button3.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					emulateDirection(Direction.DOWN);
	             }
	         });
	         final Button button4 = (Button) rootView.findViewById(R.id.button4);
	         button4.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					emulateDirection(Direction.RIGHT);
	             }
	         });
			
	         final Button btnRecord = (Button)rootView.findViewById(R.id.btnRecord);
	         btnRecord.setOnClickListener(new View.OnClickListener() {
	        	 public void onClick(View v) {
	        		 if (!shouldRecord) {
	        			 shouldRecord = true; 
	        			 System.out.println("started capturing samples...");
	        		 }
	        	 }
	         });
	         
	         final Button btnConfigure = (Button)rootView.findViewById(R.id.btnConfigure);
	         btnConfigure.setOnClickListener(new View.OnClickListener() {
	        	 public void onClick(View v) {
	        		 TrainingManager tm = new TrainingManager(getActivity());
	        		 tm.show();
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
			System.out.println(Arrays.toString(audioBuffer));
		}
	}
	
	private class FeatureExtractor {
		
		public double avg;
		public double std;
		public double total;
		public double max;
		public double maxFreq;
		
		FeatureExtractor(double[] real, double[] imaginary) {
			this.total = 0;
			double totalSqrd = 0;
			int maxFrqIndx = 0;
			double maxFreqMag = 0;
			
			// iter through data, extract max and keep track of running sum
	    	for (int i = 0; i < real.length; i++) {
	    		double mag = real[i] * real[i] + imaginary[i] * imaginary[i];
	    		this.total += Math.sqrt(mag);
	    		totalSqrd += mag;
	    		
	    		if (mag > maxFreq) {
	    			maxFreq = mag;
	    			maxFrqIndx = i;
	    		}
	    	}
	    	
	    	this.avg = this.total / real.length;
	    	this.std = (totalSqrd / real.length) - Math.pow(this.avg,2);
	    	this.max = maxFreq;
	    	this.maxFreq = maxFrqIndx;
		}
	}
}
