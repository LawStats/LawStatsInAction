package absClassifier;

import db.DBController;
import model.Decision;
import uhh_lt.ABSA.ABSentiment.AbSentiment;
import uhh_lt.ABSA.ABSentiment.type.Result;
import utils.PropertyManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class OutcomeAnalyzer {

    AbSentiment _abSentiment;
    ABSDocumentAnalyzerNew _absDocumentAnalyzerNew;

    public OutcomeAnalyzer(){
        _abSentiment = new AbSentiment(PropertyManager.getLawProperty(PropertyManager.ABS_CONFIGURATION));
        _absDocumentAnalyzerNew = new ABSDocumentAnalyzerNew();
        _absDocumentAnalyzerNew.setAbSentiment(_abSentiment);
    }


    public void determineOutcomeForMultiple(Map<String, Decision> allDecisionsMap) {
        List<Decision> temporaryDecisions = new ArrayList<>();
        int counter = 0;
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();


        List<Decision> allDecisionsList = new ArrayList<>(allDecisionsMap.values());


        for (Decision decision : allDecisionsList) {

            determineRevisionOutcome(decision);
            temporaryDecisions.add(decision);


            counter++;
            if (counter % 100 == 0) {
                dbc.makeDecisionCurrentDBEntry(temporaryDecisions);
                temporaryDecisions.clear();
                System.out.println("Verarbeitete Dateien: " + counter);
            }
        }

        dbc.makeDecisionCurrentDBEntry(temporaryDecisions);
    }



    public Decision determineRevisionOutcome(Decision decision) {

        String decisionSentence = null;
        if(!decision.getDecisionSentences().isEmpty()) {
            decisionSentence = decision.getDecisionSentences().get(0);
        }

        if (decisionSentence != null) {
            Result absResult = _absDocumentAnalyzerNew.retrieveABSResultsForDocumentText(decisionSentence);
            _absDocumentAnalyzerNew.analyzeABSResultsAndUpdateVerdict(absResult, decision);
        } else {
            decision.setRevisionOutcome(-99);
            decision.setRelevanceScore(1);
        }


        return decision;
    }


}
