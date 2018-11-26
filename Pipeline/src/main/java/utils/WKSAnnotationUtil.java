package utils;

import org.json.JSONObject;

public class WKSAnnotationUtil {


    /**
     * Helper method to retrieve a sentence from a document String.
     *
     * @param documentText  The document text from which the sentence shall be extracted.
     * @param mentionObject The mention object holding the information about the position of the sentence.
     * @return the sentence indicated by the mention object.
     */
    public static String retrieveMentionSentenceFromDocument(String documentText, JSONObject mentionObject) {
        int beginIndex = mentionObject.getInt("begin");
        int endIndex = mentionObject.getInt("end");

        if(endIndex >= documentText.length()){
            int length = documentText.length();
            System.out.println("String index out of bounds");
        }

        String sentence = documentText.substring(beginIndex, endIndex);

        return sentence;
    }







}
