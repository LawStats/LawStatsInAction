package study;

import model.Decision;
import study.AttributeAnalyzer.CourtAnalyzer;
import study.AttributeAnalyzer.DateAnalyzer;
import study.AttributeAnalyzer.DocketNumberAnalyzer;
import study.AttributeAnalyzer.JudgeAnalyzer;
import utils.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecisionAnalyzerSimple implements DecisionAnalyzer {

    /**
     * Analyzes a document String for the entities DocketNumber, Judges, Preliminary Decision Courts, Decision Date, Preliminary Decision Dates.
     * All information found is stored in a Decision object.
     * The implementation of the extraction of the different attributes is realized in the corresponding Analyzer Services.
     *
     * @param documentText The text to analyze
     * @param decisionID The unique decision ID
     * @return a Decision object holding the extracted information
     */
    public Decision analyzeDecisionText(String documentText, String decisionID) {

        Decision decision = new Decision();

        String lastSentences = TextUtil.getSpecifiedSentencesFromEndAsString(documentText, 3);
        String decisionSentence = findDecisionSentence(documentText);
        if (decisionSentence != null) {
            decision.getDecisionSentences().add(decisionSentence);
//            System.out.println(decisionSentence);
//            System.out.println("-----");
//        }
//        else {
//            String abbrDocStr = documentText.substring(0, Math.min(documentText.length(), 1200));
//            System.out.println(decisionID);
//            System.out.println(abbrDocStr);
//            System.out.println("-----");
        }


        //find and set DocketNumber
        DocketNumberAnalyzer docketNumberAnalyzer = DocketNumberAnalyzer.getInstance();
        decision = docketNumberAnalyzer.analyzeDocumentForDocketNumber(documentText, decision);



        //find judges in decision sentence
        JudgeAnalyzer judgeAnalyzer = JudgeAnalyzer.getInstance();
        decision = judgeAnalyzer.analyzeDocumentForJudges(decisionSentence, lastSentences, decision);



        //find and set courts
        CourtAnalyzer courtAnalyzer = CourtAnalyzer.getInstance();
        decision = courtAnalyzer.analyzeDocumentForCourts(decisionSentence, lastSentences, decision); //courtAnalyzer.analyzeDocumentForCourts(lastSentences);



        //find and set DecisionDate
        //depends on courts being analyzed first
        DateAnalyzer dateAnalyzer = DateAnalyzer.getInstance();
        decision = dateAnalyzer.analyzeDocumentForDate(decisionSentence, lastSentences, decision);


        decision.setDecisionID(decisionID);
        decision.setFullText(documentText);


        return decision;
    }


    /**
     * Method to find the decision sentence in a document. (based on regex)
     * @param documentText The document text to analyze
     * @return The decisionSentence or null
     */
    public String findDecisionSentence(String documentText) {

        String decisionSentencePattern = "Der (([\\dIVX\\W]+.* des )?Bundesgerichtshof|Kartellsenat des Bundesgerichtshof|Senat für Notarsachen|Bundesgerichtshof, Senat für Landwirtschaftssachen,|Senat für Wirtschaftsprüfersachen beim Bundesgerichtshof)[\\w\\W.]*(beschlossen|für Recht erkannt)\\s*:.*";


        List<String> sentences = TextUtil.splitDocumentIntoSentences(documentText);
        Pattern p = Pattern.compile(decisionSentencePattern);
        String decisionSentence = null;


        for (String sentence : sentences) {
            Matcher m = p.matcher(sentence);

            if (m.find()) {
                decisionSentence = sentence;
                break;
            }
        }

        return decisionSentence;
    }


}







