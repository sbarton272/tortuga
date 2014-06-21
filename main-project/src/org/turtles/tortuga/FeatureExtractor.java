package org.turtles.tortuga;

public class FeatureExtractor {
	
	public double avg;
	public double std;
	public double total;
	public double maxFreq;
	public double maxFreqIndex;
	
	FeatureExtractor(double[] real, double[] imaginary) {
		this.total = 0;
		double totalSqrd = 0;
		int maxFreqIndex = 0;
		double maxFreqMag = 0;
		
		// iter through data, extract max and keep track of running sum
    	for (int i = 0; i < real.length; i++) {
    		double mag = real[i] * real[i] + imaginary[i] * imaginary[i];
    		this.total += Math.sqrt(mag);
    		totalSqrd += mag;
    		
    		if (mag > maxFreqMag) {
    			maxFreqMag = mag;
    			maxFreqIndex = i;
    		}
    	}
    	
    	this.avg = this.total / real.length;
    	this.std = (totalSqrd / real.length) - Math.pow(this.avg,2);
    	this.maxFreq = maxFreq;
    	this.maxFreqIndex = maxFreqIndex;
	}
}
