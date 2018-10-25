package model;

public class StudyEvaluation {


    Decision decisionToCompare;

    Decision decisionStandard;

    double accuracy;
    double precision;
    double recall;
    double f1Score;


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

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public double getPrecision() {
        return precision;
    }

    public void setPrecision(double precision) {
        this.precision = precision;
    }

    public double getRecall() {
        return recall;
    }

    public void setRecall(double recall) {
        this.recall = recall;
    }

    public double getF1Score() {
        return f1Score;
    }

    public void setF1Score(double f1Score) {
        this.f1Score = f1Score;
    }


}
