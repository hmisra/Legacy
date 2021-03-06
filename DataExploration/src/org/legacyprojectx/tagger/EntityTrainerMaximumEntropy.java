package org.legacyprojectx.tagger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

public class EntityTrainerMaximumEntropy {
	@SuppressWarnings("deprecation")
	public static void trainEntityModelMaximumEntropy(String trainingFilePath, String outputModelFilePath)
	{
		POSModel model = null;

		InputStream dataIn = null;
		try {
			dataIn = new FileInputStream(trainingFilePath);
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream<POSSample> sampleStream = new WordTagSampleStream(lineStream);

			model = POSTaggerME.train("en", sampleStream, TrainingParameters.defaultParams(), null, null);
		}
		catch (IOException e) {
			// Failed to read or parse training data, training failed
			e.printStackTrace();
		}
		finally {
			if (dataIn != null) {
				try {
					dataIn.close();
				}
				catch (IOException e) {
					// Not an issue, training already finished.
					// The exception should be logged and investigated
					// if part of a production system.
					e.printStackTrace();
				}
			}
		}
		OutputStream modelOut = null;
		try {
			modelOut = new BufferedOutputStream(new FileOutputStream(new File(outputModelFilePath)));
			model.serialize(modelOut);
		}
		catch (IOException e) {
			// Failed to save model
			e.printStackTrace();
		}
		finally {
			if (modelOut != null) {
				try {
					modelOut.close();
				}
				catch (IOException e) {
					// Failed to correctly save model.
					// Written model might be invalid.
					e.printStackTrace();
				}
			}
		}
	}
	
	public static String[] tagWords(String[] tokens) throws InvalidFormatException, IOException
	{
		InputStream modelIn1 = new FileInputStream("/Users/himanshumisra/Desktop/ETME-en.bin");
		POSModel model = new POSModel(modelIn1);
		POSTaggerME tagger = new POSTaggerME(model);
		String[] tags= tagger.tag(tokens);
		modelIn1.close();
		return tags;
	}

}
