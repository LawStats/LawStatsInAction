package absClassifier.evaluation;

import absClassifier.OutcomeAnalyzer;
import db.DBController;
import model.Decision;
import model.DecisionComparison2;
import model.StudyEvaluation;
import study.CalculationService;
import study.CompareService;
import study.DecisionAnalyzerSimple;
import utils.EvaluationUtil;

import java.util.*;

public class ABSCompareService {

    public void evaluateClassifierQuality() {
        OutcomeAnalyzer outcomeAnalyzer = new OutcomeAnalyzer();
        CompareService compareService = new CompareService();
        DecisionAnalyzerSimple decisionAnalyzerSimple = new DecisionAnalyzerSimple();
        CalculationService calculationService = new CalculationService();
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();         //TODO evtl. schließen der verbindung einbauen

        Map<String, Decision> decisionGoldenMap = dbc.getDecisionGoldenEntries(true, false);
        Map<String, Decision> decisionTestMap = dbc.getDecisionGoldenEntries(true, false);


        Set<Map.Entry<String, Decision>> decisionsToTest = decisionTestMap.entrySet();

        for (Map.Entry<String, Decision> evaluatedDecisionEntry : decisionsToTest) {
            Decision decisionToTest = evaluatedDecisionEntry.getValue();
            Decision decisionGolden = decisionGoldenMap.get(decisionToTest.getDecisionID());

            if (decisionToTest.getFullText() != null) {
                decisionToTest = decisionAnalyzerSimple.analyzeDecisionText(decisionToTest.getFullText(), decisionToTest.getDecisionID());

                outcomeAnalyzer.determineRevisionOutcome(decisionToTest);

                //compares the decisions
                if (decisionToTest.getDecisionID() != null && decisionGolden != null) {
                    DecisionComparison2 decisionComparison2 = compareService.compareDecisionsForAttribute(decisionToTest, decisionGolden, "revisionOutcome");
                    decisionToTest.setDecisionComparison2(decisionComparison2);
                }

                decisionTestMap.put(decisionToTest.getDecisionID(), decisionToTest);
            }
        }

        StudyEvaluation studyEvaluation = calculationService.calculateStudyScores(new ArrayList<>(decisionTestMap.values()));
        EvaluationUtil.printOutScores(studyEvaluation);


    }


}
