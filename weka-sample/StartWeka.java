import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Random;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instances;

public class StartWeka {

	public static void main(String[] args) throws Exception {

		if(args.length < 1) {
				System.out.println("Sorry, I need a file path provided.");
		}

		BufferedReader bufferedReader = null;
		bufferedReader = new BufferedReader(new FileReader(args[0]));

		Instances train = new Instances(bufferedReader);
		train.setClassIndex(train.numAttributes() - 1);

		bufferedReader.close();

		NaiveBayes nB = new NaiveBayes();
		nB.buildClassifier(train);
		Evaluation eval = new Evaluation(train);
		eval.crossValidateModel(nB, train, 10, new Random(1));
		System.out.println(eval.toSummaryString("\nResults\n======\n", true));
		System.out.println(eval.fMeasure(1) + " " + eval.precision(1) + " " + eval.recall(1));
	}
}
