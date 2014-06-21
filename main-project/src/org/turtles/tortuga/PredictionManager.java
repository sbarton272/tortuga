import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.functions.LibSVM;
import weka.classifiers.Evaluation;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.Instance;

public class PredictionManager {
	
	private Instances trainingData;
	private LibSVM lsvm;

	public PredictionManager(String trainingFilePath) throws Exception {

		// Read training data
		BufferedReader trainingReader =
				new BufferedReader(new FileReader(trainingFilePath));
		this.trainingData = new Instances(trainingReader);
		this.trainingData.setClassIndex(this.trainingData.numAttributes() - 1);
		trainingReader.close();

		// Create LibSVM classifier
		this.lsvm = new LibSVM();
		this.lsvm.buildClassifier(this.trainingData);
	}

	public String classify(double avg, double std, double total,
					double max, double maxFreq) {

		// Create attributes
		Attribute avgAttr = new Attribute("avg", 0);
		Attribute stdAttr = new Attribute("std", 1);
		Attribute totalAttr = new Attribute("total", 2);
		Attribute maxAttr = new Attribute("max", 3);
		Attribute maxFreqAttr = new Attribute("maxFreq", 4);

		// Create unclassified instance
		Instance inst = new Instance(5);
		inst.setValue(avgAttr, "avg");
		inst.setValue(stdAttr, "std");
		inst.setValue(totalAttr, "total");
		inst.setValue(maxAttr, "max");
		inst.setValue(maxFreqAttr, "maxFreq");

		// Classify the instance
		inst.setDataset(this.trainingData);
		double prediction = this.lsvm.classifyInstance(inst);
		return this.trainingData.classAttribute().value((int) prediction);
	}
}
