package utils;

import preprocessing.Formatting.Formatter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    /**
     * Returns the specified number of sentences starting at the end of the document.
     *
     * @param documentText the text from which the sentences shall be extracted
     * @param numberOfSentences the number of sentences that shall be extracted
     * @return the sentences that were extracted
     */
    public static String getSpecifiedSentencesFromEndAsString(String documentText, int numberOfSentences) {
        List<String> allSentences = splitDocumentIntoSentences(documentText);
        int listLength = allSentences.size();
        String concatenatedSentences = "";

        for (int i = 0; i < numberOfSentences; i++) {

            int sentenceIndex = listLength - (1 + i);
            String sentence = "";

            if (sentenceIndex >= 0) {
                sentence = allSentences.get(sentenceIndex);
            }

            concatenatedSentences = sentence.concat(concatenatedSentences);
        }


        return concatenatedSentences;
    }


    /**
     * Splits a document into its sentences.
     *
     * @param documentText The text which is to be splitted into sentences.
     * @return A List of the sentences.
     */
    public static List<String> splitDocumentIntoSentences(String documentText) {

        return getSpecifiedSentencesFromStart(documentText, Integer.MAX_VALUE);
    }


    /**
     * Splits up the first sentences. The number of sentences to be splitted must be specified.
     *
     * @param documentText      The text where the sentences shall be splitted from.
     * @param numberOfSentences The number of sentences that shall be splitted.
     * @return A List of the splitted up sentences.
     */
    public static List<String> getSpecifiedSentencesFromStart(String documentText, int numberOfSentences) {
        String temporaryDocumentText = new String(documentText);
        int dotIndex = -1;
        String _sentence;
        List<String> _sentenceList = new ArrayList<>();

        while (temporaryDocumentText != null && !temporaryDocumentText.isEmpty() && !(numberOfSentences < 1)) {

            numberOfSentences--;
            dotIndex = temporaryDocumentText.indexOf(".");

            if (dotIndex == -1) {
                _sentence = temporaryDocumentText;
                temporaryDocumentText = "";
            } else {
                _sentence = temporaryDocumentText.substring(0, dotIndex + 1);
                temporaryDocumentText = temporaryDocumentText.substring(dotIndex + 1);
            }

            _sentence = Formatter.replaceAllNewLines(_sentence);
            _sentence = _sentence.trim();

            _sentenceList.add(_sentence);
        }

        return _sentenceList;
    }


    /**
     * Determines a text fragement based on the number of words before and after a given index.
     * "Words" are identified only on bases of whitespaces.
     * e.g. in "Hello - name is Foo." the "-" will be seen as a word.
     * @param index startin point for determining the fragement
     * @param wordsBefore the number of words BEFORE the given index that shall be included
     * @param wordsAfter the number of words AFTER the given index that shall be included
     * @param textToFragement the text from which the fragement shall be extracted
     * @return The text fragement
     */
    public static String getFragementFromText(int index, int wordsBefore, int wordsAfter, String textToFragement) {

        int spaceCountBefore = 0;
        int spaceCountAfter = 0;
        int indexBefore = index;
        int indexAfter = index;
        String newTextFragement;


        //it is <= instead of < because the first whitespace marks the word itself
        while (spaceCountBefore <= wordsBefore && indexBefore > 0) {

            indexBefore--;
            char currentCharBefore = textToFragement.charAt(indexBefore);

            if (Character.isWhitespace(currentCharBefore)) {
                spaceCountBefore++;
            }
        }


        //it is <= instead of < because the first whitespace marks the word itself
        while (spaceCountAfter <= wordsAfter && indexAfter < textToFragement.length()-1) {

            indexAfter++;
            char currentCharAfter = textToFragement.charAt(indexAfter);

            if (Character.isWhitespace(currentCharAfter)) {
                spaceCountAfter++;
            }
        }


        newTextFragement = textToFragement.substring(indexBefore, indexAfter+1);

        return newTextFragement;
    }
}
