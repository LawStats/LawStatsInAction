package study;

import model.Decision;
import model.DecisionComparison2;
import model.StudyEvaluation;

import java.util.List;

public class CalculationService {


    public StudyEvaluation calculateStudyScores(List<Decision> decisions) {
        StudyEvaluation studyEvaluation = new StudyEvaluation();
        CompareService compareService = new CompareService();
        CalculationService calculationService = new CalculationService();
        int truePositives = 0;
        int falsePositives = 0;
        int trueNegatives = 0;
        int falseNegatives = 0;
        int overallAmount = 0;


        for (Decision decision : decisions) {
            DecisionComparison2 decisionComparison2 = decision.getDecisionComparison2();

            truePositives += decisionComparison2.getTruePositives();
            falsePositives += decisionComparison2.getFalsePositives();
            trueNegatives += decisionComparison2.getTrueNegatives();
            falseNegatives += decisionComparison2.getFalseNegatives();
        }

        overallAmount = truePositives + falsePositives + trueNegatives + falseNegatives;


        double accuracy = calculationService.calculateAccuracy(truePositives, trueNegatives, overallAmount);
        double precision = calculationService.calculatePrecision(truePositives, falsePositives, falseNegatives);
        double recall = calculationService.calculateRecall(truePositives, falseNegatives);
        double f1Score = calculationService.calculateF1(precision, recall);

        studyEvaluation.setAccuracy(accuracy);
        studyEvaluation.setPrecision(precision);
        studyEvaluation.setRecall(recall);
        studyEvaluation.setF1Score(f1Score);

        return studyEvaluation;
    }




    public double calculateAccuracy(int truePositives, int trueNegatives, int totalNumberOfClassifications) {

        //is double to enable floating point result
        double numberOfCorrectClassifications = truePositives+trueNegatives;
        double accuracy = numberOfCorrectClassifications / totalNumberOfClassifications;


        return accuracy;
    }



    public double calculatePrecision(int truePositives, int falsePostives, int falseNegatives) {
        double numberOfTP = truePositives;
        double numberOfFP = falsePostives;
        double precision = 0;


        if((numberOfFP+numberOfTP)>0){
            precision = numberOfTP/(numberOfFP+numberOfTP);
        }
        else if(truePositives == 0 && falsePostives == 0 && falseNegatives == 0){
            precision = 1;
        }

        return precision;
    }



    public double calculateRecall(int truePositives, int falseNegatives) {
        double numberOfTP = truePositives;
        double numberOfFN = falseNegatives;
        double recall = 0;


        if((numberOfFN + numberOfTP) > 0) {
            recall = numberOfTP / (numberOfFN + numberOfTP);
        }

        return recall;
    }



    public double calculateF1(double precision, double recall) {
        double f1Score = 0;

        if(precision > 0 && recall > 0){
            f1Score = 2/((1/precision)+(1/recall));
        }

        return f1Score;
    }


}
