package absClassifier.evaluation;

import db.DBController;
import model.Decision;
import utils.FileUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ABSEvaluateFromCsvMain {


    public static void main(String[] args){

        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();

        Map<String, Decision> goldenDecisions = new HashMap<>();
        Map<String, Decision> currentDecisions = dbc.getDecisionCurrentEntries();
        ABSCompareService absCompareService = new ABSCompareService();
        List<String> newGoldenStandard = FileUtil.getFileAsStringList("./resources/LawStatsIAGolden_without_text.csv");
        int counter = 0;

        for(String line: newGoldenStandard){
            String[] columns = line.split(",");
            Decision newGoldenDecision = new Decision();
            newGoldenDecision.setDecisionID(columns[0]);

                newGoldenDecision.setRevisionOutcome(Integer.parseInt(columns[2]));



            goldenDecisions.put(newGoldenDecision.getDecisionID(), newGoldenDecision);
        }


        absCompareService.evaluateClassifierQuality(goldenDecisions, currentDecisions);

    }

}
