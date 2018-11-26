package initialization;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import model.Decision;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.DecisionUtil;
import utils.FileUtil;
import utils.WKSAnnotationUtil;


public class GoldenStandardInitializer {


    /**
     * Helper method to retrieve the important sentences from all annotation files. (not just one)
     *
     * @param allFiles an Array containing all the files that shall be used.
     * @return a List of trainingsdata entry arrays.
     */
    public List<Decision> createDecisionsFromAnnotations(File[] allFiles) {
        List<Decision> decisions = new ArrayList<>();


        for (int i = 0; i < allFiles.length; i++) {

            File file = allFiles[i];
            String jsonAnnotation;

            jsonAnnotation = FileUtil.getStringFromFile(file);
            decisions.add(createDecisionFromAnnotations(jsonAnnotation));
        }

        return decisions;
    }


    /**
     * Method to get all mentions from a WKS annotation JSON file for one Decision.
     * Creates a new Decision and sets the extracted information in the decision.
     *
     * @param jsonString A Watson Knowledge Studio annotation JSON.
     * @return a Decision created from Human Annotation
     */
    private Decision createDecisionFromAnnotations(String jsonString) {

        //Declares and initializes variables
        Decision decision = new Decision();

        JSONObject jsonObject = new JSONObject(jsonString);

        String fileName = jsonObject.getString("name");
        String fullDocumentText = jsonObject.getString("text");

        decision.setFullText(fullDocumentText);
        //fullDocumentText = Formatter.replaceNewLines(fullDocumentText);

        JSONArray mentionsJsonArray = jsonObject.getJSONArray("mentions");
        Iterator<Object> mentionsArrayIterator = mentionsJsonArray.iterator();


        decision.setDecisionID(DecisionUtil.retrieveDecisionIdFromFileName(fileName));


        //Analyzes the JSON file and stores the retrieved information in the decision
        while (mentionsArrayIterator.hasNext()) {
            JSONObject mentionObject;
            String mentionType;
            String mentionText;


            mentionObject = (JSONObject) mentionsArrayIterator.next();
            mentionType = mentionObject.getString("type");
            mentionText = WKSAnnotationUtil.retrieveMentionSentenceFromDocument(fullDocumentText, mentionObject);

            if (mentionType.equals("Aktenzeichen")) {
                String finalDocketNumber = DecisionUtil.handleMultipleMentions(mentionText, decision.getDocketNumber());
                decision.setDocketNumber(finalDocketNumber);
            } else if (mentionType.equals("Datum")) {
                //String datumString = annoUtil.retrieveMentionSentenceFromDocument(fullDocumentText, mentionObject);
                //TODO converten und setzen
            } else if (mentionType.equals("Richter")) {
                decision.getJudgeList().add(mentionText);
            } else if (mentionType.equals("Gericht")) {
                handleCourtsAndUpdate(decision, mentionText);
            } else if (mentionType.equals("Revisionsmisserfolg")) {
                decision.getDecisionSentences().add(mentionText);
                decision.setRevisionOutcome(-1);
            } else if (mentionType.equals("Revisionserfolg")) {
                decision.getDecisionSentences().add(mentionText);
                decision.setRevisionOutcome(1);
            }


        }

        return decision;
    }


    /**
     * Maps the mentionText to a court type and sets the information in the decision.
     *
     * @param decision    The decision to be updated
     * @param mentionText The court name (with court description)
     * @require the corresponding mentionType to the mentionText must be "Gericht"
     */
    private void handleCourtsAndUpdate(Decision decision, String mentionText) {


        String[] words = mentionText.toLowerCase().split("\\s");
        boolean isOLG = (words[0].contains("oberland") && (words[0].endsWith("gericht") || words[0].endsWith("gerichts")))
                || words[0].contains("olg");

        boolean isLG = (words[0].contains("land") && (words[0].endsWith("gericht") || words[0].endsWith("gerichts")))
                || words[0].contains("lg");

        boolean isAG = (words[0].contains("amt") && (words[0].endsWith("gericht") || words[0].endsWith("gerichts")))
                || words[0].contains("ag");


        //IMPORTANT NOTICE: the order of the if statements is crucial
        //changing it will cause OLGs being identified as LGs
        if (isOLG) {
            String olg = "olg " + words[1];
            String finalOLG = DecisionUtil.handleMultipleMentions(olg, decision.getDecisionOLG());
            decision.setDecisionOLG(finalOLG);

        } else if (isLG) {
            String lg = "lg " + words[1];
            String finalLG = DecisionUtil.handleMultipleMentions(lg, decision.getDecisionLG());
            decision.setDecisionLG(finalLG);

        } else if (isAG) {
            String ag = "ag " + words[1];
            String finalAG = DecisionUtil.handleMultipleMentions(ag, decision.getDecisionAG());
            decision.setDecisionAG(finalAG);
        } else {
            System.out.println(mentionText);
            //throw new IllegalArgumentException("The given String could not be mapped to a court type.");
        }
    }

}





