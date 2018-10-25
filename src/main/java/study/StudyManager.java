package study;


import db.DBController;
import model.Decision;
import model.DecisionComparison2;
import model.StudyEvaluation;
import model.StudyTag;
import utils.EvaluationUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StudyManager {


    public void startStudy() {

        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();
        DecisionAnalyzer decisionAnalyzer = new DecisionAnalyzerSimple();
        List<Decision> allDecisions = new ArrayList<>();
        List<Decision> temporaryDecisions = new ArrayList<>();
        CompareService compareService = new CompareService();
        CalculationService calculationService = new CalculationService();
        StudyEvaluation studyEvaluation;


        //get decision texts
        Map<String, String> documentTexts = dbc.getDecisionTextEntries();
        Iterator documentTextsIterator = documentTexts.entrySet().iterator();
        //get golden standard decisions
        Map<String, Decision> decisionsGolden = dbc.getDecisionGoldenEntries(false, false);


        //for testing purposes
        int abortCounter = 0;


        while (documentTextsIterator.hasNext()) {

            Map.Entry<String, String> decisionTextEntry = (Map.Entry<String, String>) documentTextsIterator.next();






            //returns a new decision object based on the decision text
            Decision decision = decisionAnalyzer.analyzeDecisionText(decisionTextEntry.getValue(), decisionTextEntry.getKey());
            //gets the corresponding goldenstandard decision
            Decision decisionGolden = decisionsGolden.get(decision.getDecisionID());




            //compares the decisions
            if (decision.getDecisionID() != null && decisionGolden != null) {
                DecisionComparison2 decisionComparison2 = compareService.compareDecisions(decision, decisionGolden);
                decision.setDecisionComparison2(decisionComparison2);
            }

            allDecisions.add(decision);
            temporaryDecisions.add(decision);



            abortCounter++;
            if(abortCounter%1000 == 0){
                dbc.makeDecisionCurrentDBEntry(temporaryDecisions);
                temporaryDecisions.clear();
                System.out.println("Verarbeitete Dateien: "+abortCounter);
            }
        }



        //sets the study tag in each decision
        setStudyTag(allDecisions, StudyTag.COMPARE);

        //calculate the evaluation scores
        studyEvaluation = calculationService.calculateStudyScores(allDecisions);

        //prints the average score over all decisions
        EvaluationUtil.printOutScores(studyEvaluation);

        //stores the decisions in the database
        dbc.makeDecisionCurrentDBEntry(temporaryDecisions);
    }








    private void setStudyTag(List<Decision> decisions, String studyTag) {

        for (Decision decision : decisions) {
            decision.setStudyTag(studyTag);
        }

    }


}
