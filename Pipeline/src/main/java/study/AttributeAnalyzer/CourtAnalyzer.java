package study.AttributeAnalyzer;

import model.Decision;
import utils.DecisionUtil;
import utils.DictionaryUtil;
import utils.TextUtil;

import java.util.*;

public class CourtAnalyzer {

    private static CourtAnalyzer courtAnalyzer;



    List<Integer> _courtPositionsDecisionSentence;
    List<Integer> _courtPositionsEnd;


    public static CourtAnalyzer getInstance(){
        if(courtAnalyzer == null){
            courtAnalyzer = new CourtAnalyzer();
        }

        return courtAnalyzer;
    }

    private CourtAnalyzer(){
        _courtPositionsDecisionSentence = new ArrayList<>();
        _courtPositionsEnd = new ArrayList<>();
    };


    /**
     * Analyzes a document for preliminary decision courts.
     * @param lastSentences the last Sentences of the document
     * @param decisionSentence the decision sentence of the document
     * @return a List of possible preliminary decision courts
     */
    public Decision analyzeDocumentForCourts(String decisionSentence, String lastSentences, Decision decision) {
        _courtPositionsDecisionSentence = new ArrayList<>();
        _courtPositionsEnd = new ArrayList<>();

        Set<String> courtsFound = new HashSet<>();

        if(decisionSentence != null) {
            courtsFound = analyzeFragementForCourts(decisionSentence, true);
        }

        if(courtsFound.size() < 2){
           courtsFound.addAll(analyzeFragementForCourts(lastSentences, false));
        }


       List<String> courtsFoundList = new ArrayList<>(courtsFound);

       //sets the courts in the decision object
       DecisionUtil.determineAndSetCourt(decision, courtsFoundList);


        return decision;
    }


    /**
     * Analyzes a text fragement for preliminary decision courts.
     *
     * @param textFragement The text fragement that shall be analyzed
     * @param isDecisionSentence Determines if the text fragement is the decision sentence
     * @return a Set of possible preliminary decision courts
     */
    private Set<String> analyzeFragementForCourts(String textFragement, boolean isDecisionSentence) {

        Map<String, String> courtsMap = DictionaryUtil.getMapFromCourtDictionary();
        Iterator courtIterator = courtsMap.entrySet().iterator();
        Set<String> courtsFound = new HashSet<>();


        while (courtIterator.hasNext()) {
            Map.Entry<String, String> court = (Map.Entry<String, String>) courtIterator.next();

            if (textFragement.contains(court.getKey())) {
                courtsFound.add(court.getValue());

                int position = textFragement.indexOf(court.getKey());


                if(isDecisionSentence){
                    _courtPositionsDecisionSentence.add(position);
                }
                else{
                    _courtPositionsEnd.add(position);
                }
            }
        }


        return courtsFound;
    }



    public List<Integer> getCourtPositionsDecisionSentence() {
        return _courtPositionsDecisionSentence;
    }

    public List<Integer> getCourtPositionsEnd() {
        return _courtPositionsEnd;
    }




}
