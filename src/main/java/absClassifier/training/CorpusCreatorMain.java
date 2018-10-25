package absClassifier.training;

import db.DBController;
import model.Decision;
import preprocessing.Formatting.Formatter;
import utils.DecisionUtil;
import utils.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CorpusCreatorMain {

    public static void main(String[] args){
        createCorpus();
    }

    private static void createCorpus(){
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();
        Map<String, Decision> decisions = dbc.getDecisionCurrentEntries();
        List<String> corpusLines = new ArrayList<>();
        int counter = 0;

        for(Map.Entry<String, Decision> decisionEntry: decisions.entrySet()){
            counter++;
            String decisionText = null;
            Decision decision = decisionEntry.getValue();

            if(decision.hasDecisionSentence()){
                decisionText = DecisionUtil.getStringAfterColon(decision.getDecisionSentences().get(0));
            }else{
                decisionText = decision.getFullText();
            }


            decisionText = Formatter.replaceAllNewLines(decisionText);
            String line = decisionEntry.getKey()+"\t"+decisionText+"\n";
            corpusLines.add(line);

//            if(counter > 3000){
//                break;
//            }
        }


        FileUtil.deleteFile("./resources/data/corpus/corpus.tsv");
        FileUtil.writeMultipleLinesToFile(corpusLines, "./resources/data/corpus/corpus.tsv");



    }

}
