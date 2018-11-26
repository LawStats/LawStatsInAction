package study;

import model.Decision;
import model.StudyEvaluation;
import model.DecisionComparison2;

import java.util.ArrayList;
import java.util.List;

public class CompareService {

    /**
     * Compares two Decisions per attribute.
     * 0 = true positive, 1 = true negative, 2 = false positive, 3 = false negative
     * True positive = the same item as in the standard decision was found
     * True negative = no item was found at all, just as in the standard decision
     * False positive = a wrong item was found (compared to standard decision); a item was found and not item was found at all in the standard decision (only a special case)
     * False negative = no item was found at all, but an item existed (in the standard decision)
     *
     * @param decisionToCompare The new decision that shall be evaluated
     * @param decisionStandard  The decision used for evaluation
     * @return a StudyEvaluation object containing all necessary information about the comparison
     */
    public DecisionComparison2 compareDecisions(Decision decisionToCompare, Decision decisionStandard) {
        //List of true negatives etc.;
        // 0 = true positive, 1 = true negative, 2 = false positive, 3 = false negative
        List<Integer> evaluationResults = new ArrayList<>();
        DecisionComparison2 decisionComparison2;


        int evaluationResult = -1;


//        evaluationResults.add(compareObjects(decisionToCompare.getDocketNumber(), decisionStandard.getDocketNumber()));
//        evaluationResults.add(compareObjects(decisionToCompare.getDecisionSentences(), decisionStandard.getDecisionSentences()));
//        evaluationResults.add(compareObjects(decisionToCompare.getDateDecision(), decisionStandard.getDateDecision()));
//        evaluationResults.add(compareObjects(decisionToCompare.getRevisionOutcome(), decisionStandard.getRevisionOutcome()));
        evaluationResults.add(compareObjects(decisionToCompare.getDecisionOLG(), decisionStandard.getDecisionOLG()));
        evaluationResults.add(compareObjects(decisionToCompare.getDecisionLG(), decisionStandard.getDecisionLG()));
        evaluationResults.add(compareObjects(decisionToCompare.getDecisionAG(), decisionStandard.getDecisionAG()));
//        evaluationResults.add(compareObjects(decisionToCompare.getDateOLG(), decisionStandard.getDateOLG()));
//        evaluationResults.add(compareObjects(decisionToCompare.getDateLG(), decisionStandard.getDateLG()));
//        evaluationResults.add(compareObjects(decisionToCompare.getDateAG(), decisionStandard.getDateAG()));


//        evaluationResults.addAll(compareWithMultObjects(decisionToCompare.getJudgeList(), decisionStandard.getJudgeList()));


        decisionComparison2 = countAndSetResults(evaluationResults);

        return decisionComparison2;
    }


    public DecisionComparison2 compareDecisionsForAttribute(Decision decisionToCompare, Decision decisionStandard, String attribute) {
        //List of true negatives etc.;
        // 0 = true positive, 1 = true negative, 2 = false positive, 3 = false negative
        List<Integer> evaluationResults = new ArrayList<>();
        DecisionComparison2 decisionComparison2;


        int evaluationResult = -1;

        if (attribute.equals("docketNumber")) {
            evaluationResults.add(compareObjects(decisionToCompare.getDocketNumber(), decisionStandard.getDocketNumber()));
        } else if (attribute.equals("decisionSentence")) {
            evaluationResults.add(compareObjects(decisionToCompare.getDecisionSentences(), decisionStandard.getDecisionSentences()));
        } else if (attribute.equals("dateDecision")) {
            evaluationResults.add(compareObjects(decisionToCompare.getDateDecision(), decisionStandard.getDateDecision()));
        } else if (attribute.equals("revisionOutcome")) {
            evaluationResults.add(compareRevisionOutcome(decisionToCompare.getRevisionOutcome(), decisionStandard.getRevisionOutcome()));
        } else if (attribute.equals("decisionOLG")) {
            evaluationResults.add(compareObjects(decisionToCompare.getDecisionOLG(), decisionStandard.getDecisionOLG()));
        } else if (attribute.equals("decisionLG")) {
            evaluationResults.add(compareObjects(decisionToCompare.getDecisionLG(), decisionStandard.getDecisionLG()));
        } else if (attribute.equals("decisionAG")) {
            evaluationResults.add(compareObjects(decisionToCompare.getDecisionAG(), decisionStandard.getDecisionAG()));
        } else if (attribute.equals("dateOLG")) {
            evaluationResults.add(compareObjects(decisionToCompare.getDateOLG(), decisionStandard.getDateOLG()));
        } else if (attribute.equals("dateLG")) {
            evaluationResults.add(compareObjects(decisionToCompare.getDateLG(), decisionStandard.getDateLG()));
        } else if (attribute.equals("dateAG")) {
            evaluationResults.add(compareObjects(decisionToCompare.getDateAG(), decisionStandard.getDateAG()));
        } else if (attribute.equals("judges")) {
            evaluationResults.addAll(compareWithMultObjects(decisionToCompare.getJudgeList(), decisionStandard.getJudgeList()));
        }


        decisionComparison2 = countAndSetResults(evaluationResults);

        return decisionComparison2;
    }


