package study.AttributeAnalyzer;

import model.Decision;
import utils.DictionaryUtil;
import utils.ListUtil;
import utils.TextUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JudgeAnalyzer {

    private static JudgeAnalyzer judgeAnalyzer;


    public static JudgeAnalyzer getInstance(){
        if(judgeAnalyzer == null){
            judgeAnalyzer = new JudgeAnalyzer();
        }

        return judgeAnalyzer;
    }

    private JudgeAnalyzer(){

    };

    /**
     * Analyzes a document for the BGH judges in charge of the decision.
     * Judges are found based on their position and the judge dictionary.
     *
     * @param decisionSentence the decision sentence of the document
     * @param lastSentences the last sentences of the document
     * @param decision the decision object corresponding to the document
     * @return The decision object with the judges set
     */
    public Decision analyzeDocumentForJudges(String decisionSentence, String lastSentences, Decision decision) {
        JudgeAnalyzer judgeAnalyzer = new JudgeAnalyzer();
        List<String> foundJudges = new ArrayList<>();


        if (decisionSentence != null) {
            foundJudges = judgeAnalyzer.analyzeFragementForJudges(decisionSentence);
        }



        if (foundJudges.size() < 3) {
            List<String> foundJudgesEnd = judgeAnalyzer.analyzeFragementForJudges(lastSentences);


            removeRedundantJudges(foundJudgesEnd);
            foundJudges = foundJudgesEnd;
        }



        removeRedundantJudges(foundJudges);
        resolveVonJudges(foundJudges);


        //sets the judges in  the decision
        decision.getJudgeList().clear();
        decision.getJudgeList().addAll(foundJudges);


        return decision;
    }


    /**
     * Analyzes a text fragement for judges based on the judge dictionary.
     * @param textFragement The fragement to analyze
     * @return a list of judges found in the text fragement
     */
    public List<String> analyzeFragementForJudges(String textFragement) {

        List<String> judges = DictionaryUtil.getListFromJudgeDictionary();
        judges = ListUtil.makeAllStringsLowerCase(judges);
        List<String> foundJudges = new ArrayList<>();

        List<String> wordsList = new ArrayList<>(Arrays.asList(textFragement.split("[\\s\\.\\?!:,]")));


        for(String word: wordsList){
            String wordLowerCase = word.toLowerCase();

            if(judges.contains(wordLowerCase)){
                foundJudges.add(word);
                judges.remove(wordLowerCase);
            }
        }



        return foundJudges;
    }


    /**
     * Maps judges that originally have a "von" or similar special identifiers in front of their name to that name.
     * @param foundJudges a list of judges found in the document
     */
    public void resolveVonJudges(List<String> foundJudges) {
        List<String> vonJudges = DictionaryUtil.getVonJudgesFromDictionary();
        vonJudges.forEach(vonJudge -> vonJudge = vonJudge.toLowerCase());


        for (int i = 0; i < foundJudges.size(); i++) {

            String foundJudge = foundJudges.get(i);
            String foundJudgeLowerCase = foundJudge.toLowerCase();


            if (foundJudgeLowerCase.equals("gelder")) {
                foundJudge = "van ".concat(foundJudge);
            } else if (foundJudgeLowerCase.equals("Mühlen")) {
                foundJudge = "von der ".concat(foundJudge);
            } else if (vonJudges.contains(foundJudgeLowerCase)) {
                foundJudge = "von ".concat(foundJudge);
            }
        }
    }


    /**
     * Removes judges from the beginning of the list until foundJudges.size() <= 5.
     * @param foundJudges A list of judges in the order they were found in the document
     */
    public void removeRedundantJudges(List<String> foundJudges){

        if(foundJudges.size() > 5){
            while(foundJudges.size() > 5){
                foundJudges.remove(0);
            }
        }
    }


}
