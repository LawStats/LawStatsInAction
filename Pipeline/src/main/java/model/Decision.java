package model;

//import com.sun.javafx.beans.IDProperty;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class represents one document with the extracted date from watson
 */

public class Decision {




    private String decisionID;
    private String fullText;
    private String studyTag;
    private String docketNumber;
    private int revisionOutcome;
    private double relevanceScore;
    private String senate;
    private List<String> judgeList;
    private Long dateDecision;
    private DecisionComparison2 decisionComparison2;

    //Oberlandesgericht
    private String decisionOLG;
    private Long dateOLG;

    //Landesgericht
    private String decisionLG;
    private Long dateLG;



    //Amtsgericht
    private String decisionAG;
    private Long dateAG;


    private List<String> decisionSentences;
    private int documentNumber;


    public Decision(){
        decisionComparison2 = new DecisionComparison2();
    }



    public String getDocketNumber() {
        return docketNumber;
    }

    public void setDocketNumber(String docketNumber) {
        this.docketNumber = docketNumber;
    }

    public int getRevisionOutcome() {
        return revisionOutcome;
    }

    public void setRevisionOutcome(int revisionOutcome) {
        this.revisionOutcome = revisionOutcome;
    }

    public double getRelevanceScore() {
        return relevanceScore;
    }

    public void setRelevanceScore(double relevanceScore) {
        this.relevanceScore = relevanceScore;
    }

    public String getSenate() {
        return senate;
    }

    public void setSenate(String senate) {
        if (senate != null) {
            this.senate = senate;
        } else {
            this.senate = "";
        }
    }

    public List<String> getJudgeList() {
        if(judgeList == null){
            judgeList = new ArrayList<>();
        }

        return judgeList;
    }



    public Long getDateDecision() {
        return dateDecision;
    }

    public void setDateDecision(Long dateDecision) {
        this.dateDecision = dateDecision;
    }

    public String getDecisionOLG() {
        return decisionOLG;
    }

    public void setDecisionOLG(String decisionOLG) {
        this.decisionOLG = decisionOLG;
    }

    public Long getDateOLG() {
        return dateOLG;
    }

    public void setDateOLG(Long dateOLG) {
        this.dateOLG = dateOLG;
    }

    public String getDecisionLG() {
        return decisionLG;
    }

    public void setDecisionLG(String decisionLG) {
        this.decisionLG = decisionLG;
    }

    public Long getDateLG() {
        return dateLG;
    }

    public void setDateLG(Long dateLG) {
        this.dateLG = dateLG;

    }


    @Override
    public boolean equals(Object obj) {
        return obj instanceof Decision && Objects.equals(this.docketNumber, ((Decision) obj).getDocketNumber());
    }

    @Override
    public int hashCode() {
        return this.getDocketNumber().hashCode();
    }

    public List<String> getDecisionSentences() {
        if(decisionSentences == null){
            decisionSentences = new ArrayList<>();
        }

        return decisionSentences;
    }


    public void setDecisionSentences(List<String> decisionSentences){

        this.decisionSentences = decisionSentences;
    }


    public int getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(int documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getStudyTag() {
        return studyTag;
    }

    public void setStudyTag(String studyTag) {
        this.studyTag = studyTag;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }


    public String getDecisionAG() {
        return decisionAG;
    }

    public void setDecisionAG(String decisionAG) {
        this.decisionAG = decisionAG;
    }

    public Long getDateAG() {
        return dateAG;
    }

    public void setDateAG(Long dateAG) {
        this.dateAG = dateAG;
    }


    public String getDecisionID() {
        return decisionID;
    }

    public void setDecisionID(String decisionID) {
        this.decisionID = decisionID;
    }


    public DecisionComparison2 getDecisionComparison2() {
        return decisionComparison2;
    }

    public void setDecisionComparison2(DecisionComparison2 decisionComparison2) {
        this.decisionComparison2 = decisionComparison2;
    }


    public boolean hasDecisionSentence(){

        return !decisionSentences.isEmpty() && decisionSentences.get(0) != null;
    }

}
