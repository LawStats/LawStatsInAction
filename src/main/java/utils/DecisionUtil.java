package utils;

import model.Decision;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecisionUtil {

    /**
     * Helper method to ensure that multiple mentions for the same type are not overriden but stored together
     *
     * @param newMentionContent      The mentionText for which this method was called
     * @param existingMentionContent The mentions that have already been stored in the Decision
     * @return the concatenation of newMentionContent and existingMentionContent
     */
    public static String handleMultipleMentions(String newMentionContent, String existingMentionContent) {
        String concatenatedMentions;

        if (existingMentionContent == null || existingMentionContent.equals("")) {
            concatenatedMentions = newMentionContent;
        } else {
            concatenatedMentions = existingMentionContent.concat("," + newMentionContent);
        }

        return concatenatedMentions;
    }


    /**
     * Creates a String for a given list. The list elements are seperated by a comma (",").
     *
     * @param stringList A list of Strings to be used
     * @return A String where all list elements were concatenated
     */
    public static String makeStringFromList(List<String> stringList) {
        String concatenatedString = null;
        for (String s : stringList) {

            if (concatenatedString == null) {
                concatenatedString = s;
            } else {
                concatenatedString = concatenatedString.concat("," + s);
            }
        }

        return concatenatedString;
    }


    public static List<String> makeListFromString(String stringToSplit) {
        List<String> newList = new ArrayList<>();

        if (stringToSplit != null) {
            String[] splittedString = stringToSplit.split(",");
            newList = Arrays.asList(splittedString);
        }

        return newList;
    }


    public static void determineAndSetCourt(Decision decision, List<String> courts) {

        for (String court : courts) {
            determineAndSetCourt(decision, court);
        }

    }


    public static void determineAndSetCourt(Decision decision, String court) {

        boolean isOLG = court.startsWith("OLG") || court.equals("Kammergericht");
        boolean isLG = court.startsWith("LG");
        boolean isAG = court.startsWith("AG");


        //IMPORTANT NOTICE: the order of the if statements is crucial
        //changing it will cause OLGs being identified as LGs
        if (isOLG) {
            decision.setDecisionOLG(court);

        } else if (isLG) {
            decision.setDecisionLG(court);

        } else if (isAG) {
            decision.setDecisionAG(court);
        } else {
            //System.out.println(court);
            //throw new IllegalArgumentException("The given String could not be mapped to a court type.");
        }
    }


    public static String retrieveDecisionIdFromFileName(String filename) {

        String decisionId = filename;

        String pattern = ".*(verdict(\\d{5}))_.*";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(filename);

        if (m.find()) {

            decisionId = m.group(1);

        }

        return decisionId;
    }


    public static String getRevisionOutcomeFromInt(int revisionOutcome) {
        String revisionOutcomeString = null;

        switch (revisionOutcome) {
            case 1:
                revisionOutcomeString = "revisionsErfolg";
                break;
            case -1:
                revisionOutcomeString = "revisionsMisserfolg";
                break;
            case -99:
                revisionOutcomeString = "irrelevant";
                break;
        }

       return revisionOutcomeString;
    }

    public static String getStringAfterColon(String text){
        String[] sentenceParts = text.split(":");
        String stringAfterLastColon = sentenceParts[sentenceParts.length-1];

        return stringAfterLastColon;
    }


}
