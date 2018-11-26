package absClassifier.evaluation;

import db.DBController;
import model.Decision;

import java.util.Map;

public class ABSEvaluateMain {

    public static void main(String[] args){
        ABSCompareService absCompareService = new ABSCompareService();
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();         //TODO evtl. schließen der verbindung einbauen


        Map<String, Decision> decisionGoldenMap = dbc.getDecisionGoldenEntries(true, false);
        Map<String, Decision> decisionTestMap = dbc.getDecisionGoldenEntries(true, false);

        absCompareService.evaluateClassifierQuality(decisionGoldenMap, decisionTestMap);

    }


}
