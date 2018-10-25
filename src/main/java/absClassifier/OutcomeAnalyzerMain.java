package absClassifier;

import db.DBController;
import model.Decision;

import java.sql.SQLException;
import java.util.Map;

public class OutcomeAnalyzerMain {

    public static void main(String[] args){
        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();
        Map<String, Decision> allDecisionsMap = dbc.getDecisionCurrentEntries();
        try{
            dbc.getConnection().close();
        }catch(SQLException sqlE){
            sqlE.printStackTrace();
        }
        OutcomeAnalyzer outcomeAnalyzer = new OutcomeAnalyzer();
        outcomeAnalyzer.determineOutcomeForMultiple(allDecisionsMap);
    }

}