    private DecisionComparison2 countAndSetResults(List<Integer> evaluationResults) {
        DecisionComparison2 decisionComparison2 = new DecisionComparison2();


        for (Integer evaluationResult : evaluationResults) {
            switch (evaluationResult) {
                case 0:
                    decisionComparison2.incrementTruePositives();
                    break;
                case 1:
                    decisionComparison2.incrementTrueNegatives();
                    break;
                case 2:
                    decisionComparison2.incrementFalsePositives();
                    break;
                case 3:
                    decisionComparison2.incrementFalseNegatives();
                    break;
            }
        }

        return decisionComparison2;
    }


    /**
     * Compares two list of objects (decision attributes).(e.g. judges)
     * Returns a list of numbers between 0-3 each representing:
     * 0 = true positive, 1 = true negative, 2 = false positive, 3 = false negative (for definition see compareDecision)
     * Each number in the list is the evaluation of one element in the list that shall be compared.
     * --> objectsToCompare.size() will always be equal to results.size()
     *
     * @param objectsToCompare the object that shall be compared
     * @param objectsStandard  the object used for comparison
     * @return an list of ints between 0 and 3
     */
    public List<Integer> compareWithMultObjects(List<String> objectsToCompare, List<String> objectsStandard) {

        // 0 = true positive, 1 = true negative, 2 = false positive, 3 = false negative

        List<Integer> evaluationResults = new ArrayList<>();
        int evaluationResult = -1;

        for (Object objectToCompare : objectsToCompare) {
            if (objectsStandard == null || objectsStandard.isEmpty()) {
                evaluationResult = ((objectToCompare == null) || objectsStandard.isEmpty()) ? 1 : 2;
            } else {
                if (objectsStandard.contains(objectToCompare)) {
                    evaluationResult = 0;
                } else if (objectToCompare == null) {
                    evaluationResult = 3;
                } else {
                    evaluationResult = 2;
                }
            }

            evaluationResults.add(evaluationResult);
        }

        return evaluationResults;
    }

    /**
     * Compares two objects (decision attributes).
     * Returns a number between 0-3 representing:
     * 0 = true positive, 1 = true negative, 2 = false positive, 3 = false negative
     * For definition of the classes see method compareDecisions.
     *
     * @param objectToCompare the object that shall be compared
     * @param objectStandard  the object used for comparison
     * @return an int between 0 and 3
     */
    public int compareObjects(Object objectToCompare, Object objectStandard) {

        // 0 = true positive, 1 = true negative, 2 = false positive, 3 = false negative
        int evaluationResult = -1;

        if (objectToCompare instanceof String) {
            objectToCompare = ((String) objectToCompare).toLowerCase();
        }


        if (objectStandard == null) {
            evaluationResult = (objectToCompare == null) ? 1 : 2;
        } else {
            if (objectStandard.equals(objectToCompare)) {
                evaluationResult = 0;
            } else if (objectToCompare == null) {
                evaluationResult = 3;
            } else {
                evaluationResult = 2;
            }
        }

        return evaluationResult;
    }



    public int compareRevisionOutcome(int outcomeToCompare, int outcomeStandard) {

        // 0 = true positive, 1 = true negative, 2 = false positive, 3 = false negative
        int evaluationResult = -1;


        if (outcomeStandard == -99) {
            evaluationResult = (outcomeToCompare == -99) ? 1 : 2;
        } else {
            if (outcomeStandard == outcomeToCompare) {
                evaluationResult = 0;
            } else if (outcomeToCompare == -99) {
                evaluationResult = 3;
            } else {
                evaluationResult = 2;
            }
        }

        return evaluationResult;
    }


}
