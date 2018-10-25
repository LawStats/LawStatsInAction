package absClassifier.training;

import db.DBController;
import model.Decision;
import preprocessing.Formatting.Formatter;
import uhh_lt.ABSA.ABSentiment.AbSentiment;
import utils.DecisionUtil;
import utils.FileUtil;
import utils.PropertyManager;


import java.util.ArrayList;

import java.util.List;
import java.util.Map;

public class TrainingsManager {


    public void trainAbSentimentModel(boolean useKfold, double testPercentage, AbSentiment abSentiment) {
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();

        Map<String, Decision> goldenStandardDecisions = dbc.getDecisionGoldenEntries(true, true);
        List<Decision> goldenStandardList = new ArrayList<>(goldenStandardDecisions.values());
        dbc.makeGoldenStandardDBEntryAndSetBlind(goldenStandardList);
        goldenStandardDecisions = dbc.getDecisionGoldenEntries(false, false);
        goldenStandardList = new ArrayList<>(goldenStandardDecisions.values());




//        int k = 5;
//        List<List<Decision>> decisionPartitions = new ArrayList<List<Decision>>(k);
//        int counter = 0;
//
//        for(Decision decision: goldenStandardList){
//            int listIndex = counter%k;
//
//            decisionPartitions.get(listIndex).add(decision);
//            counter++;
//        }
//
//
//        for(int i = 0; i < k; i++){
//            List<Decision> testDecisions = decisionPartitions.get(i);
//           //List<Decision> trainingsDecision =
//
//
//        }







        if (!useKfold) {
            List<String> trainingsDataRows = new ArrayList<>();


            for (Decision decision : goldenStandardList) {

                String line = getRowFromDecision(decision);

                if (line != null) {
                    trainingsDataRows.add(line);

                }
            }

            FileUtil.deleteFile(PropertyManager.getLawProperty(PropertyManager.TRAININGSDATA_FILE));
            FileUtil.deleteFile(PropertyManager.getLawProperty(PropertyManager.TESTDATA_FILE));

            FileUtil.writeMultipleLinesToFile(trainingsDataRows, PropertyManager.getLawProperty(PropertyManager.TRAININGSDATA_FILE));


            String[] parameterArray = new String[1];
            parameterArray[0] = PropertyManager.getLawProperty(PropertyManager.ABS_CONFIGURATION);


            // training model
            uhh_lt.ABSA.ABSentiment.TrainAllClassifiers.main(parameterArray);


        }


    }


    private String getRowFromDecision(Decision decision) {
        String line = null;

        if (!decision.getDecisionSentences().isEmpty() && decision.getDecisionSentences().get(0) != null) {


            String revisionOutcomeString = DecisionUtil.getRevisionOutcomeFromInt(decision.getRevisionOutcome());
            String decisionSentence = Formatter.replaceAllNewLines(decision.getDecisionSentences().get(0));
            decisionSentence = DecisionUtil.getStringAfterColon(decisionSentence);

            line = decision.getDecisionID() + "\t" + decisionSentence + "\t" + revisionOutcomeString + "\n";
        }

        return line;
    }





}
