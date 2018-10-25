package utils;

import model.StudyEvaluation;

public class EvaluationUtil {
   /**
     * Prints out the scores from the StudyEvaluation
     *
     * @param studyEvaluation holds all the calculated Scores
     */
    public static void printOutScores(StudyEvaluation studyEvaluation) {

        System.out.println("Average accuracy: " + studyEvaluation.getAccuracy());
        System.out.println("Average Precision: " + studyEvaluation.getPrecision());
        System.out.println("Average Recall: " + studyEvaluation.getRecall());
        System.out.println("Average F1: " + studyEvaluation.getF1Score());
    }

}
