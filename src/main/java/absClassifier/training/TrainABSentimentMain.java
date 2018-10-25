package absClassifier.training;

import uhh_lt.ABSA.ABSentiment.AbSentiment;
import uhh_lt.ABSA.ABSentiment.featureExtractor.precomputation.ComputeIdfTermsCategory;
import utils.PropertyManager;

import java.io.File;

public class TrainABSentimentMain {

    public static void main(String[] args){


        TrainingsManager trainingsManager = new TrainingsManager();
        System.out.println(PropertyManager.ABS_CONFIGURATION);
        AbSentiment abSentiment = new AbSentiment(PropertyManager.getLawProperty(PropertyManager.ABS_CONFIGURATION));


        String[] parameterArray = new String[1];
        parameterArray[0] = PropertyManager.getLawProperty(PropertyManager.ABS_CONFIGURATION);

        //inducing features from training set and background corpus
        //uhh_lt.ABSA.ABSentiment.PreComputeFeatures.main(parameterArray);


        //analyzing the results (not always working)
       // ComputeIdfTermsCategory.computeIdfScores("./resources/config/ABSConfiguration.txt", "./resources/data/trainingfiles/trainingsData.tsv", "./resources/data/features/relevance_idf_terms.tsv", false, "relevance");



       trainingsManager.trainAbSentimentModel(false, 0.9, abSentiment);


    }
}
