import aima.core.learning.learners.DecisionTreeLearner;
import aima.core.learning.framework.DataSetFactory;
import aima.core.learning.framework.DataSetSpecification;
import aima.core.learning.framework.DataSet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class Main {

	private static final String FIELDSEPERATOR = " ";

	/**
	 * setup the specification for the data set
	 */
	public static DataSetSpecification getSpecification() {
		// the dataset contains 6 attributes (a1, a2, ..., a6) with varying numbers of
		// classes
		DataSetSpecification dss = new DataSetSpecification();
		dss.defineStringAttribute("class", new String[] { "0", "1" });
		dss.defineStringAttribute("a1", new String[] { "1", "2", "3" });
		dss.defineStringAttribute("a2", new String[] { "1", "2", "3" });
		dss.defineStringAttribute("a3", new String[] { "1", "2" });
		dss.defineStringAttribute("a4", new String[] { "1", "2", "3" });
		dss.defineStringAttribute("a5", new String[] { "1", "2", "3", "4" });
		dss.defineStringAttribute("a6", new String[] { "1", "2" });
		dss.setTarget("class");
		return dss;
	}

	/**
	 * load at most maxNbLines lines from a file named filename + ".csv" and return
	 * a data set made from those lines
	 */
	public static DataSet loadDataSet(String filename, DataSetSpecification spec, int maxNbLines) throws Exception {
		DataSet ds = new DataSet(spec);
		// open file
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename + ".csv")));
		String line;
		// read file line by line and parse each line into an example that is added to
		// the data set
		while (maxNbLines > 0 && (line = reader.readLine()) != null) {
			ds.add(DataSetFactory.exampleFromString(line, spec, FIELDSEPERATOR));
			maxNbLines--;
		}
		reader.close();
		return ds;
	}

	/**
	 * like loadDataSet, but loads all lines in a file
	 */
	public static DataSet loadCompleteDataSet(String filename, DataSetSpecification spec) throws Exception {
		return loadDataSet(filename, spec, Integer.MAX_VALUE);
	}

	/**
	 * main procedure
	 */
	public static void main(String[] args) throws Exception {
		// setup the DataSetSpecification
		DataSetSpecification dss = getSpecification();

		/*
		 * load training and test data sets There are three data sets describing
		 * different concepts: monks-1 (124 training examples), monks-2 (169 training
		 * examples), monks-3 (122 training examples). Look at the end of monks.names,
		 * to see which concepts the data sets describe.
		 */
		String dataSetName = "monks-3"; // other sets are "monks-2" and "monks-3"
		int size = 122;
		int stepSize = 12;
		int remainder = size - (size % stepSize);
		DataSet testData;
		DataSet trainingData;
		// Take steps of four,
		for (int i = stepSize; i < size + 1; i += stepSize) {
			if (i == remainder && i != 0) {
				trainingData = loadCompleteDataSet(dataSetName + ".train", dss);
			} else {
				trainingData = loadDataSet(dataSetName + ".train", dss, i);
			}
			testData = loadCompleteDataSet(dataSetName + ".test", dss);
			// System.out.println("i: " + i + " size: " + trainingData.size());

			// initialize the learner
			DecisionTreeLearner dtl = new DecisionTreeLearner();

			// learn the decision tree using the training data
			dtl.train(trainingData);

			// test the decision tree using the test data
			int[] testResult = dtl.test(testData);
			int nbCorrect = testResult[0];
			int nbIncorrect = testResult[1];
			double accuracy = (nbCorrect * 100.0 / (nbCorrect + nbIncorrect));
			System.out.println(accuracy + "," + trainingData.size());

		}
		// // print out the learned tree
		// System.out.println("the learned tree:");
		// System.out.println(dtl.getDecisionTree());
	}
}
