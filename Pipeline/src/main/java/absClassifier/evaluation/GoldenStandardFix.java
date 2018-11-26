package absClassifier.evaluation;

import db.DBController;
import model.Decision;
import utils.DecisionUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class GoldenStandardFix {

    public static void main(String[] args){
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();

        Map<String, Decision> decisionsGolden = dbc.getDecisionGoldenEntries(true, true);
        Set<Map.Entry<String, Decision>> decisionGoldenSet = decisionsGolden.entrySet();
        Map<String, String> decisionTexts = dbc.getDecisionTextEntries();
        dbc.deleteTable("DecisionGolden");

        for(Map.Entry<String, Decision> decisionGoldenEntry: decisionGoldenSet){
            Decision decision = decisionGoldenEntry.getValue();
            String decisionId = decision.getDecisionID();
            decisionId = DecisionUtil.retrieveDecisionIdFromFileName(decisionId);
            decision.setDecisionID(decisionId);

            String decisionText = decisionTexts.get(decisionId);
            decision.setFullText(decisionText);


            if(decision.getRevisionOutcome() == 0){
                decision.setRevisionOutcome(-99);
            }
        }

        dbc.makeGoldenStandardDBEntry(new ArrayList<>(decisionsGolden.values()), true);
    }


}
