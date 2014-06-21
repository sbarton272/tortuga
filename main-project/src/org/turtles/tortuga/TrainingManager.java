package org.turtles.tortuga;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.Timer;
import java.util.TimerTask;

public class TrainingManager {
	private int[] trainingCount = new int[MainActivity.Direction.values().length];
	private static AlertDialog.Builder builder;
	private final static int trainingGoal = 2;
	
	// Strings
	private static String titleUhOh = "Uh oh!";
	private static String strConfigCancelled = "Configuration was cancelled.";
	private static String strAllSet = "You're all set!";
	
	public TrainingManager(Context context) {
		builder = new AlertDialog.Builder(context);
	}
	
	public void show() {
		show(MainActivity.Direction.UP);
	}
	
	public void show(final MainActivity.Direction nextDirection) {
		if (trainingCount[nextDirection.ordinal()] >= trainingGoal) {
			builder.setTitle(strAllSet);
			final AlertDialog dialog = builder.create();
			dialog.show();
			return;
		}
		trainingCount[nextDirection.ordinal()]++;
		
		String location = "";
		switch (nextDirection) {
		case UP: location = "ABOVE"; break;
		case DOWN: location = "BELOW"; break;
		case LEFT: location = "to the LEFT of"; break;
		case RIGHT: location = "to the RIGHT of"; break;
		}
		String msg = "Tap " + location + " your device.";
		builder.setMessage(msg).setTitle("Configuration");
		final AlertDialog dialog = builder.create();
		dialog.setOnCancelListener(new Dialog.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface di) {
				showCancelledMessage();
			}
		});
		dialog.setOnDismissListener(new Dialog.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface di) {
				int nextIndex = (nextDirection.ordinal() + 1) % MainActivity.Direction.values().length;
				show(MainActivity.Direction.values()[nextIndex]);
			}
		});
		dialog.show();
		
		Timer t = new Timer(true);
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				composeMetadata();
				dialog.dismiss();
			}
		}, 1000);
	}
	
	private void showCancelledMessage() {
		builder.setMessage(strConfigCancelled).setTitle(titleUhOh);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void composeMetadata() {
		// @stub Cheng should do all the FFT computation and storage by the time this method returns.
	}
}