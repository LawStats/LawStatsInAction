package initialization;

import db.DBController;
import utils.FileUtil;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

public class DecisionTextInitializer {

    public static void main(String[] args){
        DecisionTextInitializer decisionTextInitializer = new DecisionTextInitializer();
        File folder = new File("C:\\Users\\Phillip\\PraktikumSprachtechnologie\\Decisions\\verdictsTXTClean");
        File[] allFiles = folder.listFiles();

        Map<String, String> decisionTextMap = decisionTextInitializer.initializeDecisionText(allFiles);

        DBController dbc = DBController.getInstance();
        dbc.initDBConnection();
        dbc.makeDecisionTextEntries(decisionTextMap);

    }


    public Map<String, String> initializeDecisionText(File[] allFiles){

        Map<String, String> decisionTextMap = new HashMap<>();
        int counter = 0;

        for(File file: allFiles){

            String decisionID = file.getName();
            String decisionText = FileUtil.getStringFromFile(file);

            decisionTextMap.put(decisionID, decisionText);

            counter++;
            if(counter%1000 == 0){
                System.out.println("Verarbeitete Dateien: "+counter);
            }
        }


        return decisionTextMap;
    }


}
