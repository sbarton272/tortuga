package org.turtles.tortuga;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import weka.classifiers.functions.LibSVM;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.Instance;

public class PredictionManager {
	
	private Instances trainingData;
	private LibSVM lsvm;
	
	private AudioRecord mAudioRecorder;
    
    private static final int RECORDER_SAMPLE_RATE = 44100;
    private static final int AUDIO_BUFFER_SIZE = 4096;
    private byte[] audioBuffer = new byte[AUDIO_BUFFER_SIZE];
    private static Context context;

	public PredictionManager(String trainingFilePath, Context context) throws Exception {

		// Read training data
		BufferedReader trainingReader =
				new BufferedReader(new FileReader(trainingFilePath));
		this.trainingData = new Instances(trainingReader);
		this.trainingData.setClassIndex(this.trainingData.numAttributes() - 1);
		trainingReader.close();

		// Create LibSVM classifier
		this.lsvm = new LibSVM();
		this.lsvm.buildClassifier(this.trainingData);
		
		this.context = context;
		mAudioRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
		            RECORDER_SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO,
		            AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(RECORDER_SAMPLE_RATE, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT));
		mAudioRecorder.startRecording();
		
		Timer t = new Timer(true);
		t.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				try {
					composeMetadata();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, 1000, 1000);
	}

	public String classify(double avg, double std, double total,
					double max, double maxFreq) throws Exception {
		// Create attributes
		Attribute avgAttr = new Attribute("avg", 0);
		Attribute stdAttr = new Attribute("std", 1);
		Attribute totalAttr = new Attribute("total", 2);
		Attribute maxAttr = new Attribute("max", 3);
		Attribute maxFreqAttr = new Attribute("maxFreq", 4);

		// Create unclassified instance
		Instance inst = new Instance(5);
		inst.setValue(avgAttr, avg);
		inst.setValue(stdAttr, std);
		inst.setValue(totalAttr, total);
		inst.setValue(maxAttr, max);
		inst.setValue(maxFreqAttr, maxFreq);

		// Classify the instance
		inst.setDataset(this.trainingData);
		double prediction = this.lsvm.classifyInstance(inst);
		System.out.println(prediction);
		return this.trainingData.classAttribute().value((int) prediction);
	}
	
	private void composeMetadata() throws IOException {
		mAudioRecorder.read(audioBuffer, 0, AUDIO_BUFFER_SIZE);
    	FFT f = new FFT(AUDIO_BUFFER_SIZE);
    	
    	double[] real = new double[AUDIO_BUFFER_SIZE];
    	double[] imaginary = new double[AUDIO_BUFFER_SIZE];
    	
    	for (int i= 0; i < AUDIO_BUFFER_SIZE; i++) {
    		real[i] = (double) audioBuffer[i];
    	}
    	f.fft(real, imaginary);
    	
    	FeatureExtractor extractor = new FeatureExtractor(real, imaginary);
    	
    	try {
			String classification = classify(extractor.avg, extractor.std, extractor.total, extractor.maxFreq, extractor.maxFreqIndex);
			if (classification.equals("right")) {
				MainActivity.PlaceholderFragment.emulateDirection(MainActivity.Direction.RIGHT);
				System.out.println("classified as right");
			} else if (classification.equals("down")) {
				MainActivity.PlaceholderFragment.emulateDirection(MainActivity.Direction.DOWN);
				System.out.println("classified as down");
			} else if (classification.equals("left")) {
				MainActivity.PlaceholderFragment.emulateDirection(MainActivity.Direction.LEFT);
				System.out.println("classified as left");
			} else if (classification.equals("up")) {
				MainActivity.PlaceholderFragment.emulateDirection(MainActivity.Direction.UP);
				System.out.println("classified as up");
			} else if (classification.equals("none")) {
				System.out.println("classified as none");
			} else {
				System.out.println("unknown classificiation");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
