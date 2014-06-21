package org.turtles.tortuga;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

public class MainActivity extends ActionBarActivity {
	public enum Direction {
		RIGHT, DOWN, LEFT, UP, NONE
	}
	
    private static Boolean shouldRecord = false;
    public static FileManager fileManager = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		/*mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);*/
	}
	
/*	@Override
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
	 
	}*/

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

		private static View rootView;
		private static Activity activity;
		public PlaceholderFragment() {
		}
		
		public static void emulateDirection(final Direction d) {
			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
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
			});
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
	         
	 		try {
				fileManager = new FileManager("training.arff", getActivity());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	 		
	 		activity = getActivity();
	         
			return rootView;
		}
	}
	
/*	public class PeriodicListener implements OnRecordPositionUpdateListener {

		@Override
		public void onMarkerReached(AudioRecord arg0) {
		}

		@Override
		public void onPeriodicNotification(AudioRecord arg0) {
		}
	}
*/
}
