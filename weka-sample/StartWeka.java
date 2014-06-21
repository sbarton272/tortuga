import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.functions.LibSVM;
import weka.classifiers.Evaluation;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.Instance;


public class StartWeka {

	public static void main(String[] args) throws Exception {

		// Read training data
		BufferedReader trainingReader =
				new BufferedReader(new FileReader("car-data/train.arff"));
		Instances trainingData = new Instances(trainingReader);
		trainingData.setClassIndex(trainingData.numAttributes() - 1);
		trainingReader.close();

		// Create LibSVM classifier
		LibSVM lsvm = new LibSVM();
		lsvm.buildClassifier(trainingData);

		// Create car attributes
		// Note: these attributes are nominal attributes. Creating a numeric
		//  attribute doesn't require anything more than new
		//  Attribute("attributename", index);
		FastVector buyingNominalValues = new FastVector(4);
		buyingNominalValues.addElement("vhigh");
		buyingNominalValues.addElement("high");
		buyingNominalValues.addElement("med");
		buyingNominalValues.addElement("low");
		Attribute buying = new Attribute("buying", buyingNominalValues, 0);
		FastVector maintNominalValues = new FastVector(4);
		maintNominalValues.addElement("vhigh");
		maintNominalValues.addElement("high");
		maintNominalValues.addElement("med");
		maintNominalValues.addElement("low");
		Attribute maint = new Attribute("maint", maintNominalValues, 1);
		FastVector doorsNominalValues = new FastVector(4);
		doorsNominalValues.addElement("2");
		doorsNominalValues.addElement("3");
		doorsNominalValues.addElement("4");
		doorsNominalValues.addElement("5more");
		Attribute doors = new Attribute("doors", doorsNominalValues, 2);
		FastVector personsNominalValues = new FastVector(3);
		personsNominalValues.addElement("2");
		personsNominalValues.addElement("4");
		personsNominalValues.addElement("more");
		Attribute persons = new Attribute("persons", personsNominalValues, 3);
		FastVector lugbootNominalValues = new FastVector(3);
		lugbootNominalValues.addElement("small");
		lugbootNominalValues.addElement("med");
		lugbootNominalValues.addElement("big");
		Attribute lugboot = new Attribute("lugboot", lugbootNominalValues, 4);
		FastVector safetyNominalValues = new FastVector(3);
		safetyNominalValues.addElement("low");
		safetyNominalValues.addElement("med");
		safetyNominalValues.addElement("high");
		Attribute safety = new Attribute("safety", safetyNominalValues, 5);
		
		// Create an unclassified instance
		Instance inst = new Instance(6);
		inst.setValue(buying, "low");
		inst.setValue(maint, "low");
		inst.setValue(doors, "5more");
		inst.setValue(persons, "more");
		inst.setValue(lugboot, "big");
		inst.setValue(safety, "high");

		inst.setDataset(trainingData);
		double prediction = lsvm.classifyInstance(inst);

		System.out.println("\nID: " + inst.value(0));
		System.out.println("\nPredicted: " +
						trainingData.classAttribute().value((int) prediction));

//		Evaluation eval = new Evaluation(train);
//		eval.crossValidateModel(lsvm, train, 10, new Random(1));
//		System.out.println(eval.toSummaryString("\nResults\n======\n", true));
//		System.out.println(eval.fMeasure(1) + " " + eval.precision(1) + " " + eval.recall(1));
	}
}
