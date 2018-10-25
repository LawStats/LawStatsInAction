package initialization;

import db.DBController;
import model.Decision;

import java.io.File;
import java.util.List;

public class GoldenStandardInitializerMain {


    public static void main(String[] args){

        File folder = new File("D:\\Studium\\LawStats\\Annotations");
        File[] allFiles = folder.listFiles();
        GoldenStandardInitializer gsi = new GoldenStandardInitializer();
        DBController dbc = DBController.getInstance();



        List<Decision> allDecisions = gsi.createDecisionsFromAnnotations(allFiles);

        System.out.println("");
        dbc.initDBConnection();
        dbc.makeGoldenStandardDBEntryAndSetBlind(allDecisions);



    }




}
