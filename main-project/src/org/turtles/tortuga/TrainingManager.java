package org.turtles.tortuga;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

public class TrainingManager {
	private Context context;
	
	private int[] trainingCount = new int[MainActivity.Direction.values().length];
	private static AlertDialog.Builder builder;
	private final static int trainingGoal = 5;
	
	// Strings
	private static String titleUhOh = "Uh oh!";
	private static String strConfigCancelled = "Configuration was cancelled.";
	private static String strAllSet = "You're all set!";
	private static String strPleaseWait = "Please wait.";
	
    private AudioRecord mAudioRecorder;
    
    private static final int RECORDER_SAMPLE_RATE = 44100;
    private static final int AUDIO_BUFFER_SIZE = 8192;
    private byte[] audioBuffer = new byte[AUDIO_BUFFER_SIZE];
	
	public TrainingManager(Context context) {
		this.context = context;
		builder = new AlertDialog.Builder(context);
		mAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
		            RECORDER_SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
		            AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
		mAudioRecorder.startRecording();
	}
	
	public void show() {
		show(MainActivity.Direction.UP);
	}
	
	public void show(final MainActivity.Direction nextDirection) {
		if (trainingCount[nextDirection.ordinal()] >= trainingGoal) {
			builder.setTitle(strPleaseWait).setMessage("");
			final AlertDialog dialog = builder.create();
			dialog.show();
			final Timer t = new Timer(true);
			t.scheduleAtFixedRate(new TimerTask() {

				@Override
				public void run() {
					try {
						composeMetadata(MainActivity.Direction.NONE);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}, 2000, 2000);
			t.schedule(new TimerTask() {
				@Override
				public void run() {
					dialog.dismiss();
					try {
						passToPredictionManager();
					} catch (Exception e) {
						e.printStackTrace();
					};
					t.cancel();
				}
			}, trainingGoal * 2000);
			return;
		}
		trainingCount[nextDirection.ordinal()]++;
		
		String location = "";
		switch (nextDirection) {
		case UP: location = "ABOVE"; break;
		case DOWN: location = "BELOW"; break;
		case LEFT: location = "to the LEFT of"; break;
		case RIGHT: location = "to the RIGHT of"; break;
		case NONE: location = "nowhere on"; break;
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
				int nextIndex = (nextDirection.ordinal() + 1) % (MainActivity.Direction.values().length - 1);
				show(MainActivity.Direction.values()[nextIndex]);
			}
		});
		dialog.show();
		
		Timer t = new Timer(true);
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					composeMetadata(nextDirection);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.dismiss();
			}
		}, 2000);
	}
	
	private void showCancelledMessage() {
		builder.setMessage(strConfigCancelled).setTitle(titleUhOh);
		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	private void composeMetadata(MainActivity.Direction direction) throws IOException {
		
		mAudioRecorder.read(audioBuffer, 0, AUDIO_BUFFER_SIZE);
    	FFT f = new FFT(AUDIO_BUFFER_SIZE);
    	
    	double[] real = new double[AUDIO_BUFFER_SIZE];
    	double[] imaginary = new double[AUDIO_BUFFER_SIZE];
    	
    	for (int i= 0; i < AUDIO_BUFFER_SIZE; i++) {
    		real[i] = (double) audioBuffer[i];
    	}
    	f.fft(real, imaginary);
    	
    	FeatureExtractor extractor = new FeatureExtractor(real, imaginary);
    	
    	MainActivity.fileManager.append(extractor.avg, extractor.std, extractor.total, 
    			extractor.maxFreq, extractor.maxFreqIndex, direction.toString().toLowerCase());
	}
	
	private void passToPredictionManager() throws Exception {
		PredictionManager predictionManager = new PredictionManager(this.context.getFilesDir() + "/training.arff", context);
		
		
	}
}