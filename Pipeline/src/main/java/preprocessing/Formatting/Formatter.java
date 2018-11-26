package preprocessing.Formatting;


import org.apache.commons.io.IOUtils;
import utils.PropertyManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by tim on 21.02.2017.
 * Edited by phillip february 2018.
 */
public class Formatter {

    public static Replace[] replacements = {

            new Replace(" a\\. ", " am "),
            new Replace(" 1\\. ", " 1_ "),
            new Replace(" 2\\. ", " 2_ "),
            new Replace(" 3\\. ", " 3_ "),
            new Replace(" 4\\. ", " 4_ "),
            new Replace(" 5\\. ", " 5_ "),
            new Replace(" 6\\. ", " 6_ "),
            new Replace(" 7\\. ", " 7_ "),
            new Replace(" 8\\. ", " 8_ "),
            new Replace(" 9\\. ", " 9_ "),
            new Replace("i.V.m.", "in Verbindung mit" ),
            new Replace("VIII\\.", "VIII_"),
            new Replace("VII\\.", "VII_"),
            new Replace("XII\\.", "XII_"),
            new Replace("III\\.", "III_"),
            new Replace("II\\." , "II_"),
            new Replace("XI\\.", "XI_"),
            new Replace("IX\\.", "IX_"),
            new Replace("VI\\.", "VI_"),
            new Replace("IV\\.", "IV_"),
            new Replace("V\\.", "V_"),
            new Replace("I\\.", "I_"),
            new Replace("X\\. ", "10"),
            new Replace(" vgl\\. ", " vergleiche "),
            new Replace(" Abs\\. ", " Absatz "),
            new Replace(" Art\\. ", " Artikel "),
            new Replace(" Nr\\. ", " Nummer "),
            new Replace(" \\(vgl\\. ", " \\(vergleiche "),
            new Replace(" \\(Abs\\. ", " \\(Absatz "),
            new Replace(" \\(Art\\. ", " \\(Artikel "),
            new Replace(" \\(Nr\\. ", " \\(Nummer "),
            new Replace("Prof\\.", "Professor"),
            new Replace("Dr\\.", "Doktor"),
            new Replace("bzw\\.", "beziehungsweise"),
            new Replace("Buchst\\.", "Buchstabe"),
            new Replace("\f", ""),
            new Replace("Az\\.", "Aktenzeichen")
    };

