package absClassifier.evaluation;

import db.DBController;
import preprocessing.Formatting.Formatter;
import utils.DecisionUtil;


import java.util.HashMap;
import java.util.Map;

public class DecisionTextsFix {


    public static void main(String[] args){

        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();

        Map<String, String> decisionTexts = dbc.getDecisionTextEntries();
        Map<String, String> newDecisionTexts = new HashMap<>();
        dbc.deleteTable("DecisionText");


        for(Map.Entry<String, String> decisionTextEntry: decisionTexts.entrySet()){

            String decisionId = decisionTextEntry.getKey();
            String decisionText = decisionTextEntry.getValue();

            decisionId = DecisionUtil.retrieveDecisionIdFromFileName(decisionId);
            //decisionText = Formatter.formatTextForString(decisionText);

            newDecisionTexts.put(decisionId, decisionText);
        }


        dbc.makeDecisionTextEntries(newDecisionTexts);


    }


}
