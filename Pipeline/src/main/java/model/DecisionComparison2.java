package model;

public class DecisionComparison2 {

    Decision decisionToCompare;
    Decision decisionStandard;

    int truePositives;
    int falsePositives;
    int trueNegatives;
    int falseNegatives;


    public void incrementTruePositives(){
        truePositives++;
    }

    public void incrementFalsePositives(){
        falsePositives++;
    }

    public void incrementTrueNegatives(){
        trueNegatives++;
    }

    public void incrementFalseNegatives(){
        falseNegatives++;
    }


    public Decision getDecisionToCompare() {
        return decisionToCompare;
    }

    public void setDecisionToCompare(Decision decisionToCompare) {
        this.decisionToCompare = decisionToCompare;
    }

    public Decision getDecisionStandard() {
        return decisionStandard;
    }

    public void setDecisionStandard(Decision decisionStandard) {
        this.decisionStandard = decisionStandard;
    }

    public int getTruePositives() {
        return truePositives;
    }

    public void setTruePositives(int truePositives) {
        this.truePositives = truePositives;
    }

    public int getFalsePositives() {
        return falsePositives;
    }

    public void setFalsePositives(int falsePositives) {
        this.falsePositives = falsePositives;
    }

    public int getTrueNegatives() {
        return trueNegatives;
    }

    public void setTrueNegatives(int trueNegatives) {
        this.trueNegatives = trueNegatives;
    }

    public int getFalseNegatives() {
        return falseNegatives;
    }

    public void setFalseNegatives(int falseNegatives) {
        this.falseNegatives = falseNegatives;
    }

    public int getComparisonValue(){
        int value = -2;

        if(truePositives != 0){
            value = 0;
        }else if(trueNegatives != 0){
            value = 1;
        }else if(falsePositives != 0){
            value = 2;
        }else if(falseNegatives != 0){
            value = 3;
        }

        return value;
    }
}
