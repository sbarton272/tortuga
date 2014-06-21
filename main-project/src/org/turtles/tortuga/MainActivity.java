package org.turtles.tortuga;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
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

}
