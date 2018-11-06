package db;


import model.Decision;
import utils.DecisionUtil;

import java.sql.*;
import java.util.*;


public class DBController {

    private static final DBController dbcontroller = new DBController();
    public static Connection connection;
    private static final String DB_PATH = "./resources/LawStatsIAGolden.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Fehler beim Laden des JDBC-Treibers");
            e.printStackTrace();
        }
    }

    private DBController() {
    }

    public static DBController getInstance() {
        return dbcontroller;
    }

    public void initDBConnection() {
        try {
            if (connection != null)
                return;
            System.out.println("Creating Connection to database...");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            if (!connection.isClosed())
                System.out.println("...Connection established");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    if (!connection.isClosed() && connection != null) {
                        connection.close();
                        if (connection.isClosed())
                            System.out.println("Connection to database closed");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public Connection getConnection() {
        return connection;
    }


    /**
     * Makes a DB Entry into the DecisionCurrent Table.
     * @param decisionList a List of Decisions
     */
    public void makeDecisionCurrentDBEntry(List<Decision> decisionList) {
        try {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT OR REPLACE INTO DecisionCurrent VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");


            for (Decision decision : decisionList) {

                addDecisionInfoToPS(ps, decision);
                ps.setString(17, decision.getStudyTag());
               // ps.setDouble(18, decision.getDecisionComparison2().getFalsePositives());
                ps.addBatch();
            }

            makeDBEntry(ps);
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

    }


    /**
     * Makes a DB entry into the table DecisionGolden.
     * Every 20th Decision is set to blind = 1.
     * These Decisions are the BlindSet for later evaluation.
     * @param decisionList a List of Decisions
     */
    public synchronized void makeGoldenStandardDBEntry(List<Decision> decisionList, boolean setAllBlind) {

        try {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT OR REPLACE INTO DecisionGolden VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");


            int isBlind = -1;

            for (Decision decision : decisionList) {

                //marks every 20th decision as blind (part of blind set)
                if (setAllBlind) {
                    isBlind = 1;
                } else {
                    isBlind = -1;
                }

                addDecisionInfoToPS(ps, decision);
                ps.setInt(16, isBlind);
                ps.addBatch();
            }

            makeDBEntry(ps);
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }


    /**
     * Makes a DB entry into the table DecisionGolden.
     * Every 20th Decision is set to blind = 1.
     * These Decisions are the BlindSet for later evaluation.
     * @param decisionList a List of Decisions
     */
    public synchronized void makeGoldenStandardDBEntryAndSetBlind(List<Decision> decisionList) {
        int blindCounter = 0;




        try {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT OR REPLACE INTO DecisionGolden VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");


            int isBlind = -1;

            for (Decision decision : decisionList) {

                //marks every 20th decision as blind (part of blind set)
                if (blindCounter % 9 == 0) {
                    isBlind = 1;
                } else {
                    isBlind = -1;
                }
                blindCounter++;


                addDecisionInfoToPS(ps, decision);
                ps.setInt(16, isBlind);
                ps.addBatch();
            }

            makeDBEntry(ps);
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }

    /**
     * Makes DB entry into table DecisionText.
     * @param decisionTextMap Map where key = DecisionID and value = DocumentText
     */
    public void makeDecisionTextEntries(Map<String, String> decisionTextMap) {
        Iterator decisionTextIterator = decisionTextMap.entrySet().iterator();

        try {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO DecisionText VALUES (?, ?);");


            while (decisionTextIterator.hasNext()) {
                Map.Entry<String, String> decisionTextEntry = (Map.Entry<String, String>) decisionTextIterator.next();
                ps.setString(1, decisionTextEntry.getKey());
                ps.setString(2, decisionTextEntry.getValue());

                ps.addBatch();
            }


            makeDBEntry(ps);
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }



    public Map<String, String> getDecisionTextEntries() {

        Map<String, String> decisionTexts = new HashMap<>();


        try {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM DecisionText");


            while (results.next()) {
                String documentID = results.getString(1);
                String decisionText = results.getString(2);

                decisionTexts.put(documentID, decisionText);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return decisionTexts;
    }



    public Map<String, Decision> getDecisionCurrentEntries() {

        Map<String, Decision> decisionCurrentMap = new HashMap<>();


        try {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM DecisionCurrent");


            while (results.next()) {
                Decision decision = storeInfoInDecision(results);
                decision.setFullText(results.getString("Content"));

                String newDecisionId = DecisionUtil.retrieveDecisionIdFromFileName(decision.getDecisionID());

                if(newDecisionId != null) {
                    decision.setDecisionID(newDecisionId);
                }

                decisionCurrentMap.put(decision.getDecisionID(), decision);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return decisionCurrentMap;
    }


    public Map<String, Decision> getDecisionGoldenEntries(boolean blindSet, boolean getAll) {

        Map<String, Decision> decisionGoldens = new HashMap<>();


        try {
            String sqlStatement;
            if(getAll){
                sqlStatement = "SELECT * FROM DecisionGolden";
            }
            else if(blindSet){
                sqlStatement = "SELECT * FROM DecisionGolden WHERE IsBlind = 1";
            }
            else{
                sqlStatement = "SELECT * FROM DecisionGolden WHERE IsBlind = -1";
            }
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(sqlStatement);


            while (results.next()) {
                Decision decision = storeInfoInDecision(results);
                decision.setFullText(results.getString("Content"));
                decisionGoldens.put(decision.getDecisionID(), decision);
            }
        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }

        return decisionGoldens;
    }



    public synchronized void makeDBEntry(PreparedStatement ps) {

        try {
            connection.setAutoCommit(false);
            ps.executeBatch();
            connection.setAutoCommit(true);


        } catch (SQLException e) {
            System.err.println("Couldn't handle DB-Query");
            e.printStackTrace();
        }
        System.out.println("DB entry was made");
    }



    private Decision storeInfoInDecision(ResultSet results){
        Decision decision = new Decision();
        try {
            List<String> judgeList = DecisionUtil.makeListFromString(results.getString("Judges"));


            decision.setDecisionID(results.getString("DecisionID"));
            decision.setDocketNumber(results.getString("DocketNumber"));
            decision.setRevisionOutcome(results.getInt("RevisionOutcome"));
            decision.setRelevanceScore(results.getDouble("RelevanceScore"));
            decision.setSenate(results.getString("Senate"));
            decision.getJudgeList().addAll(judgeList);
            decision.setDateDecision(results.getLong("DateDecision"));
            decision.setDecisionOLG(results.getString("DecisionOLG"));
            decision.setDateOLG(results.getLong("DateOLG"));
            decision.setDecisionLG(results.getString("DecisionLG"));
            decision.setDateLG(results.getLong("DateLG"));
            decision.setDecisionAG(results.getString("DecisionAG"));
            decision.setDateAG(results.getLong("DateAG"));
            decision.getDecisionSentences().add(results.getString("DecisionSentence"));

        }catch(SQLException sqlE){
            sqlE.printStackTrace();
        }

        return decision;
    }



    private void addDecisionInfoToPS(PreparedStatement ps, Decision decision) {

        //The list have to be converted to Strings
        String judgeString = DecisionUtil.makeStringFromList(decision.getJudgeList());
        String decisionSentencesString = DecisionUtil.makeStringFromList(decision.getDecisionSentences());


        try {
            ps.setString(1, decision.getDecisionID());
            ps.setString(2, decision.getDocketNumber());
            ps.setInt(3, decision.getRevisionOutcome());
            ps.setDouble(4, decision.getRelevanceScore());
            ps.setString(5, decision.getSenate());
            ps.setString(6, judgeString);
            ps.setObject(7, decision.getDateDecision());
            ps.setString(8, decision.getDecisionOLG());
            ps.setObject(9, decision.getDateOLG());
            ps.setString(10, decision.getDecisionLG());
            //must not be ps.setLong to avoid NullpointerException
            ps.setObject(11, decision.getDateLG());
            ps.setString(12, decision.getDecisionAG());
            ps.setObject(13, decision.getDateAG());
            ps.setString(14, decisionSentencesString);
            ps.setString(15, decision.getFullText());


        } catch (SQLException sqlE) {
            sqlE.printStackTrace();
        }
    }


    public void closeConnection(){
        try{
            connection.close();
        }catch(SQLException sqlE){
            sqlE.printStackTrace();
        }
    }


    public void deleteTable(String tableName){
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM "+tableName);
        }catch(SQLException sqlE){
            sqlE.printStackTrace();
        }

    }
}