    public static Map<String, Integer> monate = new HashMap<String, Integer>();


    
    public static void formatText(String textPath, String cleanPath) {


        String outFile = cleanPath;
        String content = formatText(textPath);

        if(content != null && !content.equals("")) {
            try {
                FileOutputStream fos = new FileOutputStream(outFile);
                IOUtils.write(content, fos, "UTF-8");


                fos.close();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }


    public static String formatText(String textPath){
        String inFile = null;
        String content = null;

        inFile = textPath;


        try {
            FileInputStream fis = new FileInputStream(inFile);
            content = IOUtils.toString(fis,"UTF-8");
            content = formatTextForString(content);


        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;
    }



    public static String formatTextForString(String content){

        monate.put("Januar", 1);
        monate.put("Februar", 2);
        monate.put("März", 3);
        monate.put("April", 4);
        monate.put("Mai", 5);
        monate.put("Juni", 6);
        monate.put("Juli", 7);
        monate.put("August", 8);
        monate.put("September", 9);
        monate.put("Oktober", 10);
        monate.put("November", 11);
        monate.put("Dezember", 12);




        List<String> shortcuts = (new ShortcutList(Boolean.valueOf(PropertyManager.getLawProperty(PropertyManager.DEPLOYMODE)))).getShortcuts();



        //ECLI Nummer
        String patternECLI = "(ECLI.+\\s)";

        Pattern pECLI = Pattern.compile(patternECLI);
        Matcher mECLI = pECLI.matcher(content);


        while(mECLI.find()) {

            String original = mECLI.group(1);
            String neu = "";

            content = content.replace(original, neu);

        }


        // Leerzeichen
        String pattern10 = "(^\\s\\s\\s)";

        Pattern p = Pattern.compile(pattern10);
        Matcher m = p.matcher(content);

        while(m.find()) {

            String original = m.group(1);
            String neu = "";

            content = content.replace(original, neu);


        }



        for(String s: shortcuts)
        {
            String original = s.replace(".", "\\.");

            //System.out.println(original);
            String replacement = s.replace(".", "_");


            String zeilenanfang = "\n" + original + " ";
            String zeilenanfangReplace = "\n" + replacement + " ";

            String leerzeichen = "\\s"+original+"\\s";
            String leerzeichenReplace = " "+replacement+" ";

            String klammern = "\\("+original+" ";
            String klammernReplace = "("+replacement+" " ;

            String klammern2 = " " + original + "\\)" ;
            String klammern2Replace = " " + replacement + ")" ;

            String zeilenende = "\\s"+original+"\n";
            String zeilenendeReplace = " "+ replacement + " ";

            String komma = "\\s"+original+"\\,";
            String kommaReplace = " "+ replacement + ",";

            String semikolon = "\\s"+original+"\\;";
            String semikolonReplace = " "+ replacement + ";";


            content = content.replaceAll(zeilenanfang, zeilenanfangReplace);
            content = content.replaceAll(leerzeichen,leerzeichenReplace );
            content = content.replaceAll(klammern, klammernReplace);
            content = content.replaceAll(klammern2,klammern2Replace );
            content = content.replaceAll(zeilenende,zeilenendeReplace );
            content = content.replaceAll(komma,kommaReplace );
            content = content.replaceAll(semikolon,semikolonReplace );
        }


        // Seitenzahlen
        String pattern3 = "(-\\s?\\d{1,2}\\s?-)";

        p = Pattern.compile(pattern3);
        m = p.matcher(content);

        while(m.find()) {

            String original = m.group(1);
            String neu = "";

            content = content.replace(original, neu);

            // System.out.println("Seitenzahl: "+ original);
        }




        // Datum: 12. Februar 2017 -> 12-2-2017
        String pattern = "((\\d(\\d)?)\\.\\s+?([äÄöÖüÜßa-zA-Z]+)\\s(\\d{4}))";

        p = Pattern.compile(pattern,Pattern.MULTILINE | Pattern.CANON_EQ);
        m = p.matcher(content);

        while(m.find()) {

            //System.out.println("Datum: "+m.group(1));
            String original = m.group(1);
            String neu = m.group(2) + "-" + monate.get(m.group(4)) + "-" + m.group(5);

            content = content.replace(original, neu);
        }

        // Datum: 12.02.17-> 12-2-2017
        String pattern7 = "(\\s(\\d{1,2})\\.(\\d{1,2})\\.(\\d{2}\\s|\\d{4}\\s))";

        p = Pattern.compile(pattern7);
        m = p.matcher(content);

        while(m.find()) {

            String original = m.group(1);
            String neu = " "+m.group(2) + "-" + m.group(3) + "-" + m.group(4);

            content = content.replace(original, neu);

            // System.out.println("Datum: "+m.group(1));
            //  System.out.println("DatumNeu: "+neu);
        }




        // Geldbeträge: 5.000,34 € -> 5000,34 €
        String pattern2 = "(\\s\\d{1,3}(\\.\\d{3})?(\\,\\d{1,2})?\\s?)€";

        p = Pattern.compile(pattern2);
        m = p.matcher(content);

        while(m.find()) {

            String original = m.group(1);
            String neu = original.replaceAll("\\.", "");

            content = content.replace(original, neu);

            //  System.out.println("Geldbetrag: "+ original);
        }



        //hier war ein Fehler
        // 30. -> 30
        String pattern4 = "(\\d+\\.\\D+)";

        p = Pattern.compile(pattern4);
        m = p.matcher(content);

        while(m.find()) {

            String original = m.group(1);
            String neu = original.replaceAll("\\.", "_");

            content = content.replace(original, neu);

            // System.out.println("Zahl: "+ original);
        }


        // Einfache Ersetzungen mittels Replacement Array
        for(Replace r : replacements) {
            content = content.replaceAll(r.from, r.to);
        }



        // Bindestriche
        String pattern5 = "([äÄöÖüÜßa-zA-Z]-[\\r?\\n]+[äÄöÖüÜßa-zA-Z])";

        p = Pattern.compile(pattern5,Pattern.MULTILINE | Pattern.CANON_EQ);
        m = p.matcher(content);

        while(m.find()) {

            String original = m.group(1);
            String neu = original.replaceAll("\\n", "");
            neu = neu.replaceAll("-", "");
            neu = neu.replaceAll("\\r", "");

            content = content.replace(original, neu);

            //  System.out.println("Wortunterbrechung: "+ original);
        }


        // Satzumbruch
       content = replaceAllNewLines(content);


        return content;

    }



    public static String replaceNewLines(String content){


        // Satzumbruch
        String pattern6 = "([äÄöÖüÜßa-zA-Z0-9]([\\r?\\n]+)[äÄöÖüÜßa-zA-Z0-9])";


        Pattern p = Pattern.compile(pattern6, Pattern.MULTILINE | Pattern.CANON_EQ);
        Matcher m = p.matcher(content);

        while(m.find()) {
            try {
                String original = m.group(1);

                String neu = original.replaceAll(m.group(2), " ");
                content = content.replace(original, neu);



            }catch(PatternSyntaxException pSE){
                pSE.printStackTrace();

                return "";
            }

        }


        return content;
    }


    public static String replaceAllNewLines(String content){


        // Satzumbruch
        String pattern6 = "(([\\r\\n]+?))";


        Pattern p = Pattern.compile(pattern6);
        Matcher m = p.matcher(content);

        while(m.find()) {
            try {
                String original = m.group(1);

                String neu = original.replaceAll(m.group(2), " ");
                content = content.replace(original, neu);



            }catch(PatternSyntaxException pSE){
                pSE.printStackTrace();

                return "";
            }

        }


        return content;
    }



}