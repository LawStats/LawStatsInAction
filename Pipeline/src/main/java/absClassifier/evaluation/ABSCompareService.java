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
import utils.FileUtil;

import java.util.*;

public class ABSCompareService {

    public void evaluateClassifierQuality(Map<String, Decision> decisionsGolden, Map<String, Decision> decisionsTest) {
        OutcomeAnalyzer outcomeAnalyzer = new OutcomeAnalyzer();
        CompareService compareService = new CompareService();
        DecisionAnalyzerSimple decisionAnalyzerSimple = new DecisionAnalyzerSimple();
        CalculationService calculationService = new CalculationService();


        //ursprüngliche implementierung bis zu HotFix 28.10.18 nach langer pause
        //Map<String, Decision> decisionGoldenMap = dbc.getDecisionGoldenEntries(true, false);
        //Map<String, Decision> decisionTestMap = dbc.getDecisionGoldenEntries(true, false);

        Map<String, Decision> decisionGoldenMap = new HashMap<>(decisionsGolden);
        Map<String, Decision> decisionTestMap = new HashMap<>(decisionsTest);
        int counter = 0;


        Set<Map.Entry<String, Decision>> decisionsToTest = decisionTestMap.entrySet();

        for (Map.Entry<String, Decision> evaluatedDecisionEntry : decisionsToTest) {

            //QuickFix to make it more robust
            try {
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

                    FileUtil.writeLineToFile(decisionToTest.getDecisionID() +"\t"+decisionToTest.getRevisionOutcome()+"\t"+decisionGolden.getRevisionOutcome()+"\t"+decisionToTest.getDecisionComparison2().getComparisonValue()+"\n", "./resources/AnalyzingResults.tsv");
                    decisionTestMap.put(decisionToTest.getDecisionID(), decisionToTest);
                    System.out.println("Das Urteil:" + decisionToTest.getDecisionID() + "was tested.");
                    counter++;
                    if ((counter % 10) == 0) {
                        System.out.println(counter);
                    }

                }
            }catch(Throwable boese){
                boese.printStackTrace();
            }
        }

        StudyEvaluation studyEvaluation = calculationService.calculateStudyScores(new ArrayList<>(decisionTestMap.values()));
        EvaluationUtil.printOutScores(studyEvaluation);


    }


}
